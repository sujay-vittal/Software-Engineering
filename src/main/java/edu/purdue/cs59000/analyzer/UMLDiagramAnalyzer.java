package edu.purdue.cs59000.analyzer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;

public class UMLDiagramAnalyzer {

	final static Logger logger = Logger.getLogger(UMLDiagramAnalyzer.class);

	
	// Returns a Map of what each sets have that the other does not
	public HashMap<Integer, HashSet<UMLDiagramDifference>> getUMLDiagramDifferences(UMLSequenceDiagram diagram1,
			UMLSequenceDiagram diagram2) {

		HashMap<Integer, HashSet<UMLDiagramDifference>> map = new HashMap<Integer, HashSet<UMLDiagramDifference>>();

		HashSet<UMLDiagramDifference> diagram1Differences = symbolsDifferences(diagram2, diagram1);
		HashSet<UMLDiagramDifference> diagram2Differences = symbolsDifferences(diagram1, diagram2);
		map.put(1, diagram1Differences);
		map.put(2, diagram2Differences);

		return map;

	}

	private HashSet<UMLDiagramDifference> symbolsDifferences(UMLSequenceDiagram diagram1,
			UMLSequenceDiagram diagram2) {

		HashSet<UMLDiagramDifference> diagram1Differences = new HashSet<UMLDiagramDifference>();

		HashSet<UMLSymbol> symbols1 = new HashSet(diagram1.getUMLSymbols());

		for (UMLSymbol umlSymbol : diagram2.getUMLSymbols()) {
			if (symbols1.add(umlSymbol)) {
				logger.debug("Symbol not found in diagram1: " + umlSymbol);
				diagram1Differences.add(new UMLDiagramDifference(umlSymbol));
			}

		}

		diagram1Differences = (diagram1Differences.size() == 0) ? null : diagram1Differences;

		return diagram1Differences;

	}

}
