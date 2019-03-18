package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLClass;


public class UMLClassTest {	

	@Test
	public void testEqualsReturnsTrue() {
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		UMLClass classToTest2 = new UMLClass(className);
		classToTest2.setInstanceName("test1");
		assertTrue(classToTest.equals(classToTest2));
	}
	
	@Test
	public void testDiffClassNameReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		String className2 = "testClass2";
		UMLClass classToTest2 = new UMLClass(className2);
		classToTest2.setInstanceName("test1");
		assertFalse(classToTest.equals(classToTest2));
	}
	
	@Test
	public void testDiffInstanceNameReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		UMLClass classToTest2 = new UMLClass(className);
		classToTest2.setInstanceName("test2");
		assertFalse(classToTest.equals(classToTest2));
	}
	
	@Test
	public void testUnsetInstanceNameReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		UMLClass classToTest2 = new UMLClass(className);
		assertFalse(classToTest.equals(classToTest2));
	}
	
	@Test
	public void testClassNameConstructorEqualsReturnsTrue(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		UMLClass classToTest2 = new UMLClass(className);
		assertTrue(classToTest.equals(classToTest2));
	}
	
	@Test
	public void testInvalidObjectEqualsReturnsFalse(){
		Integer int1 = new Integer(0);
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		assertFalse(classToTest.equals(int1));
	}
	@Test
	public void testSimpleEqualsReturnTrue() {
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		UMLClass classToTest2 = new UMLClass(className);
		classToTest.setInstanceName("test1");
		classToTest2.setInstanceName("test1");
		assertTrue(classToTest.simpleEquals(classToTest2));
	}
		
	@Test
	public void testDiffClassNameSimpleEqualsReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		String className2 = "testClass2";
		UMLClass classToTest2 = new UMLClass(className2);
		classToTest2.setInstanceName("test1");
		assertFalse(classToTest.simpleEquals(classToTest2));
	}
	
	@Test
	public void testDiffInstanceNameSimpleEqualsReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		UMLClass classToTest2 = new UMLClass(className);
		classToTest2.setInstanceName("test2");
		assertFalse(classToTest.simpleEquals(classToTest2));
	}
	
	@Test
	public void testUnsetInstanceNameSimpleEqualsReturnsFalse(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		classToTest.setInstanceName("test1");
		UMLClass classToTest2 = new UMLClass(className);
		assertFalse(classToTest.simpleEquals(classToTest2));
	}
	
	@Test
	public void testClassNameConstructorSimpleEqualsReturnsTrue(){
		String className = "testClass";
		UMLClass classToTest = new UMLClass(className);
		UMLClass classToTest2 = new UMLClass(className);
		assertTrue(classToTest.simpleEquals(classToTest2));
	}

}
