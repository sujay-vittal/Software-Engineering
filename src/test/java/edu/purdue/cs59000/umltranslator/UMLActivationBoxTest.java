package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActivationBox;
import edu.purdue.cs59000.umltranslator.UMLActor;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;

public class UMLActivationBoxTest {
	UMLClass class1;
	UMLLifeline lifeline1;
	
	@Before
	public void initialize() throws Exception{
		class1 = new UMLClass("class");
		lifeline1 = new UMLLifeline(class1);
	}

	@Test
	public void testEqualsReturnsTrue() throws Exception{
		UMLActivationBox activationToTest = new UMLActivationBox(lifeline1);
		UMLActivationBox activationToTest2 = new UMLActivationBox(lifeline1);
		assertTrue(activationToTest.equals(activationToTest2));
	}
	
	@Test
	public void testDiffSourceEqualsReturnsFalse() throws Exception{
		UMLClass class2 = new UMLClass("class2");
		UMLLifeline lifeline2 = new UMLLifeline(class2);
		UMLActivationBox activationToTest = new UMLActivationBox(lifeline1);
		UMLActivationBox activationToTest2 = new UMLActivationBox(lifeline2);
		assertFalse(activationToTest.equals(activationToTest2));
	}
	
	@Test
	public void testEquivClassEqualsReturnsTrue() throws Exception{
		UMLClass class2 = new UMLClass("class");
		UMLLifeline lifeline2 = new UMLLifeline(class2);
		UMLActivationBox activationToTest = new UMLActivationBox(lifeline1);
		UMLActivationBox activationToTest2 = new UMLActivationBox(lifeline2);
		assertTrue(activationToTest.equals(activationToTest2));
	}
	
	@Test
	public void testInvalidObjectReturnsFalse(){
		Integer int1 = new Integer(0);
		assertFalse(class1.equals(int1));
	}
	@Test
	public void testSimpleEquals() throws UMLSDStructureException{
		UMLActivationBox activationToTest = new UMLActivationBox(lifeline1);
		UMLActivationBox activationToTest2 = new UMLActivationBox(lifeline1);
		assertTrue(activationToTest.simpleEquals(activationToTest2));
		
		UMLActor actor = new UMLActor("test","Test");
		UMLLifeline actorlifeline = new UMLLifeline(actor);
		activationToTest2 = new UMLActivationBox(actorlifeline);
		assertFalse(activationToTest.simpleEquals(activationToTest2));
		
		activationToTest = new UMLActivationBox(actorlifeline);
		assertTrue(activationToTest.simpleEquals(activationToTest2));
		
		UMLActor actor2 = new UMLActor("test","Test");
		UMLLifeline actor2lifeline = new UMLLifeline(actor2);
		activationToTest = new UMLActivationBox(actor2lifeline);
		assertTrue(activationToTest.simpleEquals(activationToTest2));
	}

	@Test
	public void testequals() throws UMLSDStructureException{
		UMLActivationBox activationToTest = new UMLActivationBox(lifeline1);
		UMLActivationBox activationToTest2 = new UMLActivationBox(lifeline1);
		assertTrue(activationToTest.equals(activationToTest2));
		
		UMLActor actor = new UMLActor("test","Test");
		UMLLifeline actorLifeline = new UMLLifeline(actor);
		activationToTest2 = new UMLActivationBox(actorLifeline);
		assertFalse(activationToTest.equals(activationToTest2));
		
		activationToTest = new UMLActivationBox(actorLifeline);
		assertTrue(activationToTest.equals(activationToTest2));
		
		UMLActor actor2 = new UMLActor("test","Test");
		UMLLifeline actorLifeline2 = new UMLLifeline(actor2);
		activationToTest = new UMLActivationBox(actorLifeline2);
		assertTrue(activationToTest.equals(activationToTest2));
	}

}
