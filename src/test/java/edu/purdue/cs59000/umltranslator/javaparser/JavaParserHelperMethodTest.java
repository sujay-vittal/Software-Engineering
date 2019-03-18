package edu.purdue.cs59000.umltranslator.javaparser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.javaparser.ast.expr.Expression;

public class JavaParserHelperMethodTest {

	@Test
	public void parseExpressionTest() {
		String simpleBinaryExpressionExpected = "x > 5";
		Expression simpleBinaryExpressionActual = JavaParserHelperMethod.parseExpression(simpleBinaryExpressionExpected);
		assertEquals(simpleBinaryExpressionExpected, simpleBinaryExpressionActual.toString());
		
		String complexBinaryExpressionExpected = "x < Object.getSomeValue()";
		Expression complexBinaryExpressionActual = JavaParserHelperMethod.parseExpression(complexBinaryExpressionExpected);
		assertEquals(complexBinaryExpressionExpected, complexBinaryExpressionActual.toString());
		
		String moreComplexBinaryExpressionExpected = "someObject.getObject().getOtherObject() == someOtherObject.getObject()";
		Expression moreComplexBinaryExpressionActual = JavaParserHelperMethod.parseExpression(moreComplexBinaryExpressionExpected);
		assertEquals(moreComplexBinaryExpressionExpected, moreComplexBinaryExpressionActual.toString());
		
		String simpleUnaryExpressionExpected = "someBooleanValue";
		Expression simpleUnaryExpressionActual = JavaParserHelperMethod.parseExpression(simpleUnaryExpressionExpected);
		assertEquals(simpleUnaryExpressionExpected, simpleUnaryExpressionActual.toString());
		
		String complexUnaryExpressionExpected = "someMethodThatReturnsABool()";
		Expression complexUnaryExpressionActual = JavaParserHelperMethod.parseExpression(complexUnaryExpressionExpected);
		assertEquals(complexUnaryExpressionExpected, complexUnaryExpressionActual.toString());
		
		String moreComplexUnaryExpressionExpected = "someMethodThatReturnsAnObject().someMethodThatReturnsABool()";
		Expression moreComplexUnaryExpressionActual = JavaParserHelperMethod.parseExpression(moreComplexUnaryExpressionExpected);
		assertEquals(moreComplexUnaryExpressionExpected, moreComplexUnaryExpressionActual.toString());
	}

}
