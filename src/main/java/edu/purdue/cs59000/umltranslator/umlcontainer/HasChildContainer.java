package edu.purdue.cs59000.umltranslator.umlcontainer;

/**
 * HasChildContainer defines an interface that specifies the existence of a parent-child relationship between UMLContainer objects 
 * and defines methods to set and get the object's child container.
 * 
 * @author Steve Yarusinsky
 *
 */

public interface HasChildContainer {
	public UMLContainer getChildContainer();
	public void setChildContainer(UMLContainer umlContainer);
}
