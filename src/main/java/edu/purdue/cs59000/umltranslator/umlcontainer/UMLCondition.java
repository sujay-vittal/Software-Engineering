package edu.purdue.cs59000.umltranslator.umlcontainer;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLCondition defines a condition upon which messages will be invoked. In an if-else statement, messages contained by a
 * UMLCondition will be invoked 'if' the condition is true. In a while loop statement, messages contained by a UMLCondition will
 * be invoked 'while' the condition is true. The condition is specified by a String object.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLCondition extends UMLContainer {
  private static UMLCodeHandler codeHandler;
  
  //condition (for example: x > 5, or check == true)
  private String condition = "";
  
  /**
   * Constructor for condition with the statement
   * @param condition
   */
  public UMLCondition (String condition) {
    setCondition (condition);
  }
  
  /**
   * Constructor for condition with the statement
   * Appends the UMLCondition to the given UMLSD
   * @param condition
   * @param umlSD
   */
  public UMLCondition (String condition, UMLSequenceDiagram umlSD) {
    this(condition);
    
    umlSD.addSymbol(this);
  }
  
  /**
   * Getter for condition
   * @return
   */
  public String getCondition() {
    return condition;
  }
  
  /**
   * Setter for condition
   * @param condition
   */
  public void setCondition (String condition) {
    this.condition = condition;
  }
  
  /*
   * (non-Javadoc)
   * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
   */
  public UMLCodeHandler getCodeHandler() {
    return UMLCondition.codeHandler;
  }
  
  public static void setCodeHandler(UMLCodeHandler codeHandler) {
    UMLCondition.codeHandler = codeHandler;
  }
  
  /**
   * Overrides default Object.equals(Object)
   */
  @Override
  public boolean equals(Object obj) {
	  //check not null
	  if(!super.equals(obj))
		  return false;
	  //check same object type
	  if(obj instanceof UMLCondition) {
      
      UMLCondition conObj = (UMLCondition) obj;
      
      //check condition
      if(getCondition() == null) {
    	  if(conObj.getCondition()==null)
    		  return true;
    	  else
    		  return false;
      } else {
        if (getCondition().equals(conObj.getCondition())) {
            return true;
        } else {
          return false;
        }    	  
      }
    } else { // not instanceof UMLCondition
      return false;
    }
  }

  @Override
  public boolean equals(UMLSymbol other) {
    return equals((Object) other);
  }
  
  public int hashCode(){
	  return this.condition.hashCode();
  }
}
