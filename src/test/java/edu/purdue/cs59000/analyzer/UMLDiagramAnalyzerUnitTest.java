package edu.purdue.cs59000.analyzer;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.UMLActor;
import edu.purdue.cs59000.umltranslator.UMLClass;
import edu.purdue.cs59000.umltranslator.UMLLifeline;
import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;
import edu.purdue.cs59000.umltranslator.message.UMLCreateMessage;
import edu.purdue.cs59000.umltranslator.message.UMLMessage;

public class UMLDiagramAnalyzerUnitTest {

	UMLSequenceDiagram diagram1, diagram2, diagram3;

	UMLSequenceDiagram diagram4, diagram5, diagram6;
	
	UMLDiagramAnalyzer analyzer;

	@Before
	public void testPrep() {

		diagram1 = new UMLSequenceDiagram();
		diagram1.addSymbol(new UMLActor("name1", "dataType1"));

		diagram2 = new UMLSequenceDiagram();
		diagram2.addSymbol(new UMLActor("name2", "dataType2"));

		diagram3 = new UMLSequenceDiagram();
		diagram3.addSymbol(new UMLActor("name1", "dataType1"));

		analyzer = new UMLDiagramAnalyzer();

	}

	@Test
	public void notEqualsUMLSymbols1() {

		HashMap<Integer, HashSet<UMLDiagramDifference>> map = analyzer.getUMLDiagramDifferences(diagram1, diagram2);

		Integer sizeOne = map.get(1).size();
		Integer sizeTwo = map.get(2).size();

		assertEquals(sizeOne, sizeTwo);

		ArrayList<UMLDiagramDifference> set1 = new ArrayList(map.get(1));
		ArrayList<UMLDiagramDifference> set2 = new ArrayList(map.get(2));

		assert (set1.get(0).getSymbol() instanceof UMLActor);

		assertEquals(((UMLActor) (set1.get(0).getSymbol())).getName(), "name1");

		assertEquals(((UMLActor) (set1.get(0).getSymbol())).getDataType(), "dataType1");

		assert (set2.get(0).getSymbol() instanceof UMLActor);

		assertEquals(((UMLActor) (set2.get(0).getSymbol())).getName(), "name2");

		assertEquals(((UMLActor) (set2.get(0).getSymbol())).getDataType(), "dataType2");

	}

	@Test
	public void testNoDifferenceUMLActor() {

		HashMap<Integer, HashSet<UMLDiagramDifference>> map = analyzer.getUMLDiagramDifferences(diagram1, diagram3);

		HashSet<UMLDiagramDifference> map1 = map.get(1);

		HashSet<UMLDiagramDifference> map2 = map.get(2);

		assertTrue(null == map1);
		assertTrue(null == map2);

	}
	
	
	
	@Test 
	public void testUMLClass() {
		

		
		diagram4 = new UMLSequenceDiagram();
		UMLClass c1 =  new UMLClass(); 
		c1.setClassName("c1");
		c1.setInstanceName("c1_instanceName");
		c1.addMessage(new UMLCreateMessage());
		diagram4.addSymbol(c1);
		
		
		diagram5 = new UMLSequenceDiagram();
		UMLClass c2 =  new UMLClass();
		c2.setClassName("c2");
		c2.setInstanceName("c2_instanceName");
		c2.addMessage(new UMLCreateMessage());
		diagram5.addSymbol(c2);

		HashMap<Integer, HashSet<UMLDiagramDifference>> map = analyzer.getUMLDiagramDifferences(diagram4, diagram5);
		
	
		ArrayList<UMLDiagramDifference> set1 = new ArrayList(map.get(1));
		ArrayList<UMLDiagramDifference> set2 = new ArrayList(map.get(2));
		
		assertTrue(null != set1);
		assertTrue(null != set2);
		
		assertEquals(((UMLClass) (set1.get(0).getSymbol())).getClassName(), "c1");
		assertEquals(((UMLClass) (set1.get(0).getSymbol())).getInstanceName(), "c1_instanceName");
		
		assertEquals(((UMLClass) (set2.get(0).getSymbol())).getClassName(), "c2");
		assertEquals(((UMLClass) (set2.get(0).getSymbol())).getInstanceName(), "c2_instanceName");
		
		
		
		
	}
	
	
	

}
