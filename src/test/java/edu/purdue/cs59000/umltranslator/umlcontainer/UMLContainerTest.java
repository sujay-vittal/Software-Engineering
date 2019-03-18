package edu.purdue.cs59000.umltranslator.umlcontainer;

import static org.junit.Assert.*;
import edu.purdue.cs59000.umltranslator.*;

import org.junit.Test;

public class UMLContainerTest {
	
	/**
	 * Varies the properties of UMLLoop and confirms that equality returns as expected.
	 * @throws Exception
	 */
	@Test
	public void equalsTest() throws Exception {
		UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
		UMLContainer umlContainer1 = new UMLLoop(new UMLCondition("x != y", umlSD), umlSD);
		UMLContainer umlContainer2 = new UMLLoop(new UMLCondition("x != y", umlSD), umlSD);
		UMLContainer umlContainer3 = new UMLLoop(new UMLCondition("x != y", umlSD), umlSD);
		
		assertTrue(umlContainer1.equals(umlContainer2)); // check expected result
		assertEqualsTest(umlContainer1,umlContainer2,umlContainer3);
		
		umlContainer2.setChildContainer(new UMLCondition("x == y", umlSD));
		assertFalse(umlContainer1.equals(umlContainer2)); // check expected result
		assertEqualsTest(umlContainer1,umlContainer2,umlContainer3);
		
		umlContainer2.setChildContainer(null);
		assertFalse(((UMLContainer)umlContainer1).equals(umlContainer2)); // check expected result
		assertEqualsTest(umlContainer1,umlContainer2,umlContainer3);		
		
		umlContainer1.setChildContainer(umlContainer2.getChildContainer());
		assertTrue(((UMLContainer)umlContainer1).equals(umlContainer2)); // check expected result
		assertEqualsTest(umlContainer1,umlContainer2,umlContainer3);

		
		// verify that IllegalArgumentException is detected
		Object o = new Object();
		Exception ee = null; 
		try {
			assertFalse(umlContainer1.equals(o));
		}catch(Exception e) {
			ee = e;
		}
		assertNull(ee);
		
		// verify that IllegalArgumentException is detected
		Exception ee2 = null;
		UMLSymbol umlSymbol = new UMLActor("1","2", new UMLSequenceDiagram());
		try {
			assertFalse(umlContainer1.equals(umlSymbol));
		}catch(Exception e) {
			ee2 = e;
		}
		assertNull(ee2);
	}
	public void assertEqualsTest(UMLContainer... objs) throws Exception
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
	private void assertReflexive(UMLContainer obj) throws Exception
	{
		if(obj!=null)
			assertTrue(obj.equals(obj));
	}
	// symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true. 
	private void assertSymmetric(UMLContainer obj1,UMLContainer obj2) throws Exception
	{
		if( obj1!=null && obj2!=null)
			assertEquals(obj1.equals(obj2),obj2.equals(obj1));
	}
	// transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true. 
	private void assertTransitive(UMLContainer obj1,UMLContainer obj2, UMLContainer obj3) throws Exception
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
	private void assertNullHandling(UMLContainer obj) throws Exception
	{
		if(obj!=null)
			assertFalse(obj.equals((UMLContainer)null));
		if(obj!=null)
			assertFalse(obj.equals((Object)null));
		if(obj!=null)
			assertFalse(obj.equals((UMLSymbol)null));
	}
}
