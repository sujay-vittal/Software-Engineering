package edu.purdue.cs59000.umltranslator.umlcontainer;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.purdue.cs59000.umltranslator.*;

public class UMLAlternativesTest {

  @Test
  public void equalsTest() {
    UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
    UMLCondition cond1 = new UMLCondition("x > 5", umlSD);
    UMLElse else1 = new UMLElse(umlSD);
    UMLAlternatives alt1 = new UMLAlternatives(cond1, else1, umlSD);
    
    UMLAlternatives alt2 = new UMLAlternatives(cond1, else1, umlSD);
    
    assertTrue(alt1.equals(alt2));
    
    UMLCondition cond2 = new UMLCondition("x > 5", umlSD);
    UMLAlternatives alt3 = new UMLAlternatives(cond2, else1, umlSD);
    
    assertTrue(alt1.equals(alt3));
    
    UMLCondition cond3 = new UMLCondition("x < 5", umlSD);
    UMLAlternatives alt4 = new UMLAlternatives(cond3, else1, umlSD);
    
    assertFalse(alt1.equals(alt4));
    
    assertFalse(alt1.equals(else1));
  }

}
