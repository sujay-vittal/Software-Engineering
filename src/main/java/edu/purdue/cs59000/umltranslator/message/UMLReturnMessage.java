package edu.purdue.cs59000.umltranslator.message;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.*;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;
/**
 * UMLReturnMessage defines the return from a synchronous message. Its returnName defines the variable name of the returned object or
 * primitive.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLReturnMessage extends UMLMessage{
    public static UMLCodeHandler codeHandler;
    
	private String returnName;
	
	/**
	 * Default constructor that initializes returnName to an empty String
	 */
	public UMLReturnMessage()
	{
		this.returnName = "";
	}
	
	/**
	 * Constructor that sets returnName, source, and destination
	 * @param returnName
	 * @param source UMLActivationBox
	 * @param destination UMLActivationBox
	 * @throws UMLSDStructureException
	 */
	public UMLReturnMessage(String returnName, UMLSymbol source, UMLSymbol destination) throws UMLSDStructureException  {
	  setReturnName(returnName);
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
	 * Constructor that sets returnName, source, and destination
	 * Also appends this message to the given UMLSD
	 * @param returnName
	 * @param source UMLActivationBox
	 * @param destination UMLActivationBox
	 * @param umlSD
	 * @throws UMLSDStructureException
	 */
	public UMLReturnMessage(String returnName, UMLSymbol source, UMLSymbol destination, UMLSequenceDiagram umlSD) throws UMLSDStructureException {
      this(returnName, source, destination);
      
      umlSD.addSymbol(this); // add this symbol to the UMLSequenceDiagram
    }

	/**
	 * Getter for ReturnName
	 * @return
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * Setter for returnName
	 * @param returnName
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLReturnMessage.codeHandler;
	}
	
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLReturnMessage.codeHandler = codeHandler;
	}
	
	/**
	 * Overrides default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object o){
		//check no null
		if(!super.equals(o)) {
			return false;
		}
		//check same object type
		if(!(o instanceof UMLReturnMessage)){
			return false;
		}
		UMLReturnMessage returnMessageToCompare = (UMLReturnMessage) o;
		//check return name
		if(!this.returnName.equals(returnMessageToCompare.getReturnName())){
			return false;
		}
		//check sources are both activation boxes
		if(!((this.getSource() instanceof UMLActivationBox) 
				&& returnMessageToCompare.getSource() instanceof UMLActivationBox)){
			return false;
		}
		//compare sources
		if(!((UMLActivationBox)this.getSource()).simpleEquals(
				((UMLActivationBox)returnMessageToCompare.getSource()))){
			return false;
		}
		//check destinations are both activation boxes
		if(!((this.getDestination() instanceof UMLActivationBox) 
				&& returnMessageToCompare.getDestination() instanceof UMLActivationBox)){
			return false;
		}
		//compare destinations
		if(!((UMLActivationBox)this.getDestination()).simpleEquals(
				((UMLActivationBox)returnMessageToCompare.getDestination()))){
			return false;
		}
		return true;
	}
	
	@Override
	/**
	 * @inheritDoc
	 */
	public UMLMessage getInvokingMessage(UMLSequenceDiagram umlSD) throws UMLSDStructureException {	
		UMLSymbol thisMessageSource = this.getDestination();
		UMLMessage invokingMessage = null;
		
		if (thisMessageSource instanceof UMLActivationBox) {
			UMLActivationBox thisMessageActBox = (UMLActivationBox) thisMessageSource;
			
			for (UMLSymbol symbol : umlSD.getUMLSymbols()) {
				if ( symbol instanceof UMLMessage && ( ((UMLMessage) symbol).getDestination().equals(thisMessageActBox) || ((UMLMessage) symbol).getDestination().equals(this.getInvoker()) ) ) {
					invokingMessage = (UMLMessage) symbol;
					break;
				} 
			}
			
			return invokingMessage;
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return getReturnName();
	}
	
	public int hashCode(){
		return this.returnName.hashCode() + this.getSource().hashCode() + this.getDestination().hashCode();
	}
}
