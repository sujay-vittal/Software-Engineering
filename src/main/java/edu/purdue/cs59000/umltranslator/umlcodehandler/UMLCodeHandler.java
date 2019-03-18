package edu.purdue.cs59000.umltranslator.umlcodehandler;

import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;

import java.util.LinkedList;

import com.github.javaparser.ast.*;

/**
 * The UMLCodeHandler abstract class defines the methods that a specific UMLSymbol's code handler should have. Specifically, each UMLSymbol's code handler should
 * have a generateSourceCode method, that takes in a UMLSequenceDiagram, a UMLSymbol, and a list of Compilation Units. It uses the sequence diagram and symbol
 * information to place the source code in the correct locations in the AST representation(List of Compilation Units).
 * 
 * @author Steve Yarusinsky
 *
 */

public abstract class UMLCodeHandler {
  public abstract void generateSourceCode(UMLSequenceDiagram umlSD, UMLSymbol symbol, LinkedList<CompilationUnit> compUnits) throws UMLSDStructureException;
}
