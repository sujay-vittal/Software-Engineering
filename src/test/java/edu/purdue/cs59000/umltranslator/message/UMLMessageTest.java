package edu.purdue.cs59000.umltranslator.message;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.purdue.cs59000.umltranslator.*;

public class UMLMessageTest {

  @Test
  public void getInvokerTest() throws Exception {
    UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
    
    UMLClass invokerClass = new UMLClass("InvokerClass", "invokerClass", umlSD);
    UMLLifeline invokerLifeline = new UMLLifeline(invokerClass, umlSD);
    UMLActivationBox invokerActivationBox = new UMLActivationBox(invokerLifeline, umlSD);
    
    UMLClass receiverClass = new UMLClass("ReceiverClass", "receiverClass", umlSD);
    UMLLifeline receiverLifeline = new UMLLifeline(receiverClass, umlSD);
    UMLActivationBox receiverActivationBox = new UMLActivationBox(receiverLifeline,umlSD);
    
    UMLSynchronousMessage syncMessage = new UMLSynchronousMessage("methodName", "ReturnType", new ArrayList<UMLMessageArgument>(), invokerActivationBox, receiverActivationBox, umlSD);
    
    assertEquals(syncMessage.getInvoker(), invokerClass);
    
    UMLCreateMessage createMessage = new UMLCreateMessage("newReceiverObject", receiverClass, new LinkedList<UMLMessageArgument>(), invokerActivationBox, umlSD);
    
    assertEquals(createMessage.getInvoker(), invokerClass);
    
    UMLReturnMessage returnMessage = new UMLReturnMessage("objectName", receiverActivationBox, invokerActivationBox, umlSD);
    
    assertEquals(returnMessage.getInvoker(), receiverClass);
  }
  
  @Test
  public void getReceiverTest() throws Exception {
    UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
    
    UMLClass invokerClass = new UMLClass("InvokerClass", "invokerClass", umlSD);
    UMLLifeline invokerLifeline = new UMLLifeline(invokerClass, umlSD);
    UMLActivationBox invokerActivationBox = new UMLActivationBox(invokerLifeline, umlSD);
    
    UMLClass receiverClass = new UMLClass("ReceiverClass", "receiverClass", umlSD);
    UMLLifeline receiverLifeline = new UMLLifeline(receiverClass, umlSD);
    UMLActivationBox receiverActivationBox = new UMLActivationBox(receiverLifeline, umlSD);
    
    UMLSynchronousMessage syncMessage = new UMLSynchronousMessage("methodName", "ReturnType", new ArrayList<UMLMessageArgument>(), invokerActivationBox, receiverActivationBox, umlSD);
    
    assertEquals(syncMessage.getReceiver(), receiverClass);
    
    UMLCreateMessage createMessage = new UMLCreateMessage("newReceiverObject", receiverClass, new ArrayList<UMLMessageArgument>(), invokerActivationBox, umlSD);
    
    assertEquals(createMessage.getReceiver(), receiverClass);
    
    UMLReturnMessage returnMessage = new UMLReturnMessage("objectName", receiverActivationBox, invokerActivationBox, umlSD);
    
    assertEquals(returnMessage.getReceiver(), invokerClass);
  }
  
  @Test
  public void getInvokingMessageTest() throws Exception {
	  UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
	  
	  UMLClass classWithInvokingMessage = new UMLClass("ClassWithInvokingMessage", "classWithInvokingMessage", umlSD);
	  UMLLifeline classWithInvokingMessageLifeline = new UMLLifeline(classWithInvokingMessage, umlSD);
	  UMLActivationBox classWithInvokingMessageActBox = new UMLActivationBox(classWithInvokingMessageLifeline, umlSD);
	  
	  UMLClass invokerClass = new UMLClass("InvokerClass", "invokerClass", umlSD);
	  UMLLifeline invokerLifeline = new UMLLifeline(invokerClass, umlSD);
	  UMLActivationBox invokerActivationBox = new UMLActivationBox(invokerLifeline, umlSD);
	    
	  UMLClass receiverClass = new UMLClass("ReceiverClass", "receiverClass", umlSD);
	  UMLLifeline receiverLifeline = new UMLLifeline(receiverClass, umlSD);
	  UMLActivationBox receiverActivationBox = new UMLActivationBox(receiverLifeline, umlSD);
	  
	  UMLSynchronousMessage invokingMessage = new UMLSynchronousMessage("invokingMessage", "void", new ArrayList<UMLMessageArgument>(), classWithInvokingMessageActBox, invokerActivationBox, umlSD);
	  UMLSynchronousMessage syncMessage = new UMLSynchronousMessage("methodName", "ReturnType", new ArrayList<UMLMessageArgument>(), invokerActivationBox, receiverActivationBox, umlSD);
	  
	  UMLMessage messageInvoking = syncMessage.getInvokingMessage(umlSD);
	  UMLMessage messageNull = invokingMessage.getInvokingMessage(umlSD);
	  assertEquals( ((UMLSynchronousMessage) messageInvoking).getName(), invokingMessage.getName());
	  assertEquals(messageNull, null);
	  
	  // test with create message
	  
	  // test with return message (which has an override of this method)
  }
}
