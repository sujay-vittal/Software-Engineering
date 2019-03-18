package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActor;

public class UMLActorTest {
	UMLActor actor1;
	
	@Before
	public void initialize(){
		String name = "actor";
		String dataType = "actorTest";
		actor1 = new UMLActor(name, dataType);
	}
	
	@Test
	public void testEqualsReturnsTrue() {
		UMLActor actor2 = new UMLActor("actor","actorTest");
		assertTrue(actor1.equals(actor2));
	}
	
	@Test
	public void testDifferentFieldsReturnsFalse(){
		UMLActor actor2 = new UMLActor("actor1","actorTest");
		assertFalse(actor1.equals(actor2));
		actor2.setName("actor");
		actor2.setDataType("actorTest1");
		assertFalse(actor1.equals(actor2));
	}
	
	@Test
	public void testInvalidObjectReturnsFalse(){
		Integer int1 = new Integer(0);
		assertFalse(actor1.equals(int1));
	}

}
