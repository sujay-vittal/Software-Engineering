package edu.purdue.cs59000.umltranslator.umlcodehandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;

import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.javaparser.JavaParserHelperMethod;
import edu.purdue.cs59000.umltranslator.message.UMLCreateMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLContainer;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLLoop;

/**
 * The UMLMessageHelperMethods class contains many helper methods that can be shared across several UMLMessage code handlers. This helps to improve readability
 * and simplify making necessary changes.
 * 
 * @author Steve Yarusinsky
 *
 */

public final class UMLMessageHelperMethods {
	
	/**
	 * The getMessageOrderAndGeneratePredecessorCode method determines the message's order in the invokingMethod's list of nested messages, and ensures that
	 * all messages that come before it have been processed by their respective code handlers. This is necessary for ordering invocations correctly.
	 * 
	 * @param message
	 * @param invokingMethodNestedMessages
	 * @param umlSD
	 * @param compUnits
	 * @return
	 * @throws UMLSDStructureException
	 */
	public static int getMessageOrderAndGeneratePredecessorCode(UMLMessage message, ArrayList<UMLMessage> invokingMethodNestedMessages, UMLSequenceDiagram umlSD, LinkedList<CompilationUnit> compUnits) throws UMLSDStructureException {
		int messageOrder = 0;	
		for (UMLMessage nestedMessage : invokingMethodNestedMessages) {
			if (nestedMessage == message) {	// order found and necessary code generated, so break loop
				break;
			}
			
			if (message.getParentContainer() == nestedMessage.getParentContainer()) {	// getting the order of the create message in its particular logic block
				messageOrder++;
			}
			
			if (nestedMessage != message && !nestedMessage.isCodeGenFlagged()) {	// ensuring any messages' code before this message has already been generated
				nestedMessage.getCodeHandler().generateSourceCode(umlSD, nestedMessage, compUnits);
			}
		}
		
		return messageOrder;
	}
	
	/**
	 * The getListOfContainersOutmostToInnermost method is a simple helper method which returns a list of UMLContainers ordered from outermost container to
	 * innermost container. For example if a message is contained in a while loop in the 'if' portion of an if-else statement, the returned list would be ordered,
	 * UMLLoop, UMLCondition, UMLAlternatives, UMLCondition.
	 * 
	 * @param message
	 * @return
	 */
	public static ArrayList<UMLContainer> getListOfContainersOutermostToInnermost(UMLMessage message) {
		ArrayList<UMLContainer> containers = new ArrayList<UMLContainer>();	// getting a list of all containers for use in ast parsing/generation later
		UMLContainer container = message.getParentContainer();
		while (container != null) {
			containers.add(container);
			container = container.getParentContainer();
		}
		Collections.reverse(containers);	// reversing order so that outermost container is first
		
		return containers;
	}
	
	/**
	 * The getClassDeclarationForInvocation method is a simple helper method to get a specific class from a list of compilation units. If the class already
	 * exists in the list of compilation units, it is simply returned. If the class does not exist in the list, the class is added to the list and then returned.
	 * If the invoker is null, this means the invoker is an Actor, and therefore has no corresponding class, so returns null.
	 * 
	 * @param invoker
	 * @param compUnits
	 * @return
	 */
	public static Optional<ClassOrInterfaceDeclaration> getClassDeclarationForInvocation(UMLClass invoker, LinkedList<CompilationUnit> compUnits) {
		if (invoker != null) {
			Optional<ClassOrInterfaceDeclaration> classDecForInvocation = Optional.empty();	
			
			for (CompilationUnit compUnit : compUnits) {	// get invocation class in compilation units
				if (compUnit.getClassByName(invoker.getClassName()).isPresent())
				{
					classDecForInvocation = compUnit.getClassByName(invoker.getClassName());
				}
			}
			
			if (!classDecForInvocation.isPresent()) {	// if receiver class isn't already in compilation units, create it
				CompilationUnit compilationUnit = new CompilationUnit();
				classDecForInvocation = Optional.of(compilationUnit.addClass(invoker.getClassName()).setPublic(true));
				compUnits.add(compilationUnit);
			}
			
			return classDecForInvocation;
		} else {
			return null;
		}
	}
	
