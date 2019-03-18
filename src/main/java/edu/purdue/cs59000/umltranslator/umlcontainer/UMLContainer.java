package edu.purdue.cs59000.umltranslator.umlcontainer;
import edu.purdue.cs59000.umltranslator.*;

/**
 * UMLContainer is an abstract class that all UML conditional containers inherit from. Through its parent-child relationship
 * nesting multiple loops or other conditionals is possible. The parent relationships should be considerably more important than
 * the child relationships, as the conditionals in the source code should be generated by accessing a message object, accessing
 * the parent container of that message object, accessing the parent container of that container object, and so on.
 * 
 * @author Steve Yarusinsky
 *
 */
public abstract class UMLContainer extends UMLSymbol implements HasParentContainer, HasChildContainer{
	private UMLContainer parentContainer;
	private UMLContainer childContainer;
	
	/**
	 * Getter for child container
	 */
	public UMLContainer getChildContainer() {
	  return childContainer;
	}
	
	/**
	 * Setter for child container
	 */
	public void setChildContainer(UMLContainer umlContainer) {
	  childContainer = umlContainer;
	}
	
	/**
	 * Getter for parent container
	 */
	public UMLContainer getParentContainer() {
	  return parentContainer;
	}
	
	/**
	 * Setter for parent container
	 */
	public void setParentContainer(UMLContainer umlContainer) {
	  parentContainer = umlContainer;
	}

	/**
	 * Overrides default Object.equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try
		{	//check not null
			if(!super.equals(obj))
				return false;
			if(obj == null)
				return false;
			if (!(obj instanceof UMLContainer))
				return false;
			
			UMLContainer altObj = (UMLContainer) obj;
			
			if(getChildContainer()!=null)
			{
				if (!getChildContainer().equals(altObj.getChildContainer()))
					return false;
			}
			else
			{
				if(altObj.getChildContainer()!=null)
					return false;
			}
			
			// no parent container equality check to avoid circular references
				
			return true;			
		}
		catch(Exception ex)
		{
			return false;
		}
	}

	@Override
	public boolean equals(UMLSymbol other) {
		try
		{	
			return equals((Object)other); 			
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	public int hashCode() {
	    return parentContainer.hashCode() + 
	    		childContainer.hashCode();
	}
	
	
	
}
