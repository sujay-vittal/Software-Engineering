package edu.purdue.cs59000.umltranslator.umlcontainer;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLLoop defines a while loop. It holds a reference to a UMLCondition, which will define the condition in which the loop will
 * occur. It also defines their parent-child relationships.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLLoop extends UMLContainer{
  private static UMLCodeHandler codeHandler;
  
  //condition for the loop
  private UMLCondition condition;
  
  /**
   * Constructor that sets the condition of the loop
   * @param condition
   */
  public UMLLoop (UMLCondition condition) {
    setCondition (condition);
  }
  
  /**
   * Constructor that sets the condition of the loop and appends the UMLLoop to the given UMLSD
   * @param condition
   * @param umlSD
   */
  public UMLLoop (UMLCondition condition, UMLSequenceDiagram umlSD) {
    this(condition);
    
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
  
  /*
   * (non-Javadoc)
   * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
   */
  public UMLCodeHandler getCodeHandler() {
    return UMLLoop.codeHandler;
  }
  
  public static void setCodeHandler(UMLCodeHandler codeHandler) {
    UMLLoop.codeHandler = codeHandler;
  }
  
  /**
   * Overrides default Object.equals(Object)
   */
  @Override
	public boolean equals(Object other)	{	
		try	{
			//check not null
			if(!super.equals(other))
	  			return false;
      
			if(other==null)
				return false;
			//check equal object types
			if(!(other instanceof UMLLoop))
				return false;
			//checks that conditions are equal
			if(condition!=null) {
				if(!condition.equals(((UMLLoop)other).condition))
					return false;
			} else {
				if(((UMLLoop)other).condition!=null)
					return false;
			}
			return true;
		}
    catch(Exception ex)	{
			return false;
		}
	}
  	
	@Override
	public boolean equals(UMLSymbol other) {
		try	{
			return equals((Object)other);			
		}
		catch(Exception ex)	{
			return false;
		}
	}	
	
	public int hashCode(){
		return 0;
	}
}