	/**
	 * The getMethodWithMatchingParametersFromSyncMessage method finds a method that matches both the name and parameter list of the
	 * invoking message. If it already exists in the list of compilation units, it returns the method declaration. If it does not already exist in the list
	 * of compilation units, it creates the method declaration and adds it to the correct class.
	 * 
	 * @param invokingMessage
	 * @param classDecForInvocation
	 * @return
	 */
	public static Optional<MethodDeclaration> getMethodWithMatchingParametersFromSyncMessage(UMLSynchronousMessage invokingMessage, Optional<ClassOrInterfaceDeclaration> classDecForInvocation) {
		UMLSynchronousMessage invokingSyncMessage = invokingMessage;
		ArrayList<UMLMessageArgument> invokingSyncMessageParams = new ArrayList<UMLMessageArgument>();
		invokingSyncMessageParams.addAll(invokingSyncMessage.getArguments());
		String invokingMessageName = invokingSyncMessage.getName();
		
		ArrayList<MethodDeclaration> methodsWithInvokingMessageName = new ArrayList<MethodDeclaration>();	// get all method signatures with this name
		methodsWithInvokingMessageName.addAll(classDecForInvocation.get().getMethodsByName(invokingMessageName));
		Optional<MethodDeclaration> methodWithMatchingParameterList = Optional.empty();
		
		NodeList<Parameter> invokingSyncMessageASTParams = new NodeList<Parameter>();	// create an ast representation of parameters
		for (UMLMessageArgument invokingSMParam : invokingSyncMessageParams) {
			String paramDataType = invokingSMParam.getDataType();
			String paramName = invokingSMParam.getName();
			
			Parameter param = new Parameter(JavaParserHelperMethod.simpleStringToType(paramDataType), paramName);
			
			if (invokingSMParam.isHasVarArgs()) {
				param.setVarArgs(true);
			}
			
			invokingSyncMessageASTParams.add(param);
		}
		
		for (MethodDeclaration methodDec : methodsWithInvokingMessageName) {	// check each method signature parameter lists to see if they match
			NodeList<Parameter> methodParams = methodDec.getParameters();
			boolean isEqualTo = true;
			
			for (int param = 0; param < methodParams.size(); param++) {
				Parameter syncParam = methodParams.get(param);
				Parameter messageParam = invokingSyncMessageASTParams.get(param);
				if (syncParam.getName().equals(messageParam.getName()) && syncParam.getTypeAsString().equals(messageParam.getTypeAsString())) {
					isEqualTo = true;
				} else {
					isEqualTo = false;
				}
			}
			
			if (isEqualTo) {
				methodWithMatchingParameterList = Optional.of(methodDec);
			}
		}
		
		if (!methodWithMatchingParameterList.isPresent()) {	// if no matching method is found in the class, create method signature
			MethodDeclaration methodWMPDec = classDecForInvocation.get().addMethod(invokingMessageName, Modifier.PUBLIC);
			
			for (UMLMessageArgument param : invokingSyncMessageParams) {	// add all parameters to method signature
				methodWMPDec.addParameter(param.getDataType(), param.getName());
			}
			
			methodWithMatchingParameterList = Optional.of(methodWMPDec);
		}
		
		return methodWithMatchingParameterList;
	}
	
