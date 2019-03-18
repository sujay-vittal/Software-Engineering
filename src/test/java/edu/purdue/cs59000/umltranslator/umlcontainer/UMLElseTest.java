package edu.purdue.cs59000.umltranslator.umlcontainer;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.purdue.cs59000.umltranslator.*;

public class UMLElseTest {

  @Test
  public void equalsTest() {
    UMLSequenceDiagram umlSD = new UMLSequenceDiagram();
    UMLElse else1 = new UMLElse(umlSD);
    UMLElse else2 = new UMLElse(umlSD);
    UMLCondition cond1 = new UMLCondition("x > 5", umlSD);
    
    assertTrue(else1.equals(else2));
    assertFalse(else1.equals(cond1));
  }

}
