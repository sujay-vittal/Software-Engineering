package edu.purdue.cs59000.umltranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Object that holds all UMLSymbols associated with a single UML Sequence Diagram
 * Has a field to ensure that UMLSymbol IDs generated will be unique
 */

public class UMLSequenceDiagram {
	//list of UMLSymbols associated with this UML Sequence Diagram
	private List<UMLSymbol> umlSymbols = new ArrayList<UMLSymbol>();
	/*highest UMLSymbol.ID in this diagram, must override UMLSymbol.ID (static) to ensure
		that symbols generated in a new run are not duplicate*/ 
	private int highest_unique_id = 0;
	
	/**
	 * Default Constructor
	 */
	public UMLSequenceDiagram()
	{
	}
	
	/**
	 * Add a symbol to this UMLSD
	 * @param umlSymbol any UMLSymbol
	 */
	public void addSymbol(UMLSymbol umlSymbol)
	{
		this.umlSymbols.add(umlSymbol);
		
		//update ID if necessary
		if(UMLSymbol.getCounter() > this.highest_unique_id){
			this.highest_unique_id = UMLSymbol.getCounter();
		}
	}
	
	/**
	 * Add a list of symbols to this UMLSD
	 * @param umlSymbols
	 */
	public void addSymbols(List<UMLSymbol> umlSymbols)
	{
	  for (UMLSymbol umlSymbol : umlSymbols) {
	    addSymbol(umlSymbol);
		
	    //update ID if necessary
	    if(UMLSymbol.getCounter() > this.highest_unique_id){
			this.highest_unique_id = UMLSymbol.getCounter();
		}
	  }
	}
	
	/**
	 * Getter for Highest Unique ID in this diagram, use to override UMLSymbol.ID
	 * @return
	 */
	public int getHighestUniqueID(){
		return this.highest_unique_id;
	}
	
	/**
	 * Getter for all UMLSymbols in this diagram
	 * @return
	 */
	public List<UMLSymbol> getUMLSymbols()
	{
		return umlSymbols;
	}
	
	/**
	 * Remove a symbol by symbol ID
	 * @param symbol_ID 
	 * @return removed symbol or null if not found
	 */
	public UMLSymbol removeSymbol(int symbol_ID)
	{
		UMLSymbol toReturn = null;
		//check to match symbol_id
		for(UMLSymbol symbol : this.umlSymbols){
			if(symbol.getId() == symbol_ID){
				toReturn = symbol;
				this.umlSymbols.remove(symbol);
				return toReturn;
			}
		}
		return toReturn;
	}
	
	/**
	 * Remove many symbols in an int[]
	 * @param symbol_IDs
	 * @return list of symbols that were removed
	 */
	public List<UMLSymbol> removeSymbols(int[] symbol_IDs)
	{
		ArrayList<UMLSymbol> toReturn = new ArrayList<UMLSymbol>();
		for(UMLSymbol symbol : this.umlSymbols){
			//slightly inefficient but not a problem if umlSymbol list < 100
			for(int i : symbol_IDs){
				if(symbol.getId() == i){
					toReturn.add(symbol);
					this.umlSymbols.remove(symbol);
					break;
				}
				//break if all symbols were found
				if(toReturn.size() == symbol_IDs.length){
					break;
				}
			}
		}
		return toReturn;
	}
}
