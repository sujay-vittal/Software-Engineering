package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.logging.*;
import edu.purdue.cs59000.umltranslator.exceptions.*;
import edu.purdue.cs59000.umltranslator.message.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.SequenceDiagramCodeController;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDClassHandler;
import edu.purdue.cs59000.umltranslator.umlcontainer.*;

public class functionalTestYamini {

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

		UMLSynchronousMessage message = new UMLSynchronousMessage("method1", "String",
				new ArrayList<UMLMessageArgument>(), actBox1, actBox2, umlSD);

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

		assertEquals(class1SourceCode, "public class Class1 {\r\n" + "}");
		assertEquals(class2SourceCode, "public class Class2 {\r\n" + "}");

		// class customer:Customer and its lifeline
		UMLClass customer = new UMLClass("customer", "Customer", umlSD);
		UMLLifeline customerLifeline = new UMLLifeline(customer, umlSD);

		// class atmmachine:Atm and its lifeline
		UMLClass atmMachine = new UMLClass("atmmachine", "Atm", umlSD);
		UMLLifeline AtmLifeline = new UMLLifeline(atmMachine, umlSD);

		// activation boxes for customer and atm
		UMLActivationBox customeractivation = new UMLActivationBox(customerLifeline, umlSD);
		UMLActivationBox atmactivation = new UMLActivationBox(AtmLifeline, umlSD);

		// list of message arguments belonging to synchronous message "withdrawal"
		ArrayList<UMLMessageArgument> withdrawalAmountargs = new ArrayList<UMLMessageArgument>();

		// syntax for this call to "withdrawal" would be "withdrawal($200)"
		UMLMessageArgument withdrawalAmount = new UMLMessageArgument("withdrawalAmount", "Dollar", "200");
		withdrawalAmountargs.add(withdrawalAmount);

		// source code for withdrawal method is "public int withdrawal(Dollar
		// withdrawalAmount)", called from class Customer and prompting class Atm
		UMLSynchronousMessage withdrawal = new UMLSynchronousMessage("withdrawal", "int", withdrawalAmountargs,
				customeractivation, atmactivation, umlSD);

		// two possible return messages for synchronous message "withdrawal($200)"
		// either transaction successful or unsuccessful
		UMLReturnMessage confirmWithdraw = new UMLReturnMessage("transactionSuccessful", atmactivation,
				customeractivation, umlSD);
		UMLReturnMessage rejectWithdraw = new UMLReturnMessage("transactionUnsuccessful", atmactivation,
				customeractivation, umlSD);

		// the condition for these return messages is "if(balance >
		// withdrawalAmount){success;} else{fail;}"
		UMLCondition hasAmount = new UMLCondition("balance > withdrawalAmount", umlSD);
		UMLElse lowBalance = new UMLElse(umlSD);

		// set a container to properly order the two return messages
		UMLAlternatives balanceCheck = new UMLAlternatives(hasAmount, lowBalance, umlSD);

		// add the return messages to the proper condition containers
		confirmWithdraw.setParentContainer(hasAmount);
		rejectWithdraw.setParentContainer(lowBalance);
		SequenceDiagramCodeController.generateSourceCode(umlSD, compUnits);

		assertEquals(compUnits.size(), 4);

		ArrayList<String> linesOfCode = new ArrayList<String>();
		for (CompilationUnit compUnit : compUnits) {
			String[] codeUnit = compUnit.toString().split("\r\n");
			for (String lineOfCode : codeUnit) {
				linesOfCode.add(lineOfCode);
			}
			System.out.println(compUnit);
		}
		assertEquals("public class Class1 {", linesOfCode.get(0));
		assertEquals("}", linesOfCode.get(1));
		assertEquals("public class Class2 {", linesOfCode.get(2));
		assertEquals("}", linesOfCode.get(3));
		assertEquals("public class customer {", linesOfCode.get(4));
		assertEquals("}", linesOfCode.get(5));
		assertEquals("public class atmmachine {", linesOfCode.get(6));
		assertEquals("}", linesOfCode.get(7));

		umlSD.removeSymbol(6);

		// iterate through all objects contained in umlSD, check their uniqID and class
		for (UMLSymbol symbol : umlSD.getUMLSymbols()) {
			System.out.println(symbol.getId());
		}
	}
}
