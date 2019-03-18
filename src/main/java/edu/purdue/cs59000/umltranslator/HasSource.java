package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;

/**
 * HasSource is an interface that specifies that a UMLSymbol has a source. For a UMLSequenceDiagram, most UMLSymbols should have a
 * source in order for the source code generation to be capable of finding the class that a message belongs to or invokes that
 * message.
 * 
 * @author Steve Yarusinsky
 *
 */

public interface HasSource {
	public UMLSymbol getSource();
	public void setSource(UMLSymbol umlSymbol) throws UMLSDStructureException;
}
