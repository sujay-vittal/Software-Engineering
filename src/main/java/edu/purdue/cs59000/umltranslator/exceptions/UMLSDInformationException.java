package edu.purdue.cs59000.umltranslator.exceptions;

import org.apache.log4j.Logger;

/**
 * UMLSDInformationException is a more specific Exception class than UMLSequenceDiagramException that identifies an issue with the 
 * information provided by the user sequence diagram. This error may be a syntax error, structural error, or other error with the 
 * user sequence diagram. 
 * 
 * @author Stevey
 *
 */

public class UMLSDInformationException extends UMLSequenceDiagramException {

	private static String errorString = "UMLSDInformationException: There is not enough information available in the sequence diagram to generate a representation or source code"; 
	
  public UMLSDInformationException() {
    super(errorString);
    logger.error(errorString);
  }
  
  public UMLSDInformationException(String s) {
    super("UMLSDInformationException: " + s);
  }
  
}
