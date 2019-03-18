package edu.purdue.cs59000.umltranslator.message;

import edu.purdue.cs59000.umltranslator.UMLSymbol;

/**
 * A UMLMessageArgument is similar to a method parameter. Every method parameter has a name, 
 * a data type, an initial value, and a flag that determines whether or not the number of arguments
 * is specified (in Java this is ellipsis (…) ). 
 */
public class UMLMessageArgument {
	private String name;
	private String dataType;
	private String initializedTo;
	private boolean hasVarArgs;
	
	/** UMLMessageArgument Constructor 
	 *  
	 * @param name The name of the argument.
	 * @param dataType The data type of the argument.
	 * @param initializedTo The default/initial value of the argument.
	 * @param hasVarArgs If true, then the number of arguments will is not specified and arguments 
	 * 	may be of variable length.
	 */
	public UMLMessageArgument(String name, String dataType, String initializedTo, boolean hasVarArgs)
	{
		setName(name);
		setDataType(dataType);
		setInitializedTo(initializedTo);
		setHasVarArgs(hasVarArgs);
	}
	public UMLMessageArgument(String name, String dataType, String initializedTo)
	{
		this(name, dataType, initializedTo,false);
	}
	/**
	 * @return The name of the argument.
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name Sets the name of the argument.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return The data type of the argument.
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * 
	 * @param dataType Sets the data type of the argument.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * 
	 * @return The initial/default value of the argument.
	 */
	public String getInitializedTo() {
		return initializedTo;
	}
	/**
	 * 
	 * @param initializedTo Sets the initial/default value of the argument.
	 */
	public void setInitializedTo(String initializedTo) {
		this.initializedTo = initializedTo;
	}
	/**
	 * 
	 * @return If true, then the length of the argument may vary. Otherwise, the argument 
	 * length must be specified.  
	 */
	public boolean isHasVarArgs() {
		return hasVarArgs;
	}
	/**
	 * 
	 * @param hasVarArgs If true, then the length of the argument will be variable. Otherwise,
	 * the length must be specified.
	 */
	public void setHasVarArgs(boolean hasVarArgs) {
		this.hasVarArgs = hasVarArgs;
	}
	
	/**
	 * Override of default Object.equals(Object) method
	 */
	@Override
	public boolean equals(Object o)
	{
		try
		{
			if(o==null)
				return false;
			if(!(o instanceof UMLMessageArgument)){
				return false;
			}
			UMLMessageArgument other = (UMLMessageArgument) o;
			if(hasVarArgs!=other.isHasVarArgs())
				return false;
			if(!name.equals(other.getName()))
				return false;
			if(!initializedTo.equals(other.getInitializedTo()))
				return false;
			if(!dataType.equals(other.getDataType()))
				return false;
						
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	/**
	 * Hashcode method
	 */
	public int hashCode(){
		if(this.hasVarArgs){
			return this.name.hashCode() + this.initializedTo.hashCode() + this.dataType.hashCode() + 1;
		}
		return this.name.hashCode() + this.initializedTo.hashCode() + this.dataType.hashCode();
	}
	
	
	
}
