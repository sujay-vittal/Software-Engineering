package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLActivationBox defines a representation of an activation box in a UML Sequence Diagram. All messages should be sourced from
 * UMLActivationBoxes and their destinations should also be UMLActivationBoxes.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLActivationBox extends UMLSymbol {
    private static UMLCodeHandler codeHandler;
    
    //Source symbol for the activation Box. Must be a lifeline belonging to a class or actor or another activation box
	private UMLSymbol source;
	
	/**
	 * Constructor that sets the source of the Activation Box
	 * @param source - a UMLLifeline corresponding to a UMLClass or UMLActor or UMLActivationBox
	 * @throws UMLSDStructureException
	 */
	public UMLActivationBox(UMLSymbol source) throws UMLSDStructureException {
		setSource(source);
	}
	
	/**
	 * Default Constructor
	 */
	public UMLActivationBox() {
		this.source = null;
	}
	
	/**
	 * Constructor that sets the source as well as appends the Activation Box to the given UMLSequence Diagram
	 * @param source
	 * @param umlSD
	 * @throws UMLSDStructureException
	 */
	public UMLActivationBox(UMLSymbol source, UMLSequenceDiagram umlSD) throws UMLSDStructureException {
		this(source);
		
		umlSD.addSymbol(this);
	}

	/**
	 * returns the source of the Activation Box
	 */
	public UMLSymbol getSource() {
		return source;
	}

	/**
	 * set the source of the activation box
	 * throws an exception if the source is not a lifeline or activation box
	 */
	public void setSource(UMLSymbol umlSymbol) throws UMLSDStructureException {
		if(umlSymbol instanceof UMLLifeline || umlSymbol instanceof UMLActivationBox) {
			source = umlSymbol;
		} else {
			throw new UMLSDStructureException("Source must be of type UMLLifeline or UMLActivationBox.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLActivationBox.codeHandler;
	}
	
	/**
	 * set the code handler for the activation box
	 * @param codeHandler
	 */
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLActivationBox.codeHandler = codeHandler;
	}
	
	/**
	 * Override of the Object.equals(Object) method
	 * Returns true if the object is of the same class and the source is equals
	 */
	@Override
	public boolean equals(Object o){
		
		//check if the two objects are of the same class
		if(!simpleEquals(o)) {
			return false;
		}
		
		UMLActivationBox activationBoxToCompare = (UMLActivationBox) o;
		
		//checks if the two sources are equals
		if(!this.source.equals(activationBoxToCompare.getSource())){
			return false;
		}
		return true;
	}
	
	/**
	 * SimpleEquals helper to ensure no infinite loops are reachable
	 * Only call this equals from the equals of another object
	 */
	public boolean simpleEquals(Object o){
		
		//check object type
		if(!super.equals(o)) {
			return false;
		}
		if(!(o instanceof UMLActivationBox)){
			return false;
		}

		UMLActivationBox activationBoxToCompare = (UMLActivationBox) o;
		
		//checks type of source
		if(this.source instanceof UMLLifeline && activationBoxToCompare.getSource() instanceof UMLLifeline){
			//if sources are both lifelines, check if they are equal
			if(!((UMLLifeline)this.source).simpleEquals((UMLLifeline)activationBoxToCompare.getSource())){
				return false;
			}
		}
		//checks type of source
		else if(this.source instanceof UMLActivationBox && activationBoxToCompare.getSource() instanceof UMLActivationBox){
			//if sources are both activation boxes, check if they are equal
			if(!((UMLLifeline)this.source).simpleEquals((UMLLifeline)activationBoxToCompare.getSource())){
				return false;
			}
		}
		//if source types are not equal, return false
		else{
			return false;
		}
		return true;
	}
	
	
	// Returns 0 due to circular reference 
	//  class -> message -> activation -> lifeline -> class
	public int hashCode() {
	    return 0;   
	}
	

	
	
	
	
}
