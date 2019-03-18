package edu.purdue.cs59000.umltranslator.umlcontainer;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLElse defines the 'else' component of an if-else statement. If a message is contained by a UMLElse object, the message will be
 * invoked if the 'if' condition is false.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLElse extends UMLContainer {
  private static UMLCodeHandler codeHandler;
  
  /**
   * Default Constructor
   */
  public UMLElse() {
    
  }
  
  /**
   * Constuctor that appends this UMLElse to the given UMLSD
   * @param umlSD
   */
  public UMLElse(UMLSequenceDiagram umlSD) {
    umlSD.addSymbol(this);
  }
  
  /*
   * (non-Javadoc)
   * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
   */
  public UMLCodeHandler getCodeHandler() {
    return UMLElse.codeHandler;
  }
  
  public static void setCodeHandler(UMLCodeHandler codeHandler) {
    UMLElse.codeHandler = codeHandler;
  }
  
  /**
   * Simple check that the two objects are of the same type
   */
  @Override
  public boolean equals(Object obj) {
	if(!super.equals(obj))
		return false;
    if (obj instanceof UMLElse) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean equals(UMLSymbol other) {
    return equals((Object) other);
  }
  
  public int hashcode(){
	  return 0;
  }
}
