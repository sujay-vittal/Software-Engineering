package edu.purdue.cs59000.umltranslator.umlcontainer;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLAlternatives defines an if-else statement. It holds references to a UMLCondition (if) and a UMLElse (else) and also defines
 * their parent-child relationships upon setting. The child container of a UMLAlternative will be its UMLCondition.
 * 
 * @author Steve Yarusinsky
 *
 */
public class UMLAlternatives extends UMLContainer{
  private static UMLCodeHandler codeHandler;
  
  //represents an if/else relation
  private UMLCondition condition;
  private UMLElse umlElse;
  
  /**
   * Constructor setting the condition/else
   * @param condition
   * @param umlElse
   */
  public UMLAlternatives (UMLCondition condition, UMLElse umlElse) {
    setCondition (condition);
    setElse (umlElse);
  }
  
  /**
   * Constructor setting the condition/else, and appends the container to the given UMLSD
   * @param condition
   * @param umlElse
   * @param umlSD
   */
  public UMLAlternatives (UMLCondition condition, UMLElse umlElse, UMLSequenceDiagram umlSD) {
    this(condition, umlElse);
    
    umlSD.addSymbol(this);
  }
  
  /**
   * Getter for the condition
   * @return
   */
  public UMLCondition getCondition() {
    return condition;
  }
  
  /**
   * Setter for the condition
   * @param condition
   */
  public void setCondition (UMLCondition condition) {
    this.condition = condition;
    
    if(condition!=null)
    	condition.setParentContainer(this);
    
    this.setChildContainer(condition);
  }
  
  /**
   * Getter for else
   * @return
   */
  public UMLElse getElse() {
    return umlElse;
  }
  
  /**
   * Setter for else
   * @param umlElse
   */
  public void setElse (UMLElse umlElse) {
    this.umlElse = umlElse;
    
    if (umlElse != null)
    	umlElse.setParentContainer(this);
  }
  
  /*
   * (non-Javadoc)
   * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
   */
  public UMLCodeHandler getCodeHandler() {
    return UMLAlternatives.codeHandler;
  }
  
  public static void setCodeHandler(UMLCodeHandler codeHandler) {
    UMLAlternatives.codeHandler = codeHandler;
  }
  
  /**
   * Overrides default Object.equals(Object)
   */
  @Override
  public boolean equals(Object obj) {
	  //check not null
	  if(!super.equals(obj))
		  return false;
	  //check same type
	  if (!(obj instanceof UMLAlternatives))
		  return false;

	  UMLAlternatives altObj = (UMLAlternatives) obj;
	  //checks condition
	  if(getCondition()!=null)
	  {
		  if(!getCondition().equals(altObj.getCondition()))
			  return false;
	  }
	  else // getCondition is null
	  {
		  if(altObj.getCondition()!=null)
			  return false;
	  }
	  
	  if(getElse() != null)
	  {
		  if(!getElse().equals(altObj.getElse()))
			  return false;
	  }
	  else // getElse is null
	  {
		  if(altObj.getElse()!=null)
			  return false;
	  }

	  return true;
  }

  @Override
  public boolean equals(UMLSymbol other) {
    return equals((Object) other);
  }
  
  public int hashCode(){
	  return this.condition.hashCode() + this.umlElse.hashCode();
  }
}
