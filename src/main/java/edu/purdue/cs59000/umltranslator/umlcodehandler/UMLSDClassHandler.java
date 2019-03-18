package edu.purdue.cs59000.umltranslator.umlcodehandler;

import edu.purdue.cs59000.umltranslator.*;

import java.util.LinkedList;
import java.util.Optional;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;

/**
 * The UMLSDClassHandler class defines the source code generation of a UMLClass object. Specifically, it checks to see if the correct class declaration exists
 * in the list of compilation units and if it does not it creates the class declaration and adds it to the list.
 * 
 * @author Steve Yarusinsky
 *
 */

public class UMLSDClassHandler extends UMLCodeHandler {
	public UMLSDClassHandler() {}
	
	public void generateSourceCode(UMLSequenceDiagram umlSD, UMLSymbol symbol, LinkedList<CompilationUnit> compUnits) {
		if (symbol instanceof UMLClass) {
			UMLClass classSymbol = (UMLClass) symbol;
			Optional<ClassOrInterfaceDeclaration> classDec = Optional.empty();
			
			// check if class exists
			for (CompilationUnit compUnit : compUnits) {
				classDec = compUnit.getClassByName(classSymbol.getClassName());
				if (classDec.isPresent()) {
					break;
				}
			}
			
			// if it doesn't create it
			if (!classDec.isPresent()) {
				CompilationUnit compilationUnit = new CompilationUnit();	// create compilation unit and add class
				compilationUnit.addClass(classSymbol.getClassName()).setPublic(true);
				
				compUnits.add(compilationUnit);	// add finished compilation unit to list of compilation units
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
}
