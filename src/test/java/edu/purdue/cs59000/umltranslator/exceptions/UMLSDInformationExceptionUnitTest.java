package edu.purdue.cs59000.umltranslator.exceptions;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import edu.purdue.cs59000.umltranslator.*;
import edu.purdue.cs59000.logging.UMLJarLogger;

public class UMLSDInformationExceptionUnitTest{

	final static Logger logger = Logger.getLogger( UMLSDInformationExceptionUnitTest.class);	
	private static String errorString = "UMLSDInformationException: There is not enough information available in the sequence diagram to generate a representation or source code"; 
    final static String fileLocation = "junitTest/UMLSDInformationExceptionUnitText.txt";
	
	@Before
	@Test
	public void testFileCreation() {
		
		UMLJarLogger.enableUmlTranslatorLoggging("junitTest/","UMLSDInformationExceptionUnitText.txt",null,Level.ALL, true);

		File file = new File( fileLocation);
		assertTrue(file.exists());
		
	}
	
	
	@Test
	public void testFileContent() throws IOException {
	
		File file = new File(fileLocation);
		file.deleteOnExit();
		file.setReadable(true);
	
		try {
			throw new  UMLSDInformationException();	
		}catch(Exception e) {
			;
		}
		
		FileReader reader =new FileReader(file);
		BufferedReader bufferReader = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		
		while(bufferReader.ready())
			sb.append(bufferReader.readLine());
		
		boolean isContainedInStringSet  = sb.toString().contains(errorString);
		bufferReader.close();
		assertTrue(isContainedInStringSet);
	
		
	
		
	}
	
	
	
	
	
	
	
	
}
