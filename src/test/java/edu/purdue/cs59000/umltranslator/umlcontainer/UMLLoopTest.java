package edu.purdue.cs59000.umltranslator.umlcontainer;

import static org.junit.Assert.*;


import edu.purdue.cs59000.umltranslator.*;

import org.junit.Test;

public class UMLLoopTest {
	
	/**
	 * Varies the properties of UMLLoop and confirms that equality returns as expected.
	 * @throws Exception
	 */
	@Test
	public void equalsTest() throws Exception {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		UMLLoop umlLoop1 = new UMLLoop(new UMLCondition("x == y", umlSD), umlSD);
		UMLLoop umlLoop2 = new UMLLoop(new UMLCondition("x == y", umlSD), umlSD);
		UMLLoop umlLoop3 = new UMLLoop(new UMLCondition("x == y", umlSD), umlSD);
		assertTrue(umlLoop1.equals(umlLoop2)); // check expected result
		assertEqualsTest(umlLoop1,umlLoop2,umlLoop3);
		
		umlLoop2.setCondition(new UMLCondition("x > y",umlSD));
		assertFalse(umlLoop1.equals(umlLoop2)); // check expected result
		assertEqualsTest(umlLoop1,umlLoop2,umlLoop3); // confirm expected equals behavior
		
		umlLoop2.setCondition(new UMLCondition("x == y",umlSD));
		assertTrue(umlLoop1.equals(umlLoop2)); // check expected result
		assertEqualsTest(umlLoop1,umlLoop2,umlLoop3); // confirm expected equals behavior

		umlLoop2.setCondition(null);
		assertFalse(umlLoop1.equals(umlLoop2)); // check expected result
		assertEqualsTest(umlLoop1,umlLoop2,umlLoop3); // confirm expected equals behavior

		umlLoop1.setCondition(null);
		assertTrue(umlLoop1.equals(umlLoop2)); // check expected result
		assertEqualsTest(umlLoop1,umlLoop2,umlLoop3); // confirm expected equals behavior
		
		// verify that IllegalArgumentException is detected
		Object o = new Object();
		Exception ee = null; 
		try {
			assertFalse(umlLoop1.equals(o));
		}catch(Exception e) {
			ee = e;
		}
		assertNull(ee);
		
		// verify that IllegalArgumentException is detected
		Exception ee2 = null;
		UMLActor umlSymbol = new UMLActor("1","2", new UMLSequenceDiagram());
		try {
			assertFalse(umlLoop1.equals(umlSymbol));
		}catch(Exception e) {
			ee2 = e;
		}
		assertNull(ee2);
	}
	public void assertEqualsTest(UMLLoop... objs) throws Exception
	{
		int msgCount = objs.length;
		
		for(int i=0;i < msgCount; i++)
		{
			assertReflexive(objs[i]);
			assertNullHandling(objs[i]);
			if(i>0)
				assertSymmetric(objs[i-1],objs[i]);

			if(i>1)
				assertTransitive(objs[i-2],objs[i-1],objs[i]);
		}
	}
	// reflexive: for any non-null reference value x, x.equals(x) should return true. 
	private void assertReflexive(UMLLoop obj) throws Exception
	{
		if(obj!=null)
			assertTrue(obj.equals(obj));
	}
	// symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true. 
	private void assertSymmetric(UMLLoop obj1,UMLLoop obj2) throws Exception
	{
		if( obj1!=null && obj2!=null)
			assertEquals(obj1.equals(obj2),obj2.equals(obj1));
	}
	// transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true. 
	private void assertTransitive(UMLLoop obj1,UMLLoop obj2, UMLLoop obj3) throws Exception
	{
		if(obj1 != null && obj2 != null && obj3 != null)
		{
			if(obj1.equals(obj2) && obj2.equals(obj3))
			{
				assertTrue(obj1.equals(obj3));
			}
		}
	}
	// For any non-null reference value x, x.equals(null) should return false. 
	private void assertNullHandling(UMLLoop obj) throws Exception
	{
		if(obj!=null)
			assertFalse(obj.equals((UMLLoop)null));
		if(obj!=null)
			assertFalse(obj.equals((Object)null));
		if(obj!=null)
			assertFalse(obj.equals((UMLSymbol)null));
	}
}
