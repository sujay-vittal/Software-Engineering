package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;
import edu.purdue.cs59000.umltranslator.umlcodehandler.UMLCodeHandler;

/**
 * UMLLifeline represents a UML lifeline in a UML Sequence Diagram. 
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLLifeline extends UMLSymbol{
    private static UMLCodeHandler codeHandler;
    
    //source of the lifeline, must be a UMLClass or UMLActor
	private UMLSymbol source;

	/**
	 * Default constuctor, initializes source to null
	 */
	public UMLLifeline() {
		this.source = null;
	}
	
	/**
	 * Constructor that sets the source of the Lifeline
	 * @param source UMLClass or UMLActor
	 * @throws UMLSDStructureException
	 */
	public UMLLifeline(UMLSymbol source) throws UMLSDStructureException  {
		setSource(source);
	}
	
	/**
	 * Constructor that sets the source of the Lifeline
	 * Appends the Lifeline to the given UMLSD
	 * @param source UMLClass or UMLActor
	 * @param umlSD
	 * @throws UMLSDStructureException
	 */
	public UMLLifeline(UMLSymbol source, UMLSequenceDiagram umlSD) throws UMLSDStructureException {
      this(source);
      umlSD.addSymbol(this);
    }

	/**
	 * Getter for the source
	 */
	public UMLSymbol getSource() {
		return source;
	}

	/**
	 * Setter for the source, must be UMLClass or UMLActor
	 */
	public void setSource(UMLSymbol umlSymbol) throws UMLSDStructureException {
		if(umlSymbol instanceof UMLClass || umlSymbol instanceof UMLActor) {
			source = umlSymbol;
		} else {
			throw new UMLSDStructureException("Source must be of type UMLActor or UMLClass.");
		}
		
	}

	/**
	 * Overrides the default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object o) { 
		//check that object types match and sources match
		if(!simpleEquals(o)) {
			return false;
		}
		
		return true;
	}

	public boolean simpleEquals(Object other) {
		//check that other is not null
		if(!super.equals(other)) {
			return false;
		}
		
		//check that other is a UMLLifeline
		if(!(other instanceof UMLLifeline)) {
			return false;
		}
		UMLLifeline umlLifeline = (UMLLifeline)other;
		//check that source types match, then that they equal each other
		if(this.source instanceof UMLClass && umlLifeline.getSource() instanceof UMLClass){
			if(!((UMLClass)this.source).simpleEquals((UMLClass)umlLifeline.getSource())){
				return false;
			}
		}
		else if(this.source instanceof UMLActor && umlLifeline.getSource() instanceof UMLActor){
			if(!((UMLActor)this.source).equals((UMLActor)umlLifeline.getSource())){
				return false;
			}
		}
		else{
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see edu.purdue.cs59000.umltranslator.HasCodeHandler#getCodeHandler()
	 */
	public UMLCodeHandler getCodeHandler() {
	  return UMLLifeline.codeHandler;
	}
	
	public static void setCodeHandler(UMLCodeHandler codeHandler) {
	  UMLLifeline.codeHandler = codeHandler;
	}
	
	/**
	 * Hashcode method
	 */
	public int hashCode() {
		int sourceCode = 0;
		if(source != null) {		
			source.hashCode();
			
		}
				
		int codeHandlerCode = 0;		
		if(codeHandler !=null) {
			codeHandler.hashCode();
			
		}
		
	    return sourceCode+
	    		codeHandlerCode;   
	}
	
	
	
}