	/**
	 * The getConstructorWithMatchingParametersFromCreateMessage method finds a constructor that matches both the name and parameter list of the
	 * invoking message. If it already exists in the list of compilation units, it returns the constructor declaration. If it does not already exist in the list
	 * of compilation units, it creates the constructor declaration and adds it to the correct class.
	 * 
	 * @param invokingMessage
	 * @param classDecForInvocation
	 * @return
	 */
	public static Optional<ConstructorDeclaration> getConstructorWithMatchingParametersFromCreateMessage(UMLCreateMessage invokingMessage, Optional<ClassOrInterfaceDeclaration> classDecForInvocation) {
		UMLCreateMessage invokingCreateMessage = invokingMessage;
		ArrayList<UMLMessageArgument> invokingCreateMessageParams = new ArrayList<UMLMessageArgument>();
		invokingCreateMessageParams.addAll(invokingCreateMessage.getArguments());
		
		ArrayList<ConstructorDeclaration> constructorsList = new ArrayList<ConstructorDeclaration>();	// get all constructor signatures
		constructorsList.addAll(classDecForInvocation.get().getConstructors());
		Optional<ConstructorDeclaration> constructorWithCorrectParam = Optional.empty();
		
		NodeList<Parameter> invokingCreateMessageASTParams = new NodeList<Parameter>();	// create an ast representation of parameters
		for (UMLMessageArgument invokingCMParam : invokingCreateMessageParams) {
			String paramDataType = invokingCMParam.getDataType();
			String paramName = invokingCMParam.getName();
			
			Parameter param = new Parameter(JavaParserHelperMethod.simpleStringToType(paramDataType), paramName);
			
			if (invokingCMParam.isHasVarArgs()) {
				param.setVarArgs(true);
			}
			
			invokingCreateMessageASTParams.add(param);
		}
		
		for (ConstructorDeclaration constructorDec : constructorsList) {	// check each constructors parameter lists to see if they match
			NodeList<Parameter> constructorParams = constructorDec.getParameters();
			boolean isEqualTo = false;
			
			for (int param = 0; param < constructorParams.size(); param++) {
				Parameter constructorParam = constructorParams.get(param);
				Parameter messageParam = invokingCreateMessageASTParams.get(param);
				if (constructorParam.getName().equals(messageParam.getName()) && constructorParam.getTypeAsString().equals(messageParam.getTypeAsString())) {
					isEqualTo = true;
				} else {
					isEqualTo = false;
				}
				
				if (isEqualTo) {
					constructorWithCorrectParam = Optional.of(constructorDec);
				}
			}
		}
		
		if (!constructorWithCorrectParam.isPresent()) {	// if no constructor signature with correct param list is found, create one
			constructorWithCorrectParam = Optional.of(classDecForInvocation.get().addConstructor(Modifier.PUBLIC));
			
			for (Parameter param : invokingCreateMessageASTParams) {
				constructorWithCorrectParam.get().addParameter(param);
			}
		}
		
		return constructorWithCorrectParam;
	}
	
	/**
	 * The getMainMethod method finds the main method in a class declaration. If it does not already exist, it creates a main method within it.
	 * 
	 * @param classDecForInvocation
	 * @return
	 * @throws UMLSDStructureException
	 */
	public static Optional<MethodDeclaration> getMainMethod(Optional<ClassOrInterfaceDeclaration> classDecForInvocation) throws UMLSDStructureException {
		Optional<MethodDeclaration> mainMethodOpt = Optional.empty();
		
		ArrayList<MethodDeclaration> mainMethods = new ArrayList<MethodDeclaration>();
		mainMethods.addAll(classDecForInvocation.get().getMethodsByName("main"));
		
		if (mainMethods.isEmpty()) {	// no main method exists, so create one
			MethodDeclaration mainMethod = classDecForInvocation.get().addMethod("main", Modifier.PUBLIC, Modifier.STATIC);
			mainMethod.setType("void");
			
			Parameter stringArgsParam = new Parameter(JavaParserHelperMethod.simpleStringToType("String[]"), new SimpleName("args"));
			
			mainMethod.addParameter(stringArgsParam);
			
			mainMethodOpt = Optional.of(mainMethod);
		} else if (mainMethods.size() > 1) {	// if there is more than one main method something is wrong 
			throw new UMLSDStructureException("Code Generation: There is more than one main method in the client");
		} else {
			MethodDeclaration mainMethod = mainMethods.get(0);
			
			mainMethodOpt = Optional.of(mainMethod);
		}
		
		return mainMethodOpt;
	}
	
