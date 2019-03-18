/*Used to set logging location file and level for the UMl project Jar
 * 
 * Orginal Author: Matthew Hruskocy
 * 
 */
package edu.purdue.cs59000.logging;

import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class UMLJarLogger{ 


 private static final String fileAppenderName = "FileLevelLogger";
 private static final String defaultPatternLayout =  "%d %-5p [%c{1}] %m%n"; 
 private static String exportFileLocation; 
 private static String  fileName; 
 
private static final String consoleAppendername = "ConsoleLevelLogger"; 
 
	public static void enableUmlTranslatorLoggging(String exportFileLocation,String fileName, String pattern,Level level, boolean setAppend){
		
		UMLJarLogger.setExportFileLocation( exportFileLocation); 
		UMLJarLogger.setFileName(fileName); 
		
		FileAppender fileAppender = new FileAppender();
		fileAppender.setName(fileAppenderName);
		fileAppender.setFile(exportFileLocation+fileName);
		if(pattern ==null){ 
			fileAppender.setLayout(new PatternLayout(defaultPatternLayout));
		}
		else {
			fileAppender.setLayout(new PatternLayout(pattern));
		}
		fileAppender.setThreshold(level);
		fileAppender.setAppend(setAppend);
		fileAppender.activateOptions();
		Logger.getRootLogger().addAppender(fileAppender);
		
		ConsoleAppender consoleAppender = new ConsoleAppender(); 
		consoleAppender.setName(consoleAppendername);
		consoleAppender.setLayout(new PatternLayout(defaultPatternLayout));
		consoleAppender.activateOptions();
		
		
		Logger.getRootLogger().addAppender(consoleAppender);
		
		
		
		
	}
	
	
	
	
	public static String getFileLocation() {
		
		return exportFileLocation + "/" + fileName;
	}




	public static String getExportFileLocation() {
		return exportFileLocation;
	}




	public static void setExportFileLocation(String exportFileLocation) {
		UMLJarLogger.exportFileLocation = exportFileLocation;
	}




	public static String getFileName() {
		return fileName;
	}




	private static void setFileName(String fileName) {
		UMLJarLogger.fileName = fileName;
	}




	public static String getFileappendername() {
		return fileAppenderName;
	}




	public static String getDefaultpatternlayout() {
		return defaultPatternLayout;
	}




	public static String getConsoleappendername() {
		return consoleAppendername;
	}
	
	

	
	
	
	
	
}
