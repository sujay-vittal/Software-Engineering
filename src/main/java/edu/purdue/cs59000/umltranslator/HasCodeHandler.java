package edu.purdue.cs59000.umltranslator;

import edu.purdue.cs59000.umltranslator.umlcodehandler.*;
/**
 * Interface for Code generation
 * All classes that generate code implement this interface
 * */
public interface HasCodeHandler {
  public UMLCodeHandler getCodeHandler();
}
