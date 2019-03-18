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

public class functionaltestsOnlineShopping {

	@Test
	public void test() throws UMLSDStructureException {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLClass.setCodeHandler(new UMLSDClassHandler());	
		UMLClass class1 = new UMLClass("Class1", "class1", umlSD);
		UMLLifeline lifeline1 = new UMLLifeline(class1, umlSD);
		UMLActivationBox actBox1 = new UMLActivationBox(lifeline1, umlSD);
		
		UMLClass class2 = new UMLClass("Class2", "class2", umlSD);	
		UMLLifeline lifeline2 = new UMLLifeline(class2, umlSD);
		UMLActivationBox actBox2 = new UMLActivationBox(lifeline2, umlSD);
		
		UMLClass class3 = new UMLClass("Class2", "class3", umlSD);	
		UMLLifeline lifeline3 = new UMLLifeline(class3, umlSD);
		UMLActivationBox actBox3 = new UMLActivationBox(lifeline3, umlSD);
		
		UMLSynchronousMessage message = new UMLSynchronousMessage("method1", "String", new ArrayList<UMLMessageArgument>(), actBox1, actBox2, umlSD);
		
		LinkedList<CompilationUnit> compUnits = new LinkedList<CompilationUnit>();	
		SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
		
		Optional<ClassOrInterfaceDeclaration> compClass1 = Optional.empty();	
		Optional<ClassOrInterfaceDeclaration> compClass2 = Optional.empty();
		for (CompilationUnit compUnit : compUnits) {	
			if (compUnit.getClassByName("Class1").isPresent()) {
				compClass1 = compUnit.getClassByName("Class1");
			}
			
			if (compUnit.getClassByName("Class2").isPresent()) {
				compClass2 = compUnit.getClassByName("Class2");
			}
				
		}
		
		assertTrue(compClass1.isPresent());	
		assertTrue(compClass2.isPresent());	
		
		String class1SourceCode = compClass1.get().toString();
		String class2SourceCode = compClass2.get().toString();
		
		assertEquals(class1SourceCode, "public class Class1 {\r\n" +	
				"}");
		assertEquals(class2SourceCode, "public class Class2 {\r\n" + 
				"}");
			
			UMLClass customer = new UMLClass("C", "customer", umlSD);	
			UMLLifeline customerlifeline = new UMLLifeline(customer, umlSD);
			UMLActivationBox custActBox = new UMLActivationBox(customerlifeline, umlSD);
			
			UMLClass system = new UMLClass("S", "system", umlSD);	
			UMLLifeline systemlifeline = new UMLLifeline(system,umlSD);
			UMLActivationBox systemActBox = new UMLActivationBox(systemlifeline, umlSD);
			
			UMLClass shoppingCart = new UMLClass("SC", "shoppingCart", umlSD);	
			UMLLifeline shoppingCartlifeline = new UMLLifeline(shoppingCart, umlSD);
			UMLActivationBox scActBox = new UMLActivationBox(shoppingCartlifeline, umlSD);
			
			
			ArrayList<UMLMessageArgument> UserValidationArgs = new ArrayList<UMLMessageArgument>();
			UMLMessageArgument validationcheck = new UMLMessageArgument("DOB","UserName","Password");
			UserValidationArgs.add(validationcheck);
			UMLSynchronousMessage message1 = new UMLSynchronousMessage("check the validation", "String",UserValidationArgs, custActBox,systemActBox,umlSD);
	        UMLReturnMessage ValidUser  = new UMLReturnMessage("user activated",systemActBox,custActBox,umlSD);
			UMLReturnMessage NotValidUser = new UMLReturnMessage("noREsultFound",systemActBox,custActBox,umlSD);
			UMLCondition isValid = new UMLCondition("checkCredentials()",umlSD);
			UMLElse notValid = new UMLElse(umlSD);
			UMLAlternatives userValidationAlternative=new UMLAlternatives(isValid,notValid,umlSD); 
			ValidUser.setParentContainer(isValid);
			NotValidUser.setParentContainer(notValid);
			
			ArrayList<UMLMessageArgument> systemArgs = new ArrayList<UMLMessageArgument>();
			UMLSynchronousMessage addorUpdateCart = new UMLSynchronousMessage("add or update cart", "String",systemArgs, systemActBox, scActBox,umlSD);
			UMLReturnMessage itemAvailable  = new UMLReturnMessage("itemDetails",scActBox,systemActBox,umlSD);
			UMLReturnMessage itemNotAvailable  = new UMLReturnMessage("noREsultFound",scActBox,systemActBox,umlSD);
			UMLReturnMessage itemAvailablityToCustomer  = new UMLReturnMessage("itemDetails",systemActBox,custActBox,umlSD);
			UMLReturnMessage itemNotAvailityToCustomer = new UMLReturnMessage("noREsultFound",systemActBox,custActBox,umlSD);
			UMLCondition hasitem = new UMLCondition("itemAvailable==true",umlSD);
			UMLElse noitem = new UMLElse(umlSD);
			UMLAlternatives itemAvailableAlternative=new UMLAlternatives(hasitem,noitem,umlSD); 
			itemAvailable.setParentContainer(hasitem);
			itemNotAvailable.setParentContainer(noitem);
			
			SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);
			
			assertEquals(compUnits.size(), 5); 
			
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
			assertEquals("public class C {",linesOfCode.get(4));
			assertEquals("}",linesOfCode.get(5));
			assertEquals("public class S {",linesOfCode.get(6));
			assertEquals("}",linesOfCode.get(7));
			assertEquals("public class SC {",linesOfCode.get(8));
			assertEquals("}",linesOfCode.get(9));
			umlSD.removeSymbol(8);
		    for(UMLSymbol symbol : umlSD.getUMLSymbols()) {
				System.out.println(symbol.getId());
			}
			
		}

	}
