package edu.purdue.cs59000.umltranslator.umlcodehandler;

import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;
import edu.purdue.cs59000.umltranslator.message.UMLCreateMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLContainer;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLElse;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLLoop;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.javaparser.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

/**
 * The UMLSDClassHandler class defines the source code generation of a UMLCreateMessage object. It correctly identifies the location of both the method signature
 * and invocation, and places them in the AST representation accordingly.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLSDSynchronousMessageHandler extends UMLCodeHandler {
	public UMLSDSynchronousMessageHandler() {}
	
	/**
	 * The generateSourceCode method correctly identifies the location of both the method signature and invocation, and places them in the AST representation 
	 * accordingly. It also ensures that previous nested messages have already been processed by their respective code handlers.
	 */
	public void generateSourceCode(UMLSequenceDiagram umlSD, UMLSymbol symbol, LinkedList<CompilationUnit> compUnits) throws UMLSDStructureException {
		if (symbol instanceof UMLSynchronousMessage) {
			UMLSynchronousMessage syncMessage = (UMLSynchronousMessage) symbol;
			
			UMLClass invoker = syncMessage.getInvoker();
			UMLClass receiver = syncMessage.getReceiver();
			UMLMessage invokingMessage = syncMessage.getInvokingMessage(umlSD);	// getting invoking message to determine where to place invocation
			ArrayList<UMLMessage> nestedMessages = new ArrayList<UMLMessage>();	
			if (invokingMessage != null) {
				nestedMessages.addAll(invokingMessage.getNestedMessages());	// getting nested messages to determine order of invocations
			}
			
			// getting this message's order in it's logic block, and generating source code for all predecessor messages in list of nested messages
			int syncMessageOrder = UMLMessageHelperMethods.getMessageOrderAndGeneratePredecessorCode(syncMessage, nestedMessages, umlSD, compUnits);
			
			// getting a list of this message's containers ordered from its outermost container to its innermost container
			ArrayList<UMLContainer> containers = UMLMessageHelperMethods.getListOfContainersOutermostToInnermost(syncMessage);
			
			// create invocation
			ExpressionStmt invocation = createInvocation(syncMessage, umlSD);
			
			// ensure that signature exists in compilation units, if not create it
			createSignature(syncMessage, receiver, compUnits);
			
			if (invocation != null) {
				// ensure that class declaration for invocation exists, if not create it
				Optional<ClassOrInterfaceDeclaration> classDecForInvocation = UMLMessageHelperMethods.getClassDeclarationForInvocation(invoker, compUnits);
				
				if (invokingMessage instanceof UMLSynchronousMessage) {	// invocation lies in a method signature
					// ensure that method with matching parameter list exists, if not create it
					Optional<MethodDeclaration> methodWithMatchingParameterList = UMLMessageHelperMethods.getMethodWithMatchingParametersFromSyncMessage((UMLSynchronousMessage)invokingMessage, classDecForInvocation);
					
					MethodDeclaration methodWMPDec = methodWithMatchingParameterList.get();
					
					Optional<BlockStmt> methodBody = methodWMPDec.getBody();
					
					if (!methodBody.isPresent()) {
						methodBody = Optional.of(methodWMPDec.createBody());
					}
					
					// place invocation in correct logic block in body of method with matching parameter list
					UMLMessageHelperMethods.placeInvocationInBody(methodBody, containers, syncMessage, syncMessageOrder, invocation);
				} else if (invokingMessage instanceof UMLCreateMessage) {	// invocation lies in a constructor
					// ensure that constructor with matching parameter list exists, if not create it
					Optional<ConstructorDeclaration> constructorWithCorrectParam = UMLMessageHelperMethods.getConstructorWithMatchingParametersFromCreateMessage((UMLCreateMessage)invokingMessage, classDecForInvocation);
					
					Optional<BlockStmt> constructorBody = Optional.of(constructorWithCorrectParam.get().getBody());
					
					if (!constructorBody.isPresent()) {
						constructorBody = Optional.of(constructorWithCorrectParam.get().createBody());
					}
					
					// place invocation in correct logic block in body of method with matching parameter list
					UMLMessageHelperMethods.placeInvocationInBody(constructorBody, containers, syncMessage, syncMessageOrder, invocation);
				} else {	// no invoking message found, so invoking class will be treated as the client with this message being invoked in its main method
					// ensure that main method exists, if not create it
					if (classDecForInvocation != null) {
						Optional<MethodDeclaration> mainMethod = UMLMessageHelperMethods.getMainMethod(classDecForInvocation);
						
						Optional<BlockStmt> mainMethodBody = mainMethod.get().getBody();
						
						if (!mainMethodBody.isPresent()) {
							mainMethodBody = Optional.of(mainMethod.get().createBody());
						}
						
						// place invocation in correct logic block in body of main method 
						UMLMessageHelperMethods.placeInvocationInBody(mainMethodBody, containers, syncMessage, syncMessageOrder, invocation);
					}
				}
			}
			
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * The createInvocation method creates an expression statement of the constructor invocation to be placed in the AST representation.
	 * 
	 * @param createMessage
	 * @param receiver
	 * @return
	 */
	public ExpressionStmt createInvocation(UMLSynchronousMessage syncMessage, UMLSequenceDiagram umlSD) throws UMLSDStructureException {
		if (syncMessage.getInvoker() != null) {
			NodeList<Expression> listOfArgumentInits = new NodeList<Expression>();	// get list of arguments passed to constructor and create expressions
			for (UMLMessageArgument argument : syncMessage.getArguments()) {
				NameExpr argumentExpr = new NameExpr(argument.getInitializedTo());
				listOfArgumentInits.add(argumentExpr);
			}
			
			ExpressionStmt invocation = null;
			
			if (syncMessage.getReturnType().equals("void")) {
				NameExpr objectToCall = new NameExpr(new SimpleName(syncMessage.getReceiver().getInstanceName()));	// name of object to call method on
				MethodCallExpr methodCall = new MethodCallExpr(objectToCall, new SimpleName(syncMessage.getName()), listOfArgumentInits);	// create method call
				
				invocation = new ExpressionStmt(methodCall);	// wrap to expression statement
			} else {
				ClassOrInterfaceType type = JavaParser.parseClassOrInterfaceType(syncMessage.getReturnType());	// get JavaParser type and create expressions
				NameExpr objectToCall = new NameExpr(new SimpleName(syncMessage.getReceiver().getInstanceName()));	// name of object to call method on
				MethodCallExpr methodCall = new MethodCallExpr(objectToCall, new SimpleName(syncMessage.getName()), listOfArgumentInits);	// create method call
				VariableDeclarationExpr refName = new VariableDeclarationExpr(type, syncMessage.getReturnMessage(umlSD).getReturnName());
				
				AssignExpr assignCallToRef = new AssignExpr(refName, methodCall, Operator.ASSIGN);
				
				invocation = new ExpressionStmt(assignCallToRef);	// wrap to expression statement
			}
			
			return invocation;
		} else {
			return null;
		}
	}
	
	/**
	 * The createSignature method creates a method signature and also places it in the correct location in the AST representation.
	 * 
	 * @param createMessage
	 * @param receiver
	 * @param compUnits
	 * @return
	 */
	public Optional<ClassOrInterfaceDeclaration> createSignature(UMLSynchronousMessage syncMessage, UMLClass receiver, LinkedList<CompilationUnit> compUnits) {
		Optional<ClassOrInterfaceDeclaration> classDecForSignature = Optional.empty();	
		
		for (CompilationUnit compUnit : compUnits) {	// get receiver class in compilation units
			if (compUnit.getClassByName(receiver.getClassName()).isPresent())
			{
				classDecForSignature = compUnit.getClassByName(receiver.getClassName());
			}
		}
		
		if (!classDecForSignature.isPresent()) {	// if receiver class isn't already in compilation units, create it
			CompilationUnit compilationUnit = new CompilationUnit();
			classDecForSignature = Optional.of(compilationUnit.addClass(receiver.getClassName()).setPublic(true));
			compUnits.add(compilationUnit);
		}
		
		ArrayList<String> argumentClasses = new ArrayList<String>();	// get a list of all parameter types
		for (int numArgument = 0; numArgument < syncMessage.getArguments().size(); numArgument++) {
			String arg = syncMessage.getArguments().get(numArgument).getDataType();
			
			argumentClasses.add(arg);
		}
		
		String[] argumentClassesArray = new String[argumentClasses.size()];
		Optional<MethodDeclaration> signature = Optional.empty();	// check if method signature with these parameter types exists
		if (!classDecForSignature.get().getMethodsByParameterTypes( argumentClasses.toArray(argumentClassesArray) ).isEmpty()) {
			List<MethodDeclaration> methods = classDecForSignature.get().getMethodsByParameterTypes( argumentClasses.toArray(argumentClassesArray) );
			
			for (MethodDeclaration method : methods) {
				if (method.getNameAsString().equals(syncMessage.getName())) {	// if method with same name exists in the list of methods with this # of parameters
					signature = Optional.of(method);	// this method is the one we are looking for
				}
			}
		}
		
		if (!signature.isPresent()) {	// if signature with these parameter types doesn't exist, create one
			MethodDeclaration methodDec = classDecForSignature.get().addMethod(syncMessage.getName());
			
			for (int numArgument = 0; numArgument < syncMessage.getArguments().size(); numArgument++) {
				UMLMessageArgument argument = syncMessage.getArguments().get(numArgument);
				String argDataType = argument.getDataType();
				String argName = argument.getName();
				
				if (argument.isHasVarArgs()) {	// if argument is varargs, the parameter added to the constructor must be varargs
					Parameter varArgsParam = new Parameter(JavaParserHelperMethod.simpleStringToType(argDataType), argName);
					varArgsParam.setVarArgs(true);
					methodDec.addParameter(varArgsParam);
				} else {
					methodDec.addParameter(argDataType, argName);
				}
			}
			
			methodDec.setType(syncMessage.getReturnType());
			methodDec.setPublic(true);
		}
		
		return classDecForSignature;
	}
	
}
