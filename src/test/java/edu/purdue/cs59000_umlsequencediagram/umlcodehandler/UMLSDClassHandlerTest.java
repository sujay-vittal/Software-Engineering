package edu.purdue.cs59000_umlsequencediagram.umlcodehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.*;
import edu.purdue.cs59000.umltranslator.umlcontainer.*;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;

public class UMLSDClassHandlerTest {

	@Test
	public void generateSourceCodeTest() throws Exception {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLClass.setCodeHandler(new UMLSDClassHandler());	// setting the source code handler to UMLSDClassHandler
		UMLSynchronousMessage.setCodeHandler(null);
		
		UMLClass class1 = new UMLClass("Class1", "class1", umlSD);	// creating a class and it's children
		UMLLifeline lifeline1 = new UMLLifeline(class1, umlSD);
		UMLActivationBox actBox1 = new UMLActivationBox(lifeline1, umlSD);
		
		UMLClass class2 = new UMLClass("Class2", "class2", umlSD);	// creating a second class and it's children
		UMLLifeline lifeline2 = new UMLLifeline(class2, umlSD);
		UMLActivationBox actBox2 = new UMLActivationBox(lifeline2, umlSD);
		
		UMLClass class3 = new UMLClass("Class2", "class3", umlSD);	// creating another instance of Class2, this shouldn't affect compUnits
		UMLLifeline lifeline3 = new UMLLifeline(class3, umlSD);
		UMLActivationBox actBox3 = new UMLActivationBox(lifeline3, umlSD);
		
		UMLSynchronousMessage message = new UMLSynchronousMessage("method1", "String", new ArrayList<UMLMessageArgument>(), actBox1, actBox2, umlSD);
		
		LinkedList<CompilationUnit> compUnits = new LinkedList<CompilationUnit>();	// adding an empty compilation unit list to be filled 
		try {
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		} catch (UMLSDStructureException e) {
			System.out.println(e.getMessage());
		}
		
		assertEquals(compUnits.size(), 2); // no additional class should have created from class3 
		
		Optional<ClassOrInterfaceDeclaration> compClass1 = Optional.empty();	// initializing as empty
		Optional<ClassOrInterfaceDeclaration> compClass2 = Optional.empty();
		for (CompilationUnit compUnit : compUnits) {	// checking if classes exist in compUnits and assigning to Optionals if they do
			if (compUnit.getClassByName("Class1").isPresent()) {
				compClass1 = compUnit.getClassByName("Class1");
			}
			
			if (compUnit.getClassByName("Class2").isPresent()) {
				compClass2 = compUnit.getClassByName("Class2");
			}
				
		}
		
		assertTrue(compClass1.isPresent());	// ensuring the classes exist in the ast
		assertTrue(compClass2.isPresent());	
		
		String class1SourceCode = compClass1.get().toString();
		String class2SourceCode = compClass2.get().toString();
		
		assertEquals(class1SourceCode, "public class Class1 {\r\n" +	// ensuring the source code output is correct
				"}");
		assertEquals(class2SourceCode, "public class Class2 {\r\n" + 
				"}");
	}

}