	/**
	 * The placeInvocationInBody method takes a block statement in which to place the invocation, a reversed list of containers, the message, the order of the
	 * message, and the invocation expression, to correctly place the invocation in the AST representation of the code.  
	 * 
	 * @param body
	 * @param containers this list should be reversed using the getListOfContainersOutermostToInnermost method
	 * @param message
	 * @param messageOrder
	 * @param invocation
	 */
	public static void placeInvocationInBody(Optional<BlockStmt> body, ArrayList<UMLContainer> containers, UMLMessage message, int messageOrder, ExpressionStmt invocation) {
		// find the correct logic statement node in the the ast to place the invocation
		BlockStmt lastBlock = body.get();
		NodeList<Statement> stmts = lastBlock.getStatements();
		
		for (UMLContainer cntr	: containers) {	// for each container, create the logic statement and determine if it already lies in method body
			if (cntr instanceof UMLAlternatives) {
				UMLAlternatives alt = (UMLAlternatives) cntr;
				Expression altCon = JavaParserHelperMethod.parseExpression(alt.getCondition().getCondition());
				IfStmt altIfStmt = new IfStmt(altCon, new BlockStmt(), new BlockStmt());
				boolean statementFound = false;
				
				for (Statement stmt : stmts) {	// try to find IfStmt with matching condition in list of statements
					if (stmt instanceof IfStmt) {
						IfStmt ifStmt = (IfStmt) stmt;
						
						if (ifStmt.getCondition().toString().equals(altCon.toString())) {
							if (message.getParentContainer() instanceof UMLCondition) {
								lastBlock = ifStmt.getThenStmt().toBlockStmt().get();
							} else {
								lastBlock = ifStmt.getElseStmt().get().toBlockStmt().get();
							}
							stmts = lastBlock.getStatements();	// set stmts to list of statements inside if statement for next container
							statementFound = true;
							break;
						}
					}
				}
				
				if (!statementFound) {	// if statement isn't found in correct block, insert it
					if (message.getParentContainer() instanceof UMLCondition) {
						lastBlock = lastBlock.addAndGetStatement(altIfStmt).getThenStmt().toBlockStmt().get();
					} else {
						lastBlock = lastBlock.addAndGetStatement(altIfStmt).getElseStmt().get().toBlockStmt().get();
					}
				}
			} else if (cntr instanceof UMLLoop) {
				UMLLoop loop = (UMLLoop) cntr;
				Expression loopCon = JavaParserHelperMethod.parseExpression(loop.getCondition().getCondition());
				WhileStmt loopWhileStmt = new WhileStmt(loopCon, new BlockStmt());
				boolean statementFound = false;
				
				for (Statement stmt : stmts) {	// try to find WhileStmt with matching condition in list of statements
					if (stmt instanceof WhileStmt) {
						WhileStmt whileStmt = (WhileStmt) stmt;
						
						if (whileStmt.getCondition().toString().equals(loopCon.toString())) {
							lastBlock = whileStmt.getBody().toBlockStmt().get();
							stmts = lastBlock.getStatements();
							statementFound = true;
							break;
						}
					}
				}
				
				if (!statementFound) {	// if statement isn't found in correct block, insert it
					lastBlock = lastBlock.addAndGetStatement(loopWhileStmt).createBlockStatementAsBody();
				}
			}
		}
		
		// check if this invocation already exists in body, if not create it
		if (lastBlock.getStatements().size() > messageOrder && lastBlock.getStatement(messageOrder).toString().equals(invocation.toString())) {
			return;
		} else {
			lastBlock.addStatement(messageOrder, invocation);
		}
	}
	
}
