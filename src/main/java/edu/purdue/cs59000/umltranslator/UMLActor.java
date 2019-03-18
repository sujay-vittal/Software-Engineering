package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLActor represents an actor in a UML Sequence Diagram. In source code generation, it will be treated as a class, with 'dataType'
 * being the class name and 'name' being the instance name. By default its source is null, meaning it is the end of a source tree
 * and therefore a class.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLActor extends UMLSymbol {
    private static UMLCodeHandler codeHandler;
  
    //an actor contains a name and dataType (user:User)
	private String name;
	private String dataType;
	
	/**
	 * Default Constructor, set both to empty strings
	 */
	public UMLActor()
	{
		this.name="";
		this.dataType="";
	}
	
	/**
	 * Constructor to set the name of an Actor
	 * DataType is left as an empty string
	 * @param name
	 */
	public UMLActor(String name)
	{
		this.dataType ="";
		setName(name);
	}
	
	/**
	 * Constructor to set both the name and dataType of the Actor
	 * @param name
	 * @param dataType
	 */
	public UMLActor(String name,String dataType) {
		setName(name);
		setDataType(dataType);
	}
	
	/**
	 * Constructor that sets the name and dataType of the Actor, as well as appends the Actor to the give UMLSD
	 * @param name
	 * @param dataType
	 * @param umlSD
	 */
	public UMLActor(String name,String dataType, UMLSequenceDiagram umlSD) {
	  this(name, dataType);
      
      umlSD.addSymbol(this);
  }

	/**
	 * Getter for name
	 * @return name:String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for dataType
	 * @return dataType:String
	 */
	public String getDataType()
	{
		return dataType;
	}
	
	/**
	 * Setter for dataType
	 * @param dataType
	 */
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLActor.codeHandler;
	}

	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLActor.codeHandler = codeHandler;
	}
	
	/**
	 * Overrides default Object.equals(Object)
	 * Checks that Object o is of type UMLActor, then compares the name and dataType fields
	 */
	@Override
	public boolean equals(Object o){
		if(!(o instanceof UMLActor)){
			return false;
		}
		UMLActor actorToCompare = (UMLActor) o;
		if(!this.name.equals(actorToCompare.getName())){
			return false;
		}
		if(!this.dataType.equals(actorToCompare.getDataType())){
			return false;
		}
		return true;
	}
	
	/**
	 * HashCode function for UMLActors
	 */
	public int hashCode() {
	    return this.name.hashCode() +
	    		this.dataType.hashCode();         
	}
	
	
	
	
}
