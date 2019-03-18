package edu.purdue.cs59000.umltranslator.message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.cs59000.umltranslator.exceptions.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;
import edu.purdue.cs59000.umltranslator.*;

/**
 * UMLCreateMessage defines a message invocation that creates an object. Its return type should always be the class name of the 
 * object being created. Its name should always be the instance name of the object created. It can also take a list of arguments 
 * that will be passed to the constructor upon the objects creation.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLCreateMessage extends UMLMessage {
    private static UMLCodeHandler codeHandler;
    
    //a UMLCreateMessage has a name, list of arguments, returnType, and associated class
	private String name;
	private List<UMLMessageArgument> arguments = new ArrayList<UMLMessageArgument>();
	private String returnType;
	private UMLClass umlClass;
	
	/**
	 * Default constructor initializes fields to empty strings/null
	 */
	public UMLCreateMessage()
	{
		this.name="";
		this.returnType="";
		this.umlClass=null;
	}
	
	/**
	 * Constructor that sets all of the fields of the UMLCreateMessage
	 * @param name String
	 * @param umlClass UMLClass
	 * @param arguments List<UMLMessageArguments>
	 * @param source UMLSymbol
	 * @throws UMLSDStructureException
	 */
	public UMLCreateMessage(String name, UMLClass umlClass, List<UMLMessageArgument> arguments, UMLSymbol source) throws UMLSDStructureException {
	  setName(name);
	  setReturnType(umlClass.getClassName());
	  setUMLClass(umlClass);
	  addArguments(arguments);
	  setSource(source);
	  setDestination(umlClass);
	  
	  try {    // add this message to invoker UMLClass's list of messages
		  if (getInvoker() != null) {
			  getInvoker().addMessage(this);
		  }
	  } catch (UMLSDStructureException e) {
	    System.out.println(e.getMessage());    // replace this to add to error log file instead of printing to console
	  }
	  
	  try { // add this message to receiver UMLClass's list of messages
		  if (getReceiver() != null) {
			  getReceiver().addMessage(this);
		  }
	  } catch (UMLSDStructureException e) {
	    System.out.println(e.getMessage());    // replace this to add to error log file instead of printing to console
	  }
	}
	
	/**
	 * Constructor that sets all of the fields and appends the message to the given UMLSD
	 * @param name String
	 * @param umlClass UMLClass
	 * @param arguments List<UMLMessageArguments>
	 * @param source UMLSymbol
	 * @param umlSD
	 * @throws UMLSDStructureException
	 */
	public UMLCreateMessage(String name, UMLClass umlClass, List<UMLMessageArgument> arguments, UMLSymbol source, UMLSequenceDiagram umlSD) throws UMLSDStructureException  {
      this(name, umlClass, arguments, source);
      
      umlSD.addSymbol(this); // add this symbol to the UMLSequenceDiagram
    }
	
	/**
	 * Getter for the associated UMLClass
	 * @return
	 */
	public UMLClass getUMLClass() {
		return umlClass;
	}
	
	/**
	 * Setter for the associated UMLClass
	 * @param umlClass
	 */
	public void setUMLClass(UMLClass umlClass) {
		this.umlClass = umlClass;
		setDestination(umlClass);
	}
	
	/**
	 * Getter for name
	 * @return
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
	 * Getter for ReturnType
	 * @return
	 */
	public String getReturnType() {
		return returnType;
	}
	
	/**
	 * Setter for ReturnType
	 * @param returnType
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * Append an argument to the internal list of arguments
	 * @param argument
	 */
	public void addArgument(UMLMessageArgument argument)
	{
		arguments.add(argument);
	}
	
	/**
	 * Append a list of arguments to the internal list of arguments
	 * @param arguments
	 */
	public void addArguments(List<UMLMessageArgument> arguments)
	{
		for (UMLMessageArgument argument : arguments) {
			this.arguments.add(argument);
		}
	}
	
	/**
	 * Getter for the internal list of arguments
	 * @return
	 */
	public List<UMLMessageArgument> getArguments() {
		return arguments;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLCreateMessage.codeHandler;
	}
	
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLCreateMessage.codeHandler = codeHandler;
	}
	
	/**
	 * Override of default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object o){
		//check non null
		if(!super.equals(o)) {
			return false;
		}
		//check correct class 
		if(!(o instanceof UMLCreateMessage)){
			return false;
		}
		UMLCreateMessage createMessageToCompare = (UMLCreateMessage) o;
		//check names
		if(!this.name.equals(createMessageToCompare.getName())){
			return false;
		}
		//check return types
		if(!this.returnType.equals(createMessageToCompare.getReturnType())){
			return false;
		}
		//shallow check same class
		if(!this.umlClass.simpleEquals(createMessageToCompare.getUMLClass())){
			return false;
		}
		//check same arguments
		if(!this.arguments.equals(createMessageToCompare.getArguments())){
			return false;
		}
		//check same source type
		if(!((this.getSource() instanceof UMLActivationBox) && createMessageToCompare.getSource() instanceof UMLActivationBox)){
			return false;
		}
		//shallow check sources
		if(!((UMLActivationBox)this.getSource()).simpleEquals(((UMLActivationBox)createMessageToCompare.getSource()))){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	/**
	 * hashcode method
	 */
	public int hashCode(){
		return this.name.hashCode() + this.returnType.hashCode() + this.umlClass.hashCode();
	}
}
