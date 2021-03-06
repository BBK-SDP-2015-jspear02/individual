package sml;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.Class;
import java.lang.ClassNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program. Uses reflection to open up instances of classes.
 * 
 * @author John Spear and BBK
 */

public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				String label = scan();

				if (label.length() > 0) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					return false;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		return true;
	} 

	/**
	 * Each line of text in the file should contain one instruction split by space. 
	 * This function uses reflection to load an instance of the correct class (by reading the opcode in the file) and using the parameter.
	 * The classes I created only had one constructor but the classes already made (add and lin) had two constructors so I checked to see which the 
	 * longest constructor was before using it to instantiate the object.
	 * @param label String The label which has already been stripped from the line of text
	 * @return newInstruction Instruction The instruction which has been instantiated from reading the line of the text file.
	 */	
	public Instruction getInstruction(String label) {
		if (line.equals(""))
			return null;

		String ins = scan();
		try {
			//load the class for reflection
			Class<?> instruct = Class.forName("sml."+ ucfirst(ins) + "Instruction");
			if (instruct.getSuperclass().equals(Instruction.class)) {
			Constructor[] allConstructors = instruct.getDeclaredConstructors();
			
			//The largest constructor is the one to use. Loop through until you find the largest.
			Constructor largest = null;
			for (Constructor ctor : allConstructors) {
				if (largest == null) {
					largest = ctor;
				} else if (ctor.getParameterCount() > largest.getParameterCount()) {
					largest = ctor;
				}
			}
			//Get the correct params back from the file
			Object[] params = getParams(label, largest);
			Instruction newInstruct = null;
			//Create the new instruction. All of these 
			try {
				newInstruct = (Instruction) largest.newInstance(params);
			} catch (InstantiationException e) {
				printErrs("Wasn't able to instantiate clas",ucfirst(ins));				
			} catch (IllegalAccessException e) {
				printErrs("Illegal Access Exception with class",ucfirst(ins));				
			} catch (IllegalArgumentException e) {
				printErrs("Illegal Argument Exception with class",ucfirst(ins));				
			} catch (InvocationTargetException e) {
				printErrs("Invocation Exception with class",ucfirst(ins));
			}

			return newInstruct;
			
			} else {
				printErrs("Class not a subclass of Instruction with",ucfirst(ins));
				return null;
			}
			
		} catch (ClassNotFoundException ex) {
			printErrs("Class not found exception with",ucfirst(ins));
			return null;
		}
		
	}
	/**
	 * Places the label as the first item in the array then loops through the constructor's required items, choosing whether
	 * to use scan or scanInt based upon whether the param is an int or a string.
	 * @param label String The label which has already been stripped from the line of text. Is going to be the first item in the array.
	 * @param con Constructor The constructor which is going to be used to create the instance.
	 * @return params Object[] An array of the correct constructors taken from the file.
	 */	
	public Object[] getParams(String label, Constructor con) {
		Object[] params = new Object[con.getParameterCount()];
		//Set the first item to be the label
		params[0] = label;
		Class<?>[] paramType  = con.getParameterTypes();
		//Skip the first one. It's already been set.
		for (int i = 1; i < paramType.length; i++) {
			if (paramType[i].getTypeName().equals("int")){
				params[i] = scanInt();
			} else {
				params[i] = scan();
			}

		}
		//printArr(params);
		return params;
	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
	
	/**
	 * We need to use this because the classes are named with an upper case.
	 * @param word String The word that you need to uppercase the first letter of.
	 * @return ucWord String The word with the transformation
	 */
	private static String ucfirst(String word) {
		String ucWord = "";
		if ((word.length() > 0) && (word != null)){
			ucWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
		}
		return ucWord;
	}
	
	//Just for testing
	private void printArr(Object[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.println(arr[i]);
		}
	}

	private void printErrs(String msg, String cl) {
		System.err.println("Error: " + msg + " " + ucfirst(cl) + "Instruction" );
	}
}