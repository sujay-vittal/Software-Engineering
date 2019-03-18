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

public class UMLSDNestedLoopTest {

	@Test
	public void test() throws Exception {
		
		int N = (int)(Math.random()*10.0);
		
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();

		UMLClass clientClass = new UMLClass("Client", "client", umlSD);
		UMLLifeline clientLifeline = new UMLLifeline(clientClass, umlSD);
		UMLActivationBox clientActBox = new UMLActivationBox(clientLifeline, umlSD);
		
		UMLClass class1Class = new UMLClass("Class1", "class1", umlSD);
		UMLLifeline class1Lifeline = new UMLLifeline(class1Class, umlSD);
		UMLActivationBox class1ActBox = new UMLActivationBox(class1Lifeline, umlSD);
		
		UMLLoop[] umlLoop = new UMLLoop[N];
		UMLCondition[] loopCondition = new UMLCondition[N];
		
		for(int i=0;i<N;i++) {
			loopCondition[i] = new UMLCondition("x > " + i, umlSD);
			umlLoop[i] = new UMLLoop(loopCondition[i],umlSD);	
			umlLoop[i].setCodeGenFlag(true);
			if(i!=0) {
				umlLoop[i].setParentContainer(umlLoop[i-1]);
			}
		}
		ArrayList<UMLMessageArgument> class1CreateArgs = new ArrayList<UMLMessageArgument>();
		class1CreateArgs.add(new UMLMessageArgument("x", "int", "3", false));
		UMLCreateMessage class1CreateMessage = new UMLCreateMessage("Class1", class1Class, class1CreateArgs, clientActBox, umlSD);
		
		UMLClass class2Class = new UMLClass("Class2", "class2", umlSD);
		UMLLifeline class2Lifeline = new UMLLifeline(class2Class, umlSD);
		UMLActivationBox class2ActivationBox = new UMLActivationBox(class2Lifeline, umlSD);
		
		String class2args = "";
		for(int i=0;i<N;i++) {
			class2args += "x-"+i;
			if(i!=N-1) {
				class2args += ",";
			}
		}
				
		
		ArrayList<UMLMessageArgument> class2CreateArgs = new ArrayList<UMLMessageArgument>();
		class2CreateArgs.add(new UMLMessageArgument("intArgs", "int", class2args, true));
		UMLCreateMessage class2CreateMessage = new UMLCreateMessage("Class2", class2Class, class2CreateArgs, class1ActBox, umlSD);
		if(N>0) {
			class2CreateMessage.setParentContainer(loopCondition[N-1]);	
		}
		
		class1CreateMessage.addNestedMessage(class2CreateMessage);
		
		UMLCreateMessage.setCodeHandler(new UMLSDCreateMessageHandler());
		UMLClass.setCodeHandler(new UMLSDClassHandler());
		
		LinkedList<CompilationUnit> compUnits = new LinkedList<CompilationUnit>();
		try {
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		} catch (UMLSDStructureException e) {
			System.out.println(e.getMessage());
			throw new Exception(e);
		}
		
		ArrayList<String> linesOfCode = new ArrayList<String>();
		for (CompilationUnit compUnit : compUnits) {
			String[] codeUnit = compUnit.toString().split("\r\n");
			for(String lineOfCode:codeUnit) {
				linesOfCode.add(lineOfCode);
			}
			System.out.println(compUnit);
		}
		assertEquals("public class Client {",linesOfCode.get(0));
		assertEquals("",linesOfCode.get(1));
		assertEquals("    public static void main(String[] args) {",linesOfCode.get(2));
		assertEquals("        Class1 class1 = new Class1(3);",linesOfCode.get(3));
		assertEquals("    }",linesOfCode.get(4));
		assertEquals("}",linesOfCode.get(5));
		assertEquals("public class Class1 {",linesOfCode.get(6));
		assertEquals("",linesOfCode.get(7));
		assertEquals("    public Class1(int x) {",linesOfCode.get(8));
		
		int startLine = 9;
		String class2 = "";
		for(int i=0; i<N; i++) {
			String spaces = "        ";
			for(int j=0;j<i;j++) {
				spaces += "    ";
			}
			assertEquals(spaces + "while (x > "+i+") {",linesOfCode.get(i+startLine));
			assertEquals(spaces +"}",linesOfCode.get((N*2)-i + startLine));
			
			class2+="x-"+i;
			if(i!=N-1) {
				class2+=",";
			}else {
				class2 =spaces+"    "+"Class2 class2 = new Class2("+class2;
				class2+=");";
			}
		}
		assertEquals(linesOfCode.get(N+startLine),class2);
		
		assertEquals("    }",linesOfCode.get(N*2+1+startLine));
		assertEquals("}",linesOfCode.get(N*2+2+startLine));
		assertEquals("public class Class2 {",linesOfCode.get(N*2+3+startLine));
		assertEquals("",linesOfCode.get(N*2+4+startLine));
		assertEquals("    public Class2(int... intArgs) {",linesOfCode.get(N*2+5+startLine));
		assertEquals("    }",linesOfCode.get(N*2+6+startLine));
		assertEquals("}",linesOfCode.get(N*2+7+startLine));

		
		
	}

}
