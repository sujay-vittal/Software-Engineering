package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.message.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;
import java.util.List;
import java.util.LinkedList;


/**
 * UMLClass represents a class and/or instance in a UML Sequence Diagram. It also holds a reference to a list of messages that it
 * either invokes or defines. By default its source is null, meaning it is the end of a source tree and therefore a class.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLClass extends UMLSymbol {
    private static UMLCodeHandler codeHandler;
    
    //a class contains a classname and instancename (class:Class)
	private String className;
	private String instanceName;
	
	//also contains a list of messages associated with this class
	private List<UMLMessage> messages = new LinkedList<UMLMessage>();
	
	/**
	 * Default Constructor, initializes classname and instancename to empty strings
	 */
	public UMLClass() {
		this.className = "";
		this.instanceName = "";
	}
	
	/**
	 * Constructor that sets the classname of a UMLClass
	 * @param className
	 */
	public UMLClass(String className) {
	  setClassName(className);
	  this.instanceName = "";
	}
	
	/**
	 * Sets the classname as well as appends the UMLCLass to the given UMLSD
	 * @param className
	 * @param umlSD
	 */
	public UMLClass(String className, UMLSequenceDiagram umlSD) {
      this(className);
      
      umlSD.addSymbol(this);
  }

	/**
	 * Sets the classname and instancename of the UMLClass
	 * @param className
	 * @param instanceName
	 */
	public UMLClass(String className, String instanceName) {
		this(className);
		setInstanceName(instanceName);
	}
	
	/**
	 * Constructor that sets the classname and instancename of the UMLClass
	 * Also appends the UMLClass to the given UMLSD
	 * @param className
	 * @param instanceName
	 * @param umlSD
	 */
	public UMLClass(String className, String instanceName, UMLSequenceDiagram umlSD) {
      this(className, instanceName);
      
      umlSD.addSymbol(this);
  }

	/**
	 * Getter for classname
	 * @return className:String
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Setter for classname
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Getter for instancename
	 * @return instanceName:String
	 */
	public String getInstanceName() {
		return instanceName;
	}
	
	/**
	 * Setter for instancename
	 * @param instanceName
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	/**
	 * Getter for associated UMLMessages
	 * @return messages:List<UMLMessage>
	 */
	public List<UMLMessage> getMessages() {
		return messages;
	}
	
	/**
	 * adds a UMLMessage to the internal list of UMLMessages
	 * @param message
	 */
	public void addMessage(UMLMessage message) {
	    messages.add(message);
	}
	
	/**
	 * Adds any number of UMLMessages to the internal list of UMLMessages
	 * @param messages
	 */
	public void addMessages(UMLMessage... messages) {
	  for (UMLMessage message : messages) {
	    this.messages.add(message);
	  }
	}
	
	/**
	 * Adds a List<UMLMessage> to the internal list of UMLMessages
	 * @param messages
	 */
	public void addMessages(List<UMLMessage> messages) {
	    this.messages.addAll(messages);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLClass.codeHandler;
	}
	
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLClass.codeHandler = codeHandler;
	}
	
	/**
	 * Overrides the default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object o){

		//check that object types are the same, as well as classname/instancename
		if(!simpleEquals(o)) {
			return false;
		}
		
		UMLClass classToCompare = (UMLClass) o;

		//checks that the messages associated with each UMLClass are equal
		if(!this.messages.equals(classToCompare.getMessages())){
			return false;
		}		
		return true;
	}
	
	/**
	 * Method to provide a shallow equals of two UMLClasses
	 * @param o - Will return false if Object o is not a UMLClass
	 * @return
	 */
	public boolean simpleEquals(Object o){
		//check that o is not null and is a UMLSymbol
		if(!super.equals(o)) {
			return false;
		}
		//check that o is a UMLClass
		if(!(o instanceof UMLClass)){
			return false;
		}
		
		//compare the string fields of the two objects
		UMLClass classToCompare = (UMLClass) o;
		if(!this.className.equals(classToCompare.getClassName())){
			return false;
		}
		if(!this.instanceName.equals(classToCompare.getInstanceName())){
			return false;
		}
		return true;
	}
	
	/**
	 * Hashcode method for a UMLCLass
	 */
	public int hashCode() {
		
		return className.hashCode() + 
	    		instanceName.hashCode();  
	}
	
	
}
