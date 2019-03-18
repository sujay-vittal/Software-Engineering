package edu.purdue.cs59000.umltranslator.umlcodehandler;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSDStructureException;

import java.util.LinkedList;

import com.github.javaparser.ast.*;

/**
 * This class is a simple controller class that iterates through each UMLSymbol in a UMLSequenceDiagram and calls its respective code handler to generate source
 * code. A list of compilation units are taken in, presumably with or without existing code, and the list of compilation units is then 'updated' by the source
 * code generation process, ensuring that all classes, method signatures, and method invocation lie in their correct location in the AST representation. In
 * addition, the generateSourceCode method ensures each uml symbol's code handler is only called once on each instance of the uml symbol. This helps to improve
 * efficiency.
 * 
 * @author Steve Yarusinsky
 *
 */

public class SequenceDiagramCodeController {
	public static void generateSourceCode(UMLSequenceDiagram umlSD, LinkedList<CompilationUnit> compUnits) throws UMLSDStructureException {
		for (UMLSymbol symbol : umlSD.getUMLSymbols()) {	// generating source code from each UMLSymbol
			if (symbol.getCodeHandler() != null && !symbol.isCodeGenFlagged()) {
				symbol.getCodeHandler().generateSourceCode(umlSD, symbol, compUnits);
				symbol.setCodeGenFlag(true);
			}
		}
		
		for (UMLSymbol symbol : umlSD.getUMLSymbols()) {	// returning the codeGenFlags back to false, for the next time generateSourceCode is called
			symbol.setCodeGenFlag(false);
		}
	}
}
