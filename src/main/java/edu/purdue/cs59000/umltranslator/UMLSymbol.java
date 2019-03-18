package edu.purdue.cs59000.umltranslator;

import java.io.Serializable;

import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;

/**
 * UMLSymbol is an abstract class that all UML symbol representations inherit
 * from. They all have a source, which is used to access the class from which
 * messages are invoked or defined by.
 * 
 * @author Steve Yarusinsky
 *
 */

public abstract class UMLSymbol implements HasSource, HasCodeHandler, Serializable {
	//each UMLSymbol has an associated ID
	private int id;

	private boolean codeGenFlag = false;	// for efficiency when generating source code (ensures the symbol is only generating code once)
	
	//static int to count highest unique ID seen in this run
	protected static int counter = 0;

	//get the ID of a UMLSymbol
	public int getId() {
		return this.id;
	}
	
	/**
	 * Default constuctor
	 * Sets the id and increments the global counter
	 */
	public UMLSymbol(){
		this.id = counter;
		counter += 1;
	}
	
	/**
	 * Sets the global counter to the highest ID seen to ensure ID uniqueness
	 * @param max
	 * @return
	 */
	public static int setCounter(int max){
		counter = max;
		return max;
	}
	
	/**
	 * Getter for the global counter
	 * @return
	 */
	public static int getCounter(){
		return counter;
	}

	/**
	 * Default getsource() for all UMLSymbols
	 * Returns null for UMLClass/UMLActor
	 */
	public UMLSymbol getSource() { // if source is null, this means the UMLSymbol is a UMLClass or UMLActor.
									// Override for all other classes
		return null;
	}

	public void setSource(UMLSymbol symbol) throws UMLSDStructureException {
		// do nothing
	}

	/**
	 * Overrides Object.equals(Object)
	 * Checks that the objects are not null and are both UMLSymbols
	 */
	public boolean equals(Object other) {
		try {
			if (other == null)
				return false;
			if (!(other instanceof UMLSymbol))
				return false;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean equals(UMLSymbol other) {
		try {
			return equals((Object) other);
		} catch (Exception ex) {
			return false;
		}
	}
	

	public boolean isCodeGenFlagged() {
		return codeGenFlag;
	}
	
	public void setCodeGenFlag(boolean flag) {
		codeGenFlag = flag;
	}

}
