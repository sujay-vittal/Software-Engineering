package edu.purdue.cs59000.umltranslator.exceptions;

import org.apache.log4j.Logger;

/**
 * UMLSequenceDiagramException is an Exception class that identifies issues with either the generation of a Java representation of
 * a user sequence diagram, a syntax error in the user sequence diagram, or an error in source code generation from a sequence
 * diagram representation object.
 * 
 * @author Stevey
 *
 */

public class UMLSequenceDiagramException extends Exception {
	
	final static Logger logger = Logger.getLogger( UMLSequenceDiagramException.class);	
	private static String errorMessage = "UMLSequenceDiagramException: Something is wrong with either the parsed sequence diagram or its Java representation."; 
  
	
  public UMLSequenceDiagramException() {
	  		
			super(errorMessage);
			logger.error(errorMessage);
			
  }
  
  public UMLSequenceDiagramException(String s) {
    super("UMLSequenceDiagramException: " + s);
  }
  
}
