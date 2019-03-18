package edu.purdue.cs59000.umltranslator.umlcontainer;

/**
 * HasParentContainer defines an interface that specifies that an object is being contained by a UMLContainer object. It defines
 * methods for setting and getting an object's parent container.
 * 
 * @author Steve Yarusinsky
 *
 */

public interface HasParentContainer {
	public UMLContainer getParentContainer();
	public void setParentContainer(UMLContainer umlContainer);
}
