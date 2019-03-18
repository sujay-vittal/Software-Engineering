package edu.purdue.cs59000.umltranslator.message;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;

public class UMLSynchronousMessageTest {
	UMLSynchronousMessage synchronousMessage1;
	UMLActivationBox source;
	UMLActivationBox dest;
	
	@Before
	public void initialize() throws Exception{
		String name = "sync";
		String returnType = "test";
		UMLClass sourceClass = new UMLClass("source","Source");
		UMLClass destClass = new UMLClass("dest","Dest");
		UMLLifeline sourceLifeline = new UMLLifeline(sourceClass);
		UMLLifeline destLifeline = new UMLLifeline(destClass);		
		source = new UMLActivationBox(sourceLifeline);
		dest = new UMLActivationBox(destLifeline);
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		synchronousMessage1 = new UMLSynchronousMessage(name,returnType,args,source,dest);
		
	}

	@Test
	public void testEqualsReturnsTrue() throws Exception {
		String name = "sync";
		String returnType = "test";
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLSynchronousMessage synchronousMessage2 = new UMLSynchronousMessage(name,returnType,args,source,dest);
		assertTrue(synchronousMessage1.equals(synchronousMessage2));
	}
	
	@Test
	public void testDifferentFieldsEqualsReturnsFalse() throws Exception{
		String name = "sync1";
		String returnType = "test";
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLSynchronousMessage synchronousMessage2 = new UMLSynchronousMessage(name,returnType,args,source,dest);
		assertFalse(synchronousMessage1.equals(synchronousMessage2));
		
		synchronousMessage2 = new UMLSynchronousMessage("sync","test1",args,source,dest);
		assertFalse(synchronousMessage1.equals(synchronousMessage2));
	}
	
	@Test
	public void testDifferentSourcesEqualsReturnsFalse() throws Exception{
		String name = "sync";
		String returnType = "test";
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLClass sourceClass = new UMLClass("source1","Source");
		UMLLifeline sourceLifeline = new UMLLifeline(sourceClass);
		source = new UMLActivationBox(sourceLifeline);
		UMLSynchronousMessage synchronousMessage2 = new UMLSynchronousMessage(name,returnType,args,source,dest);
		assertFalse(synchronousMessage1.equals(synchronousMessage2));
	}
	
	@Test
	public void testDifferentDestinationsEqualsReturnsFalse() throws Exception{
		String name = "sync";
		String returnType = "test";
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLClass destClass = new UMLClass("source1","Source");
		UMLLifeline destLifeline = new UMLLifeline(destClass);	
		dest = new UMLActivationBox(destLifeline);
		UMLSynchronousMessage synchronousMessage2 = new UMLSynchronousMessage(name,returnType,args,source,dest);
		assertFalse(synchronousMessage1.equals(synchronousMessage2));
	}
	
	@Test
	public void testInvalidObjectEqualsReturnsFalse(){
		Integer int1 = new Integer(0);
		assertFalse(synchronousMessage1.equals(int1));
	}
	
	@Test
	public void getReturnTypeTest1() throws UMLSDStructureException {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		
		UMLClass classWithInvokingMessage = new UMLClass("ClassWithInvokingMessage", "classWithInvokingMessage", umlSD);	// creating class to start the seq.
		UMLLifeline lifelineWithInvokingMessage = new UMLLifeline(classWithInvokingMessage, umlSD);
		UMLActivationBox actBoxWithInvokingMessage = new UMLActivationBox(lifelineWithInvokingMessage, umlSD);
		
		UMLClass classWithSyncMessage = new UMLClass("ClassWithSyncMessage", "classWithSyncMessage", umlSD);	// creating class to invoke the sync message
		UMLLifeline lifelineWithSyncMessage = new UMLLifeline(classWithSyncMessage, umlSD);
		UMLActivationBox actBoxWithSyncMessage = new UMLActivationBox(lifelineWithSyncMessage, umlSD);
		
		UMLClass classThatReceivesSyncMessage = new UMLClass("ClassThatReceivesSyncMessage", "classThatReceivesSyncMessage", umlSD);	// class to receive sync m.
		UMLLifeline lifelineThatReceivesSyncMessage = new UMLLifeline(classThatReceivesSyncMessage);
		UMLActivationBox actBoxThatReceivesSyncMessage = new UMLActivationBox(lifelineThatReceivesSyncMessage, umlSD);
		
		UMLCreateMessage messageToStartSeq = new UMLCreateMessage("ClassWithSyncMessage", classWithSyncMessage, new ArrayList<UMLMessageArgument>(), actBoxWithInvokingMessage, umlSD);
		
		UMLSynchronousMessage syncMessage = new UMLSynchronousMessage("syncMessage", "String", new ArrayList<UMLMessageArgument>(), actBoxWithSyncMessage, actBoxThatReceivesSyncMessage, umlSD);
		messageToStartSeq.addNestedMessage(syncMessage);
		
		UMLReturnMessage returnMessage = new UMLReturnMessage("stringValue", actBoxThatReceivesSyncMessage, actBoxWithSyncMessage);
		messageToStartSeq.addNestedMessage(returnMessage);
		
		UMLReturnMessage returnMessageReceived = syncMessage.getReturnMessage(umlSD);
		assertEquals(returnMessage.getReturnName(), returnMessageReceived.getReturnName());
		assertEquals(returnMessage.getId(), returnMessageReceived.getId());
		
		UMLSynchronousMessage voidMessage = new UMLSynchronousMessage("voidMessage", "void", new ArrayList<UMLMessageArgument>(), actBoxWithSyncMessage, actBoxThatReceivesSyncMessage, umlSD);
		messageToStartSeq.addNestedMessage(voidMessage);
		
		UMLReturnMessage returnMessageReceivedFromVoidMessage = voidMessage.getReturnMessage(umlSD);
		assertNull(returnMessageReceivedFromVoidMessage);
		
		UMLSynchronousMessage messageWithReturnTypeButNoReturnMessage = new UMLSynchronousMessage("messageWithReturnTypeButNoReturnMessage", "String", new ArrayList<UMLMessageArgument>(), actBoxWithSyncMessage, actBoxThatReceivesSyncMessage, umlSD);
		messageToStartSeq.addNestedMessage(messageWithReturnTypeButNoReturnMessage);
		
		UMLReturnMessage returnMessageReceivedFromNoReturnMessage = messageWithReturnTypeButNoReturnMessage.getReturnMessage(umlSD);
		assertNull(returnMessageReceivedFromNoReturnMessage);
		
	}

}
