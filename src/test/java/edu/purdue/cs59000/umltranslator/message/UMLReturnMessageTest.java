package edu.purdue.cs59000.umltranslator.message;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;
import edu.purdue.cs59000.umltranslator.message.UMLReturnMessage;
import edu.purdue.cs59000.umltranslator.message.UMLSynchronousMessage;

public class UMLReturnMessageTest {
	UMLReturnMessage returnMessage;
	UMLActivationBox source;
	UMLActivationBox dest;
	
	@Before
	public void initialize() throws Exception{
		String returnName = "ret";
		UMLClass sourceClass = new UMLClass("source","Source");
		UMLLifeline sourceLifeline = new UMLLifeline(sourceClass);
		UMLClass destClass = new UMLClass("dest","Dest");
		UMLLifeline destLifeline = new UMLLifeline(destClass);
		source = new UMLActivationBox(sourceLifeline);
		dest = new UMLActivationBox(destLifeline);
		returnMessage = new UMLReturnMessage(returnName,source,dest);
	}
	
	@Test
	public void testEqualsReturnsTrue() throws Exception {
		UMLReturnMessage returnMessage2 = new UMLReturnMessage("ret",source,dest);
		assertTrue(returnMessage.equals(returnMessage2));
	}
	
	@Test
	public void testDifferentFieldsEqualsReturnsFalse() throws Exception{
		UMLReturnMessage returnMessage2 = new UMLReturnMessage("ret2",source,dest);
		assertFalse(returnMessage.equals(returnMessage2));
	}
	
	@Test
	public void testDifferentSourcesEqualsReturnsFalse() throws Exception{
		UMLClass sourceClass = new UMLClass("source1","Source");
		UMLLifeline sourceLifeline = new UMLLifeline(sourceClass);
		source = new UMLActivationBox(sourceLifeline);
		UMLReturnMessage returnMessage2 = new UMLReturnMessage("ret",source,dest);
		assertFalse(returnMessage.equals(returnMessage2));
	}
	
	@Test
	public void testDifferentDestinationsEqualsReturnsFalse() throws Exception{
		UMLClass destClass = new UMLClass("dest1","Dest");
		UMLLifeline destLifeline = new UMLLifeline(destClass);
		dest = new UMLActivationBox(destLifeline);
		UMLReturnMessage returnMessage2 = new UMLReturnMessage("ret",source,dest);
		assertFalse(returnMessage.equals(returnMessage2));
	}
	
	@Test
	public void testInvalidObjectEqualsReturnsFalse(){
		Integer int1 = new Integer(0);
		assertFalse(returnMessage.equals(int1));
	}

}
