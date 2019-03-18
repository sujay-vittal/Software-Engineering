package edu.purdue.cs59000.umltranslator.message;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * UMLSynchronousMessage represents a synchronous message in a UML Sequence Diagram. It contains a method name 'name', a list
 * of arguments that method takes, and the return type of the method.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLSynchronousMessage extends UMLMessage{
    private static UMLCodeHandler codeHandler;
  
	private String name;
	private List<UMLMessageArgument> arguments = new ArrayList<UMLMessageArgument>();
	private String returnType;
  
	/**
	 * Default Constructor
	 * Sets name and returnType to empty strings
	 */
	public UMLSynchronousMessage() {
		this.name = "";
		this.returnType = "";
	}
	
	/**
	 * Constructor that initializes name, returnType, arguments, source, and destination
	 * @param name
	 * @param returnType
	 * @param arguments List<UMLMessageArgument>
	 * @param source UMLSymbol
	 * @param destination UMLSymbol
	 * @throws UMLSDStructureException
	 */
	public UMLSynchronousMessage(String name, String returnType, List<UMLMessageArgument> arguments, UMLSymbol source, UMLSymbol destination) throws UMLSDStructureException {
	  setName(name);
	  setReturnType(returnType);
	  addArguments(arguments);
	  setSource(source);
	  setDestination(destination);
	  
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
	 * Constructor that initializes name, returnType, arguments, source, and destination
	 * Also appends this message to the given UMLSD
	 * @param name
	 * @param returnType
	 * @param arguments List<UMLMessageArgument>
	 * @param source UMLSymbol
	 * @param destination UMLSymbol
	 * @param umlSD
	 * @throws UMLSDStructureException
	 */
	public UMLSynchronousMessage(String name, String returnType, List<UMLMessageArgument> arguments, UMLSymbol source, UMLSymbol destination, UMLSequenceDiagram umlSD) throws UMLSDStructureException {
      this(name, returnType, arguments, source, destination);
      
      umlSD.addSymbol(this); // add this symbol to the UMLSequenceDiagram
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
	 * getter for returnType
	 * @return
	 */
	public String getReturnType() {
		return returnType;
	}
	
	/**
	 * Setter for returnType
	 * @param returnType
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * Add an argument to internal list of arguments
	 * @param argument
	 */
	public void addArgument(UMLMessageArgument argument) {
		this.arguments.add(argument);
	}
	
	/**
	 * Add a list of arguments to internal list of arguments
	 * @param arguments
	 */
	public void addArguments(List<UMLMessageArgument> arguments) {
		this.arguments.addAll(arguments);
	}
	
	/**
	 * Getter for internal list of arguments
	 * @return
	 */
	public List<UMLMessageArgument> getArguments() {
		return arguments;
	}
	
	/**
	 * Helper method to get the return message associated with this synchronous message
	 * @param umlSD
	 * @return
	 * @throws UMLSDStructureException
	 */
	public UMLReturnMessage getReturnMessage(UMLSequenceDiagram umlSD) throws UMLSDStructureException {
		UMLMessage invokingMessage = this.getInvokingMessage(umlSD);
		List<UMLMessage> nestedMessages = invokingMessage.getNestedMessages();
		
		if (nestedMessages.size() > 0) {
			int indexOfThisMessage = Integer.MAX_VALUE;
			
			for (int i = 0; i < nestedMessages.size(); i++) {
				if (nestedMessages.get(i) == this) {
					indexOfThisMessage = i;
				}
			}
			
			if (indexOfThisMessage >= nestedMessages.size() - 1 || this.getReturnType().equals("void")) {
				return null;
			} else {
				UMLMessage nextMessage = nestedMessages.get(indexOfThisMessage + 1);
				
				if (nextMessage instanceof UMLReturnMessage) {
					return (UMLReturnMessage) nextMessage;
				} else {
					throw new UMLSDStructureException("Return type of synchronous message '" + getName() + "' is not void and doesn't have a return message.");
				}
			}
		} else {
			return null;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLSynchronousMessage.codeHandler;
	}
	
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLSynchronousMessage.codeHandler = codeHandler;
	}
	
	/**
	 * Override default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object o){
		//check not null
		if(!super.equals(o)) {
			return false;
		}
		//check equal class types
		if(!(o instanceof UMLSynchronousMessage)){
			return false;
		}
		UMLSynchronousMessage synchronousMessageToCompare = (UMLSynchronousMessage) o;
		//check name
		if(!this.name.equals(synchronousMessageToCompare.getName())){
			return false;
		}
		//check returnType
		if(!this.returnType.equals(synchronousMessageToCompare.getReturnType())){
			return false;
		}
		//check arguments list
		if(!this.arguments.equals(synchronousMessageToCompare.getArguments())){
			return false;
		}
		//check equal source types
		if(!((this.getSource() instanceof UMLActivationBox) 
				&& synchronousMessageToCompare.getSource() instanceof UMLActivationBox)){
			return false;
		}
		//check sources are equal
		if(!((UMLActivationBox)this.getSource()).simpleEquals(
				((UMLActivationBox)synchronousMessageToCompare.getSource()))){
			return false;
		}
		//check equal destination types
		if(!((this.getDestination() instanceof UMLActivationBox) 
				&& synchronousMessageToCompare.getDestination() instanceof UMLActivationBox)){
			return false;
		}
		//check destinations are equal
		if(!((UMLActivationBox)this.getDestination()).simpleEquals(
				((UMLActivationBox)synchronousMessageToCompare.getDestination()))){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * Hashcode method
	 */
	public int hashCode(){
		int argumentsCode = 0;
		for(UMLMessageArgument messageArgument : this.arguments){
			argumentsCode += messageArgument.hashCode();
		}
		return this.name.hashCode() + this.returnType.hashCode() + argumentsCode; 
	}
}
