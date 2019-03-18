/**
 * 
 */
package edu.purdue.cs59000.umltranslator.message;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.purdue.cs59000.umltranslator.*;

/**
 * @author Aaron Muir
 *
 */
public class UMLMessageArgumentTest {

	/**
	 * Varies the properties of UMLMessageArgument and confirms that equality returns as expected.
	 * @throws Exception
	 */
	@Test
	public void equalsTest() throws Exception {
		
		UMLMessageArgument msgArg1 = new UMLMessageArgument("name1","datatype1","initVal",false);
		UMLMessageArgument msgArg2 = new UMLMessageArgument("name1","datatype1","initVal",false);
		UMLMessageArgument msgArg3 = new UMLMessageArgument("name1","datatype1","initVal",false);
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		// vary the datatype
		msgArg2.setDataType("datatype2");
		assertFalse(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		msgArg2.setDataType("datatype1");
		assertTrue(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		// vary the name
		msgArg2.setName("name2");
		assertFalse(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		msgArg2.setName("name1");
		assertTrue(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		// vary the initial value
		msgArg2.setInitializedTo("initVal2");
		assertFalse(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		msgArg2.setInitializedTo("initVal");
		assertTrue(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		// vary the variable arguments flag
		msgArg2.setHasVarArgs(true);
		assertFalse(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		msgArg2.setHasVarArgs(false);
		assertTrue(msgArg1.equals(msgArg2));
		assertEqualsTest(msgArg1,msgArg2,msgArg3);
		
		// verify that IllegalArgumentException is detected
		Object o = new Object();
		Exception ee = null; 
		try {
			assertFalse(msgArg1.equals(o));
		}catch(Exception e) {
			ee = e;
		}
		assertNull(ee);
		
		// verify that IllegalArgumentException is detected
		Exception ee2 = null;
		UMLSymbol umlSymbol = new UMLActor("1","2", new UMLSequenceDiagram());
		try {
			assertFalse(msgArg1.equals(umlSymbol));
		}catch(Exception e) {
			ee2 = e;
		}
		assertNull(ee2);
	}
	
	public void assertEqualsTest(UMLMessageArgument... msgArgs) throws Exception
	{
		int msgCount = msgArgs.length;
		
		for(int i=0;i < msgCount; i++)
		{
			assertReflexive(msgArgs[i]);
			assertNullHandling(msgArgs[i]);
			if(i>0)
				assertSymmetric(msgArgs[i-1],msgArgs[i]);

			if(i>1)
				assertTransitive(msgArgs[i-2],msgArgs[i-1],msgArgs[i]);
		}
	}
	// reflexive: for any non-null reference value x, x.equals(x) should return true. 
	private void assertReflexive(UMLMessageArgument msgArg) throws Exception
	{
		if(msgArg!=null)
			assertTrue(msgArg.equals(msgArg));
	}
	// symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true. 
	private void assertSymmetric(UMLMessageArgument msgArg1,UMLMessageArgument msgArg2) throws Exception
	{
		if( msgArg1!=null && msgArg2!=null)
			assertEquals(msgArg1.equals(msgArg2),msgArg2.equals(msgArg1));
	}
	// transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true. 
	private void assertTransitive(UMLMessageArgument msgArg1,UMLMessageArgument msgArg2, UMLMessageArgument msgArg3) throws Exception
	{
		if(msgArg1 != null && msgArg2 != null && msgArg3 != null)
		{
			if(msgArg1.equals(msgArg2) && msgArg2.equals(msgArg3))
			{
				assertTrue(msgArg1.equals(msgArg3));
			}
		}
	}
	// For any non-null reference value x, x.equals(null) should return false. 
	private void assertNullHandling(UMLMessageArgument msgArg) throws Exception
	{
		if(msgArg!=null)
			assertFalse(msgArg.equals((UMLMessageArgument)null));
		if(msgArg!=null)
			assertFalse(msgArg.equals((Object)null));
		if(msgArg!=null)
			assertFalse(msgArg.equals((UMLSymbol)null));
	}
}
