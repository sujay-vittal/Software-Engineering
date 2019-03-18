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
import edu.purdue.cs59000.umltranslator.message.UMLReturnMessage;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;
import edu.purdue.cs59000.umltranslator.umlcodehandler.SequenceDiagramCodeController;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDClassHandler;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDCreateMessageHandler;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLSDSynchronousMessageHandler;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLAlternatives;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLCondition;
import edu.purdue.cs59000.umltranslator.umlcontainer.UMLElse;

public class UMLSDSynchronousMessageHandlerTest {

	@Test
	public void generateSourceCodeTest() throws UMLSDStructureException {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLActor user = new UMLActor("User");	// creating the 'User' actor
		UMLLifeline userLifeline = new UMLLifeline(user, umlSD);
		UMLActivationBox userActBox = new UMLActivationBox(userLifeline, umlSD);
		
		UMLClass atmUI = new UMLClass("ATMUserInterface", "atmUI", umlSD);	// creating the ATMUserInterface class
		UMLLifeline atmUILifeline = new UMLLifeline(atmUI, umlSD);
		UMLActivationBox atmUIActBox = new UMLActivationBox(atmUILifeline, umlSD);
		
		UMLClass withdrawalRequest = new UMLClass("WithdrawalRequest", "request", umlSD);	// creating the WithdrawalRequest class
		UMLLifeline withdrawalRequestLifeline = new UMLLifeline(withdrawalRequest, umlSD);
		UMLActivationBox withdrawalRequestActBox = new UMLActivationBox(withdrawalRequestLifeline, umlSD);
		
		UMLClass cash = new UMLClass("Cash", "cash", umlSD);	// creating the Cash class
		UMLLifeline cashLifeline = new UMLLifeline(cash, umlSD);
		UMLActivationBox cashActBox1 = new UMLActivationBox(cashLifeline, umlSD);
		UMLActivationBox cashActBox2 = new UMLActivationBox(cashLifeline, umlSD);
		
		UMLCondition hasFunds = new UMLCondition("amount >= funds", umlSD);	// creating the alternative for the cash methods
		UMLElse doesntHaveFunds = new UMLElse(umlSD);
		UMLAlternatives fundsIfElse = new UMLAlternatives(hasFunds, doesntHaveFunds, umlSD);
		
		// creating cashWithdrawal message, adding its arguments, and creating return message
		UMLSynchronousMessage cashWithdrawal = new UMLSynchronousMessage("cashWithdrawal", "Cash", new ArrayList<UMLMessageArgument>(), userActBox, atmUIActBox, umlSD);
		cashWithdrawal.addArgument(new UMLMessageArgument("amount", "double", "amount", false));
		UMLReturnMessage cashWithdrawalReturn = new UMLReturnMessage("cash", atmUIActBox, userActBox, umlSD);
		
		// creating WithdrawalRequest create message, adding its arguments, and adding it to nested messages
		UMLCreateMessage withdrawalRequestCreateMessage = new UMLCreateMessage("WithdrawalRequest", withdrawalRequest, new ArrayList<UMLMessageArgument>(), atmUIActBox, umlSD);
		withdrawalRequestCreateMessage.addArgument(new UMLMessageArgument("amount", "double", "amount", false));
		cashWithdrawal.addNestedMessage(withdrawalRequestCreateMessage);
		
		// creating getCash method, adding its return message, and adding them to nested messages
		UMLSynchronousMessage getCash = new UMLSynchronousMessage("getCash", "Cash", new ArrayList<UMLMessageArgument>(), atmUIActBox, withdrawalRequestActBox, umlSD);
		UMLReturnMessage getCashReturn = new UMLReturnMessage("cash", withdrawalRequestActBox, atmUIActBox, umlSD);
		cashWithdrawal.addNestedMessage(getCash);
		cashWithdrawal.addNestedMessage(getCashReturn);
		
		// creating the Cash create message, adding its arguments, and adding it to the nested messages
		UMLCreateMessage cashCreate = new UMLCreateMessage("Cash", cash, new ArrayList<UMLMessageArgument>(), withdrawalRequestActBox, umlSD);
		cashCreate.addArgument(new UMLMessageArgument("amount", "double", "amount", false));
		getCash.addNestedMessage(cashCreate);
		
		// creating the generateAvailableFundsReport message, setting its conditions, and adding to nested messages
		UMLSynchronousMessage generateAFR = new UMLSynchronousMessage("generateAvailableFundsReport", "void", new ArrayList<UMLMessageArgument>(), withdrawalRequestActBox, cashActBox1, umlSD);
		generateAFR.setParentContainer(hasFunds);
		getCash.addNestedMessage(generateAFR);
		
		// doing the same for the generateInsufficientFunds message
		UMLSynchronousMessage generateIFR = new UMLSynchronousMessage("generateInsufficientFundsReport", "void", new ArrayList<UMLMessageArgument>(), withdrawalRequestActBox, cashActBox2, umlSD);
		generateIFR.setParentContainer(doesntHaveFunds);
		getCash.addNestedMessage(generateIFR);
		
		// setting code handlers for source code generation
		UMLCreateMessage.setCodeHandler(new UMLSDCreateMessageHandler());
		UMLSynchronousMessage.setCodeHandler(new UMLSDSynchronousMessageHandler());
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
		
		assertEquals(linesOfCode.get(0), "public class ATMUserInterface {");
		assertEquals(linesOfCode.get(1), "");
		assertEquals(linesOfCode.get(2), "    public Cash cashWithdrawal(double amount) {");
		assertEquals(linesOfCode.get(3), "        WithdrawalRequest request = new WithdrawalRequest(amount);");
		assertEquals(linesOfCode.get(4), "        Cash cash = request.getCash();");
		assertEquals(linesOfCode.get(5), "    }");
		assertEquals(linesOfCode.get(6), "}");
		assertEquals(linesOfCode.get(7), "public class WithdrawalRequest {");
		assertEquals(linesOfCode.get(8), "");
		assertEquals(linesOfCode.get(9), "    public WithdrawalRequest(double amount) {");
		assertEquals(linesOfCode.get(10), "    }");
		assertEquals(linesOfCode.get(11), "");
		assertEquals(linesOfCode.get(12), "    public Cash getCash() {");
		assertEquals(linesOfCode.get(13), "        Cash cash = new Cash(amount);");
		assertEquals(linesOfCode.get(14), "        if (amount >= funds) {");
		assertEquals(linesOfCode.get(15), "            cash.generateAvailableFundsReport();");
		assertEquals(linesOfCode.get(16), "        } else {");
		assertEquals(linesOfCode.get(17), "            cash.generateInsufficientFundsReport();");
		assertEquals(linesOfCode.get(18), "        }");
		assertEquals(linesOfCode.get(19), "    }");
		assertEquals(linesOfCode.get(20), "}");
		assertEquals(linesOfCode.get(21), "public class Cash {");
		assertEquals(linesOfCode.get(22), "");
		assertEquals(linesOfCode.get(23), "    public Cash(double amount) {");
		assertEquals(linesOfCode.get(24), "    }");
		assertEquals(linesOfCode.get(25), "");
		assertEquals(linesOfCode.get(26), "    public void generateAvailableFundsReport() {");
		assertEquals(linesOfCode.get(27), "    }");
		assertEquals(linesOfCode.get(28), "");
		assertEquals(linesOfCode.get(29), "    public void generateInsufficientFundsReport() {");
		assertEquals(linesOfCode.get(30), "    }");
		assertEquals(linesOfCode.get(31), "}");
		
	}

}
