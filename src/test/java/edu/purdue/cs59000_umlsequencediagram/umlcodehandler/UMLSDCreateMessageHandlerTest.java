package edu.purdue.cs59000_umlsequencediagram.umlcodehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.*;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLElse;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLLoop;

public class UMLSDCreateMessageHandlerTest {

	@Test
	public void test() throws Exception {

		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLClass clientClass = new UMLClass("Client", "client", umlSD);	// creating client class
		UMLLifeline clientLifeline = new UMLLifeline(clientClass, umlSD);
		UMLActivationBox clientActBox = new UMLActivationBox(clientLifeline, umlSD);
		
		UMLClass class1Class = new UMLClass("Class1", "class1", umlSD);
		UMLLifeline class1Lifeline = new UMLLifeline(class1Class, umlSD);
		UMLActivationBox class1ActBox = new UMLActivationBox(class1Lifeline, umlSD);
		
		UMLCondition createClass2Con = new UMLCondition("x > 5", umlSD);
		UMLElse createClass2Else = new UMLElse(umlSD);
		UMLAlternatives createClass2Alt = new UMLAlternatives(createClass2Con, createClass2Else, umlSD);
		
		UMLCondition createClass3Con = new UMLCondition("y > 4", umlSD);
		UMLElse createClass3Else = new UMLElse(umlSD);
		UMLAlternatives createClass3Alt = new UMLAlternatives(createClass3Con, createClass3Else, umlSD);
		
		UMLCondition class1ConstructorCon = new UMLCondition("z > 3", umlSD);
		UMLLoop class1ConstructorLoop = new UMLLoop(class1ConstructorCon, umlSD);
		
		createClass2Alt.setParentContainer(class1ConstructorCon);
		createClass3Alt.setParentContainer(class1ConstructorCon);
		
		ArrayList<UMLMessageArgument> class1CreateArgs = new ArrayList<UMLMessageArgument>();
		class1CreateArgs.add(new UMLMessageArgument("argument1", "String", "'a string value'", false));
		UMLCreateMessage class1CreateMessage = new UMLCreateMessage("Class1", class1Class, class1CreateArgs, clientActBox, umlSD);
		
		UMLClass class2Class = new UMLClass("Class2", "class2", umlSD);
		UMLLifeline class2Lifeline = new UMLLifeline(class2Class, umlSD);
		UMLActivationBox class2ActivationBox = new UMLActivationBox(class2Lifeline, umlSD);
		
		ArrayList<UMLMessageArgument> class2CreateArgs = new ArrayList<UMLMessageArgument>();
		class2CreateArgs.add(new UMLMessageArgument("intArgs", "int", "10, 15, 20", true));
		UMLCreateMessage class2CreateMessage = new UMLCreateMessage("Class2", class2Class, class2CreateArgs, class1ActBox, umlSD);
		class2CreateMessage.setParentContainer(createClass2Con);
		class1CreateMessage.addNestedMessage(class2CreateMessage);
		
		UMLClass class3Class = new UMLClass("Class3", "class3", umlSD);
		UMLLifeline class3Lifeline = new UMLLifeline(class3Class, umlSD);
		UMLActivationBox class3ActBox = new UMLActivationBox(class3Lifeline, umlSD);
		
		ArrayList<UMLMessageArgument> class3CreateArgs = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage class3CreateMessage = new UMLCreateMessage("Class3", class3Class, class3CreateArgs, class1ActBox, umlSD);
		class3CreateMessage.setParentContainer(createClass3Con);
		
		UMLClass class5Class = new UMLClass("Class5", "class5", umlSD);
		UMLLifeline class5Lifeline = new UMLLifeline(class5Class, umlSD);
		UMLActivationBox class5ActBox = new UMLActivationBox(class5Lifeline, umlSD);
		
		ArrayList<UMLMessageArgument> class5CreateArgs = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage class5CreateMessage = new UMLCreateMessage("Class5", class5Class, class5CreateArgs, class1ActBox, umlSD);
		class5CreateMessage.setParentContainer(createClass3Else);
		
		UMLClass class4Class = new UMLClass("Class4", "class4", umlSD);
		UMLLifeline class4Lifeline = new UMLLifeline(class4Class, umlSD);
		UMLActivationBox class4ActBox = new UMLActivationBox(class4Lifeline, umlSD);
		
		ArrayList<UMLMessageArgument> class4CreateArgs = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage class4CreateMessage = new UMLCreateMessage("Class4", class4Class, class4CreateArgs, class1ActBox, umlSD);
		class4CreateMessage.setParentContainer(createClass3Con);
		class1CreateMessage.addNestedMessage(class4CreateMessage);
		class1CreateMessage.addNestedMessage(class3CreateMessage);
		class1CreateMessage.addNestedMessage(class5CreateMessage);
		
		UMLCreateMessage.setCodeHandler(new UMLSDCreateMessageHandler());
		UMLClass.setCodeHandler(new UMLSDClassHandler());
		
		LinkedList<CompilationUnit> compUnits = new LinkedList<CompilationUnit>();
		try {
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		} catch (UMLSDStructureException e) {
			System.out.println(e.getMessage());
		}
		
		for (CompilationUnit compUnit : compUnits) {
			System.out.println(compUnit);
		}
	}

}
