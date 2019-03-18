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
 * The UMLSDCreateMessageHandler class defines the source code generation of a UMLCreateMessage object. It correctly identifies the location of both the method 
 * signature and invocation, and places them in the AST representation accordingly.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLSDCreateMessageHandler extends UMLCodeHandler {
	public UMLSDCreateMessageHandler() {}
	
	/**
	 * The generateSourceCode method correctly identifies the location of both the method signature and invocation, and places them in the AST representation 
	 * accordingly. It also ensures that previous nested messages have already been processed by their respective code handlers.
	 */
	public void generateSourceCode(UMLSequenceDiagram umlSD, UMLSymbol symbol, LinkedList<CompilationUnit> compUnits) throws UMLSDStructureException {
		if (symbol instanceof UMLCreateMessage) {
			UMLCreateMessage createMessage = (UMLCreateMessage) symbol;
			
			UMLClass invoker = createMessage.getInvoker();
			UMLClass receiver = createMessage.getReceiver();
			UMLMessage invokingMessage = createMessage.getInvokingMessage(umlSD);	// getting invoking message to determine where to place invocation
			ArrayList<UMLMessage> nestedMessages = new ArrayList<UMLMessage>();	
			if (invokingMessage != null) {
				nestedMessages.addAll(invokingMessage.getNestedMessages());	// getting nested messages to determine order of invocations
			}
			
			// getting this message's order in it's logic block, and generating source code for all predecessor messages in list of nested messages
			int createMessageOrder = UMLMessageHelperMethods.getMessageOrderAndGeneratePredecessorCode(createMessage, nestedMessages, umlSD, compUnits);
			
			// getting a list of this message's containers ordered from its outermost container to its innermost container
			ArrayList<UMLContainer> containers = UMLMessageHelperMethods.getListOfContainersOutermostToInnermost(createMessage);
			
			// create invocation
			ExpressionStmt invocation = createInvocation(createMessage, receiver);
			
			// ensure that signature exists in compilation units, if not create it
			createSignature(createMessage, receiver, compUnits);
			
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
					UMLMessageHelperMethods.placeInvocationInBody(methodBody, containers, createMessage, createMessageOrder, invocation);
				} else if (invokingMessage instanceof UMLCreateMessage) {	// invocation lies in a constructor
					// ensure that constructor with matching parameter list exists, if not create it
					Optional<ConstructorDeclaration> constructorWithCorrectParam = UMLMessageHelperMethods.getConstructorWithMatchingParametersFromCreateMessage((UMLCreateMessage)invokingMessage, classDecForInvocation);
					
					Optional<BlockStmt> constructorBody = Optional.of(constructorWithCorrectParam.get().getBody());
					
					if (!constructorBody.isPresent()) {
						constructorBody = Optional.of(constructorWithCorrectParam.get().createBody());
					}
					
					// place invocation in correct logic block in body of method with matching parameter list
					UMLMessageHelperMethods.placeInvocationInBody(constructorBody, containers, createMessage, createMessageOrder, invocation);
				} else {	// no invoking message found, so invoking class will be treated as the client with this message being invoked in its main method
					// ensure that main method exists, if not create it
					if (classDecForInvocation != null) {
						Optional<MethodDeclaration> mainMethod = UMLMessageHelperMethods.getMainMethod(classDecForInvocation);
						
						Optional<BlockStmt> mainMethodBody = mainMethod.get().getBody();
						
						if (!mainMethodBody.isPresent()) {
							mainMethodBody = Optional.of(mainMethod.get().createBody());
						}
						
						// place invocation in correct logic block in body of main method 
						UMLMessageHelperMethods.placeInvocationInBody(mainMethodBody, containers, createMessage, createMessageOrder, invocation);
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
	public ExpressionStmt createInvocation(UMLCreateMessage createMessage, UMLClass receiver) throws UMLSDStructureException {
		if (createMessage.getInvoker() != null) {
			NodeList<Expression> listOfArgumentInits = new NodeList<Expression>();	// get list of arguments passed to constructor and create expressions
			for (UMLMessageArgument argument : createMessage.getArguments()) {
				NameExpr argumentExpr = new NameExpr(argument.getInitializedTo());
				listOfArgumentInits.add(argumentExpr);
			}
			
			ClassOrInterfaceType type = JavaParser.parseClassOrInterfaceType(receiver.getClassName());	// get JavaParser type and create expressions
			ObjectCreationExpr constructorCall = new ObjectCreationExpr(null, type, listOfArgumentInits);
			VariableDeclarationExpr refName = new VariableDeclarationExpr(type, receiver.getInstanceName());
			
			AssignExpr assignCallToRef = new AssignExpr(refName, constructorCall, Operator.ASSIGN);
			
			ExpressionStmt invocation = new ExpressionStmt(assignCallToRef);	// wrap to expression statement
			
			return invocation;
		} else {
			return null;
		}
	}
	
	/**
	 * The createSignature method creates a constructor signature and also places it in the correct location in the AST representation.
	 * 
	 * @param createMessage
	 * @param receiver
	 * @param compUnits
	 * @return
	 */
	public Optional<ClassOrInterfaceDeclaration> createSignature(UMLCreateMessage createMessage, UMLClass receiver, LinkedList<CompilationUnit> compUnits) {
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
		for (int numArgument = 0; numArgument < createMessage.getArguments().size(); numArgument++) {
			String arg = createMessage.getArguments().get(numArgument).getDataType();
			
			argumentClasses.add(arg);
		}
		
		String[] argumentClassesArray = new String[argumentClasses.size()];
		Optional<ConstructorDeclaration> signature = Optional.empty();	// check if constructor signature with these parameter types exists
		signature = classDecForSignature.get().getConstructorByParameterTypes( argumentClasses.toArray(argumentClassesArray) );
		
		if (!signature.isPresent()) {	// if signature with these parameter types doesn't exist, create one
			ConstructorDeclaration constructorDec = classDecForSignature.get().addConstructor();
			
			for (int numArgument = 0; numArgument < createMessage.getArguments().size(); numArgument++) {
				UMLMessageArgument argument = createMessage.getArguments().get(numArgument);
				String argDataType = argument.getDataType();
				String argName = argument.getName();
				
				if (argument.isHasVarArgs()) {	// if argument is varargs, the parameter added to the constructor must be varargs
					Parameter varArgsParam = new Parameter(JavaParserHelperMethod.simpleStringToType(argDataType), argName);
					varArgsParam.setVarArgs(true);
					constructorDec.addParameter(varArgsParam);
				} else {
					constructorDec.addParameter(argDataType, argName);
				}
			}
			
			constructorDec.setPublic(true);
		}
		
		return classDecForSignature;
	}
	
}
