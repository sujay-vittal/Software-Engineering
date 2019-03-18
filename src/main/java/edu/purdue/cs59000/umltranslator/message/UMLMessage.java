package edu.purdue.cs59000.umltranslator.message;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.*;
import edu.purdue.cs59000.umltranslator.umlcontainer.*;

/**
 * UMLMessage is an abstract class that all UML message types inherit from. All UMLMessages have a source and a destination. They
 * also have the capability of being contained by a UMLContainer, which will define logic in source code generation. Two helper
 * methods (getInvoker and getReciever) should be used heavily in source code generation to find the classes that either invoke
 * the messages or define the message signatures. In addition, a message invocation can set off a chain of message invocations.
 * Because of this, it is the parser's responsibility to add the nested messages in the correct order specified by the sequence
 * diagram.
 * 
 * @author Steve Yarusinsky
 *
 */

public abstract class UMLMessage extends UMLSymbol implements HasSource, HasDestination, HasParentContainer{
	//all messages contain a source and destination
	//some may be in a container (control logic) or contain nested messages
	private UMLSymbol source;
	private UMLSymbol destination;
	private UMLContainer container;
	private List<UMLMessage> nestedMessages = new ArrayList<UMLMessage>();
	
	//helper method to get the Class or Actor calling this message
	public UMLClass getInvoker() throws UMLSDStructureException {
	    UMLSymbol temp = this.getSource();
	    
	    while (temp.getSource() != null) {     // follow sources to reach UMLClass
	      temp = temp.getSource();
	    }
	    
	    //return if invoker is a class
	    if (temp instanceof UMLClass) {
	      return (UMLClass) temp;
	    } else if (temp instanceof UMLActor) {	//do nothing if invoker is an actor
	      return null;
	    } else {
	      throw new UMLSDStructureException("Could not get invoking class. Error in Java representation structure.");
	    }
	}
	
	//helper method to get the Class that receives this message
	public UMLClass getReceiver() throws UMLSDStructureException {
		UMLSymbol temp = this.getDestination();
		//follow sources until a class is reached
		while (temp.getSource() != null) {
		  temp = temp.getSource();
		}
		
		if (temp instanceof UMLClass) {
		  return (UMLClass) temp;
		} else if (temp instanceof UMLActor) {
		      return null;
		} else {
		  throw new UMLSDStructureException("Could not get receiving class. Error in Java representation structure.");
		}
	}
	
	/**
	 * Getter for internal NestedMessages 
	 * @return
	 */
	public List<UMLMessage> getNestedMessages() {
		return nestedMessages;
	}
	
	/**
	 * Add a message to internal NestedMessages structure
	 * @param message
	 */
	public void addNestedMessage(UMLMessage message) {    // nested messages should be added by the parser in the sequence they are invoked
	    nestedMessages.add(message);
	}
	
	/**
	 * Add any number of messages to internal NestedMessages structure
	 * @param messages
	 */
	public void addNestedMessages(UMLMessage... messages) {
	    for (UMLMessage message : messages) {
	      nestedMessages.add(message);
	    }
	}
	
	/**
	 * Add a list of UMLMessages to the internal NestedMessages structure
	 * @param messages
	 */
	public void addNestedMessages(List<UMLMessage> messages) {
	    nestedMessages.addAll(messages);
	}
	
	/**
	 * Getter for destination of message
	 */
	public UMLSymbol getDestination() {
	    return destination;
	}
	
	/**
	 * Setter for destination of message
	 */
	public void setDestination(UMLSymbol umlSymbol) {
		destination = umlSymbol;
	}
	
	/**
	 * Getter for source
	 */
	public UMLSymbol getSource() {
		return source;
	}
	
	/**
	 * Setter for source
	 * Source must a UMLActivationBox, UMLCondition or UMLElse
	 */
	public void setSource(UMLSymbol umlSymbol) throws UMLSDStructureException {
		if (umlSymbol instanceof UMLActivationBox
				|| umlSymbol instanceof UMLCondition
				|| umlSymbol instanceof UMLElse) {
			source = umlSymbol;
		} else {
			throw new UMLSDStructureException("Source must be of type UMLActivationBox, UMLCondition, or UMLElse.");
		}
	}
	
	/**
	 * Getter for parent container
	 */
	public UMLContainer getParentContainer() {
		return container;
	}
	
	/**
	 * Setter for parent container
	 */
	public void setParentContainer(UMLContainer umlContainer) {
	    container = umlContainer;
	}
	
	/**
	 * Override default Object.equals(Object)
	 */
	public boolean equals(Object other)
	{
		try
		{
			//check not null
			if(!super.equals(other)) {
				return false;
			}
			//check proper class type
			if(!(other instanceof UMLMessage)) {
				return false;
			}
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}




	/**
	 * This method gets the message that invokes the current message. For example, if a message 'walkForward' in one class invoked a message
	 * 'pickUpRightFoot' in another class, 'getInvokingMessage' called on the message 'pickUpRightFoot' would return 'walkForward'. If no 
	 * invoking message is found, null is returned meaning that it lies in the client's main method.
	 * @param umlSD
	 * @return
	 * @throws UMLSDStructureException
	 */
	public UMLMessage getInvokingMessage(UMLSequenceDiagram umlSD) throws UMLSDStructureException {	
		UMLSymbol thisMessageSource = this.getSource();
		UMLMessage invokingMessage = null;
		
		if (thisMessageSource instanceof UMLActivationBox) {
			UMLActivationBox thisMessageActBox = (UMLActivationBox) thisMessageSource;
			
			for (UMLSymbol symbol : umlSD.getUMLSymbols()) {
				if ( symbol instanceof UMLMessage && ( ((UMLMessage) symbol).getDestination().equals(thisMessageActBox) || ((UMLMessage) symbol).getDestination().equals(this.getInvoker()) ) && !(symbol instanceof UMLReturnMessage) ) {
					invokingMessage = (UMLMessage) symbol;
					if (invokingMessage.getDestination().equals(thisMessageActBox)) {
						break;
					}
				} 
			}
			
			return invokingMessage;
		} else {
			return null;
		}
	}

	/**
	 * Hashcode method
	 */
  	public int hashCode() {
	    
	
		int umlMessagesHash = 0;
		for(UMLMessage message: nestedMessages) {
			umlMessagesHash = umlMessagesHash + message.hashCode();
		}
		
		int destinationValue = 0;
		if(destination != null) {
			destinationValue = destination.hashCode();
		}
		int containerValue = 0; 
		if(container != null )
		{
			containerValue = container.hashCode();
		}
		
		int sourceValue = 0; 
		if(source != null) {
			
			sourceValue = source.hashCode();
		}
		
		
		
		return sourceValue + 
				destinationValue+
				containerValue+ 
				umlMessagesHash;

	}
	
  
  
}
