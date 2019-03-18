package edu.purdue.cs59000.umltranslator.umlcontainer;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.purdue.cs59000.umltranslator.*;

public class UMLConditionTest {

  @Test
  public void equalsTest() {
    UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
    UMLCondition cond1 = new UMLCondition("x > 5", umlSD);
    UMLCondition cond2 = new UMLCondition("x > 5", umlSD);
    UMLCondition cond3 = new UMLCondition("x < 5", umlSD);
    UMLElse else1 = new UMLElse(umlSD);
    
    assertTrue(cond1.equals(cond2));
    assertFalse(cond1.equals(cond3));
    assertFalse(cond1.equals(else1));
  }

}
