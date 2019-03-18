package edu.purdue.cs59000.umltranslator.message;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLActor;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.message.UMLCreateMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessageArgument;

public class UMLCreateMessageTest {
	UMLCreateMessage createMessage1;
	
	@Before
	public void initialize() throws Exception{
		String name1 = "test";
		String dataType1 = "testClass";
		UMLClass class1 = new UMLClass(dataType1,name1);
		UMLActor source1 = new UMLActor("sourceTest","source1");
		UMLLifeline umlsource1lifeline = new UMLLifeline(source1);
		UMLActivationBox activationBox1 = new UMLActivationBox(umlsource1lifeline);
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		createMessage1 = new UMLCreateMessage("message1",class1,args,activationBox1);
	}

	@Test
	public void testEqualsReturnsTrue() throws Exception {
		String name1 = "test";
		String dataType1 = "testClass";
		UMLClass class1 = new UMLClass(dataType1,name1);
		UMLActor source1 = new UMLActor("sourceTest","source1");
		UMLLifeline umlsource1lifeline = new UMLLifeline(source1);
		UMLActivationBox activationBox1 = new UMLActivationBox(umlsource1lifeline);
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage createMessage2 = new UMLCreateMessage("message1",class1,args,activationBox1);
		assertTrue(createMessage1.equals(createMessage2));
	}
	
	@Test 
	public void testDifferentFieldsEqualsReturnsFalse() throws Exception{
		String name1 = "test";
		String dataType1 = "testClass";
		UMLClass class1 = new UMLClass(dataType1,name1);
		UMLActor source1 = new UMLActor("sourceTest","source1");
		UMLLifeline umlsource1lifeline = new UMLLifeline(source1);
		UMLActivationBox activationBox1 = new UMLActivationBox(umlsource1lifeline);
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage createMessage2 = new UMLCreateMessage("message2",class1,args,activationBox1);
		assertFalse(createMessage1.equals(createMessage2));
		
		
		class1 = new UMLClass("testClass1","test");
		createMessage2 = new UMLCreateMessage("message1",class1,args,activationBox1);
		assertFalse(createMessage1.equals(createMessage2));
		
		class1 = new UMLClass("testClass","test1");
		createMessage2 = new UMLCreateMessage("message1",class1,args,activationBox1);
		assertFalse(createMessage1.equals(createMessage2));
		
		source1 = new UMLActor("sourceTest","source2");
		umlsource1lifeline = new UMLLifeline(source1);
		activationBox1 = new UMLActivationBox(umlsource1lifeline);
		createMessage2 = new UMLCreateMessage("message2",class1,args,activationBox1);
		assertFalse(createMessage1.equals(createMessage2));
	}
	
	@Test
	public void testDifferentSourceEqualsReturnsFalse() throws Exception{
		String name1 = "test";
		String dataType1 = "testClass";
		UMLClass class1 = new UMLClass(dataType1,name1);
		UMLClass source2 = new UMLClass("testClass2","test2");
		UMLLifeline umlsource2lifeline = new UMLLifeline(source2);
		UMLActivationBox activationBox1 = new UMLActivationBox(umlsource2lifeline);
		ArrayList<UMLMessageArgument> args = new ArrayList<UMLMessageArgument>();
		UMLCreateMessage createMessage2 = new UMLCreateMessage("message2",class1,args,activationBox1);
		assertFalse(createMessage1.equals(createMessage2));
	}
	
	@Test
	public void testInvalidObjectEqualsReturnsFalse(){
		Integer int1 = new Integer(0);
		assertFalse(createMessage1.equals(int1));
	}

}
