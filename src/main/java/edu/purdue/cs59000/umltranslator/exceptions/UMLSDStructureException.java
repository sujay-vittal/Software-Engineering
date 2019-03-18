package edu.purdue.cs59000.umltranslator.exceptions;

/**
 * UMLSDStructureException is a more specific Exception class than UMLSequenceDiagramException that identifies an issue with the 
 * structure of the Java representation of a sequence diagram. 
 * 
 * @author Stevey
 *
 */

public class UMLSDStructureException extends UMLSequenceDiagramException {

	private static String exceptionString = "UMLSDStructureException: Something is wrong with the structure of the Java representation of the sequence diagram.";
	
	public UMLSDStructureException() {
		super(exceptionString);
		logger.error(exceptionString);
  }
  
  public UMLSDStructureException(String s) {
    super("UMLSDStructureException: " + s);
  }
  
}
