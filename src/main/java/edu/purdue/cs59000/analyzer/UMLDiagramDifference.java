package edu.purdue.cs59000.analyzer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.purdue.cs59000.umltranslator.UMLSequenceDiagram;
import edu.purdue.cs59000.umltranslator.UMLSymbol;
import edu.purdue.cs59000.umltranslator.exceptions.UMLSequenceDiagramException;

public class UMLDiagramDifference {

	final static Logger logger = Logger.getLogger(UMLDiagramDifference.class);

	UMLSymbol symbol;

	UMLDiagramDifference(UMLSymbol umlSymbol) {
		try {
			this.symbol = generateDeepCopy(umlSymbol);
		} catch (ClassNotFoundException | IOException e) {
			logger.debug("Exception thrown in UMLDiagramDifference(): {}, exception is: " + e);
			e.printStackTrace();
		}

	}

	private UMLSymbol generateDeepCopy(UMLSymbol umlSymbol) throws IOException, ClassNotFoundException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(umlSymbol);
		objectOutputStream.flush();
		objectOutputStream.close();
		byteArrayOutputStream.close();
		byte[] serializedObject = byteArrayOutputStream.toByteArray();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
		edu.purdue.cs59000.umltranslator.UMLSymbol object = (edu.purdue.cs59000.umltranslator.UMLSymbol) new ObjectInputStream(
				byteArrayInputStream).readObject();

		return object;

	}

	public UMLSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(UMLSymbol symbol) {
		this.symbol = symbol;
	}

}
