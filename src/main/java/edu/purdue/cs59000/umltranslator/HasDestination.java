package edu.purdue.cs59000.umltranslator;

/**
 * HasDestination is an interface that specifies that a UMLSymbol has a destination. A UMLMessage would be an example of a UMLSymbol
 * that has a destination.
 * 
 * @author Steve Yarusinsky
 *
 */

public interface HasDestination {
	public UMLSymbol getDestination();
	public void setDestination(UMLSymbol umlSymbol);
}
