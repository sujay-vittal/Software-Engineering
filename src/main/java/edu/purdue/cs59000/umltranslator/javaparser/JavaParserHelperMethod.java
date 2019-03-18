package edu.purdue.cs59000.umltranslator.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.*;

/**
 * The JavaParserHelperMethod class contains several static methods that can be used as helper methods when dealing with code that utilized JavaParser.
 * 
 * @author Steve Yarusinsky
 *
 */

public class JavaParserHelperMethod {
	
	/**
	 * The simpleStringToType method takes a string which represents a data type, and returns its corresponding JavaParser type representation.
	 * 
	 * @param dataType
	 * @return
	 */
	public static Type simpleStringToType(String dataType) {
		if (dataType == "boolean") {
			return PrimitiveType.booleanType();
		} else if (dataType == "byte") {
			return PrimitiveType.byteType();
		} else if (dataType == "char") {
			return PrimitiveType.charType();
		} else if (dataType == "short") {
			return PrimitiveType.shortType();
		} else if (dataType == "int") {
			return PrimitiveType.intType();
		} else if (dataType == "long") {
			return PrimitiveType.longType();
		} else if (dataType == "float") {
			return PrimitiveType.floatType();
		} else if (dataType == "double") {
			return PrimitiveType.doubleType();
		} else if (dataType.trim().substring(dataType.length() - 2) == "[]"){
			AnnotationExpr[] annotations = new AnnotationExpr[0];
			return new ArrayType( simpleStringToType(dataType.trim().substring(0, dataType.length() - 3)), annotations );
		} else {
			return new ClassOrInterfaceType(null, dataType);
		}
	}
	
	/**
	 * The parseExpression method is a wrapper method to ensure Expressions are generated correctly for use in logic statements. Previously some complex expressions
	 * were unable to be parsed by JavaParser.parseExpression, however this seems to be fixed. If issues arise in creating expressions again, this method should
	 * be modified to generate expressions correctly.
	 * 
	 * @param expression
	 * @return
	 */
	public static Expression parseExpression(String expression) {
		return JavaParser.parseExpression(expression);
	}
}
