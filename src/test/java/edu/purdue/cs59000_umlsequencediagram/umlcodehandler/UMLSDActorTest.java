package edu.purdue.cs59000_umlsequencediagram.umlcodehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLActor;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.UMLCreateMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.umlcodehandler.SequenceDiagramCodeController;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDClassHandler;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDCreateMessageHandler;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLElse;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLLoop;

public class UMLSDActorTest {

	@Test
	public void test() throws UMLSDStructureException {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLActor actor = new UMLActor("user");	// creating actor
		umlSD.addSymbol(actor);
		UMLLifeline actorLifeline = new UMLLifeline(actor, umlSD);
		UMLActivationBox actorActBox = new UMLActivationBox(actorLifeline, umlSD);
		
		UMLClass class1Class = new UMLClass("Class1", "class1", umlSD);
		UMLLifeline class1Lifeline = new UMLLifeline(class1Class, umlSD);
		UMLActivationBox class1ActBox = new UMLActivationBox(class1Lifeline, umlSD);
		
		UMLCondition createClass2Con = new UMLCondition("someObject.getSomeOtherObject().getSomeBoolValue()", umlSD);
		UMLLoop createClass2Loop = new UMLLoop(createClass2Con, umlSD);
		createClass2Con.setParentContainer(createClass2Loop);
		
		ArrayList<UMLMessageArgument> class1CreateArgs = new ArrayList<UMLMessageArgument>();
		class1CreateArgs.add(new UMLMessageArgument("argument1", "ArrayList<String>", "new ArrayList<String>()", false));
		UMLCreateMessage class1CreateMessage = new UMLCreateMessage("Class1", class1Class, class1CreateArgs, actorActBox, umlSD);
		
		UMLClass class2Class = new UMLClass("Class2", "class2", umlSD);
		UMLLifeline class2Lifeline = new UMLLifeline(class2Class, umlSD);
		UMLActivationBox class2ActivationBox = new UMLActivationBox(class2Lifeline, umlSD);
		
		ArrayList<UMLMessageArgument> class2CreateArgs = new ArrayList<UMLMessageArgument>();
		class2CreateArgs.add(new UMLMessageArgument("stringArgs", "String", "'some string', 'some other string', 'some other string'", true));
		UMLCreateMessage class2CreateMessage = new UMLCreateMessage("Class2", class2Class, class2CreateArgs, class1ActBox, umlSD);
		class2CreateMessage.setParentContainer(createClass2Con);
		class1CreateMessage.addNestedMessage(class2CreateMessage);
		
		UMLCreateMessage.setCodeHandler(new UMLSDCreateMessageHandler());
		UMLClass.setCodeHandler(new UMLSDClassHandler());
		
		LinkedList<CompilationUnit> compUnits = new LinkedList<CompilationUnit>();
		try {
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		} catch (UMLSDStructureException e) {
			System.out.println(e.getMessage());
		}
		
		ArrayList<String> linesOfCode = new ArrayList<String>();
		for (CompilationUnit compUnit : compUnits) {
			String[] codeUnit = compUnit.toString().split("\r\n");
			for(String lineOfCode:codeUnit) {
				linesOfCode.add(lineOfCode);
			}
			System.out.println(compUnit);
		}
		
		assertEquals(linesOfCode.get(0), "public class Class1 {");	// ensure actor doesn't get generated in code
		assertEquals(linesOfCode.get(1), "");
	}

}
