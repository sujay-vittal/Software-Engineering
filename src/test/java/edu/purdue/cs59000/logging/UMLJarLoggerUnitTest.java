/*UMLJarLogggerUnitTest
 *  
 * Orginal Author: Matthew Hruskocy
 */
package edu.purdue.cs59000.logging;

import org.apache.log4j.Logger;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Level;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



public class UMLJarLoggerUnitTest{ 


	final static Logger logger = Logger.getLogger( UMLJarLoggerUnitTest.class);	
	
	@Before
	@Test
	public void testFileCreation() {
		
		UMLJarLogger.enableUmlTranslatorLoggging("junitTest/","test.txt",null,Level.ALL, true);

		File file = new File("junitTest/test.txt");
		assertTrue(file.exists());
		
	}
	
	@Test
	public void testFileContent() throws IOException {
		String stringToTest = "abcdefg";
		File file = new File("junitTest/test.txt");
		file.deleteOnExit();
		file.setReadable(true);
		logger.info(stringToTest);
		FileReader reader =new FileReader(file);
		BufferedReader bufferReader = new BufferedReader(reader);
		
		HashSet<String> lines = new HashSet<String>();
		while(bufferReader.ready())
			lines.addAll( new HashSet<String> (Arrays.asList(bufferReader.readLine().split("\\s+"))));
		
		boolean isContainedInStringSet  = lines.contains(stringToTest);
		
		assertTrue(isContainedInStringSet);
	
		
	
		
	}
	
	
	
}

	
	
	
	
	
