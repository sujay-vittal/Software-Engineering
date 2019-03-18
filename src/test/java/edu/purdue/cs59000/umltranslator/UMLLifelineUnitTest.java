package edu.purdue.cs59000.umltranslator;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;



public class UMLLifelineUnitTest{
	
	
	@Test
	public void equalsTest() throws Exception {
		
		
		UMLLifeline umlLifeline1 = new UMLLifeline(new UMLClass("") {

			@Override
			public boolean equals(UMLSymbol other) {
				return false;
			}},
				new UMLSequenceDiagram());
		UMLLifeline umlLifeline2 = new UMLLifeline(new UMLClass("") {

			@Override
			public boolean equals(UMLSymbol other) {
				return true;
			}},
				new UMLSequenceDiagram());
		
		
		UMLLifeline umlLifeline3 = new UMLLifeline(new UMLClass("") {

			@Override
			public boolean equals(UMLSymbol other) {
				return true;
			}},
				new UMLSequenceDiagram());
		
		boolean areEqual = umlLifeline1.equals(umlLifeline2);
		boolean areEqual2 = umlLifeline3.equals(umlLifeline2);
		assertEquals(false, areEqual);
		assertEquals(true, areEqual2);
		
		UMLLifeline umlLifeline4 = new UMLLifeline(new UMLActor("","")  {

			@Override
			public boolean equals(UMLSymbol other) {
				return false;
			}},
				new UMLSequenceDiagram());
		UMLLifeline umlLifeline5 = new UMLLifeline(new UMLActor("","")  {

			@Override
			public boolean equals(UMLSymbol other) {
				return true;
			}},
				new UMLSequenceDiagram());
		
		
		UMLLifeline umlLifeline6 = new UMLLifeline(new UMLActor("","")  {

			@Override
			public boolean equals(UMLSymbol other) {
				return true;
			}},
				new UMLSequenceDiagram());
		
		boolean areEqual3 = umlLifeline4.equals(umlLifeline5);
		boolean areEqual4 = umlLifeline6.equals(umlLifeline5);
		assertEquals(false, areEqual3);
		assertEquals(true, areEqual4);
	}
	
	@Test
	public void exceptionTest() throws Exception {
		
		UMLLifeline umlLifeline1 = new UMLLifeline(new UMLClass("") {

			@Override
			public boolean equals(Object other) {
				return false;
			}},
				new UMLSequenceDiagram());
		
		Object o = new Object();
		Exception ee = null; 
		try {
			assertFalse(umlLifeline1.equals(o));
		}catch(Exception e) {
			ee = e;
		}
		
		assertNull(ee);
		
		UMLLifeline umlLifeline3 = new UMLLifeline(new UMLActor("","") {

			@Override
			public boolean equals(Object other) {
				return false;
			}},
				new UMLSequenceDiagram());
		
		Object o1 = new Object();
		Exception ee1 = null; 
		try {
			assertFalse(umlLifeline3.equals(o1));
		}catch(Exception e) {
			ee1 = e;
		}
		
		assertNull(ee1);
	
	}
	
	
	
	
	
}
