package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.message.UMLReturnMessage;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;
import edu.purdue.cs59000.umltranslator.umlcodehandler.SequenceDiagramCodeController;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDClassHandler;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLElse;

public class functionaltestsLibraryApp {

	@Test
	public void test() throws UMLSDStructureException {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLClass.setCodeHandler(new UMLSDClassHandler());	// setting the source code handler to UMLSDClassHandler
		
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
		SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		
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
		
		assertEquals(class1SourceCode, "public class Class1 {\r\n"+ 	// ensuring the source code output is correct
	"}");
		assertEquals(class2SourceCode, "public class Class2 {\r\n"+  
				"}");
			
			UMLClass librarian = new UMLClass("librarian", "L", umlSD);	// creating a class and it's children
			UMLLifeline librarianlifeline = new UMLLifeline(librarian, umlSD);
			UMLActivationBox libactBox = new UMLActivationBox(librarianlifeline, umlSD);
			
			UMLClass book = new UMLClass("Book", "B", umlSD);	// creating a second class and it's children
			UMLLifeline booklifeline = new UMLLifeline(book,umlSD);
			UMLActivationBox bookActBox = new UMLActivationBox(booklifeline, umlSD);
			
			UMLClass memberRecord = new UMLClass("MemberRecord", "MR", umlSD);	// creating another instance of Class2, this shouldn't affect compUnits
			UMLLifeline memberRecordlifeline = new UMLLifeline(memberRecord, umlSD);
			UMLActivationBox mrActBox = new UMLActivationBox(memberRecordlifeline, umlSD);
			
			/*UMLClass transaction  = new UMLClass("T", "transaction", umlSD);	// creating another instance of Class2, this shouldn't affect compUnits
			UMLLifeline transactionlifeline = new UMLLifeline(transaction, umlSD);
			UMLActivationBox transactionActBox = new UMLActivationBox(transactionlifeline, umlSD);*/
			
			ArrayList<UMLMessageArgument> bookAvailableArgs = new ArrayList<UMLMessageArgument>();
			UMLMessageArgument bookAvailablecheck = new UMLMessageArgument("bookName","AutherName","Edition");
			bookAvailableArgs.add(bookAvailablecheck);
			UMLSynchronousMessage message1 = new UMLSynchronousMessage("check availability of book", "String",bookAvailableArgs, libactBox,bookActBox,umlSD);
	        UMLReturnMessage bookAvailable  = new UMLReturnMessage("bookDetails",bookActBox,libactBox,umlSD);
			UMLReturnMessage bookNotAvailable = new UMLReturnMessage("noREsultFound",bookActBox,libactBox,umlSD);
			UMLCondition hasbook = new UMLCondition("instock > 0",umlSD);
			UMLElse checkedout = new UMLElse(umlSD);
			UMLAlternatives bookAvailableAlternative=new UMLAlternatives(hasbook,checkedout,umlSD); 
			bookAvailable.setParentContainer(hasbook);
			bookNotAvailable.setParentContainer(checkedout);
			
			ArrayList<UMLMessageArgument> studentArgs = new ArrayList<UMLMessageArgument>();
			UMLMessageArgument checkstudent = new UMLMessageArgument("studentFirstname","studentlastname","id");
			studentArgs.add(checkstudent);
			UMLSynchronousMessage statusofstudent = new UMLSynchronousMessage("checkstatusofstudent", "String",studentArgs , libactBox,mrActBox,umlSD);
			UMLReturnMessage studentAvailable  = new UMLReturnMessage("studentDetails",mrActBox,libactBox,umlSD);
			UMLReturnMessage studentNotAvailable = new UMLReturnMessage("noREsultFound",mrActBox,libactBox,umlSD);
			UMLCondition hasstudent = new UMLCondition("hasaccount==true",umlSD);
			UMLElse nostudent = new UMLElse(umlSD);
			UMLAlternatives studentAvailableAlternative=new UMLAlternatives(hasstudent,nostudent,umlSD); 
			studentAvailable.setParentContainer(hasstudent);
			bookNotAvailable.setParentContainer(nostudent);
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
			
			assertEquals(compUnits.size(), 5); // no additional class should have created from class3, 4 more for lib exmples
			ArrayList<String> linesOfCode = new ArrayList<String>();
						for (CompilationUnit compUnit : compUnits) {
							String[] codeUnit = compUnit.toString().split("\r\n");
							for(String lineOfCode:codeUnit) {
								linesOfCode.add(lineOfCode);
							}
							System.out.println(compUnit);
			}
			
						assertEquals("public class Class1 {",linesOfCode.get(0));
						assertEquals("}",linesOfCode.get(1));
						assertEquals("public class Class2 {",linesOfCode.get(2));
						assertEquals("}",linesOfCode.get(3));
						assertEquals("public class librarian {",linesOfCode.get(4));
						assertEquals("}",linesOfCode.get(5));
						assertEquals("public class Book {",linesOfCode.get(6));
						assertEquals("}",linesOfCode.get(7));
						assertEquals("public class MemberRecord {",linesOfCode.get(8));
					assertEquals("}",linesOfCode.get(9));
			umlSD.removeSymbol(10);
			System.out.println("\n");
		    for(UMLSymbol symbol : umlSD.getUMLSymbols()) {
				System.out.println(symbol.getId());
			}
			
		}

	} 