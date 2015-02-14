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


/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
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

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {
		if (line.equals(""))
			return null;

		String ins = scan();
		try {
			Class<?> instruct = Class.forName("sml."+ ucfirst(ins) + "Instruction");
			Constructor[] allConstructors = instruct.getDeclaredConstructors();
			//The largest constructor is the one to use.
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
			
			try {
				newInstruct = (Instruction) largest.newInstance(params);
			} catch (InstantiationException e) {
				System.out.println(e.getMessage());
			} catch (IllegalAccessException e) {
				System.out.println(e.getMessage());
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (InvocationTargetException e) {
				System.out.println(e.getMessage());
			}

			return newInstruct;
		} catch (ClassNotFoundException ex) {

			System.out.println("sml."+ ins + "Instruction doesn't exist");
			return null;
		}
		
	}
	
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
		printArr(params);
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
	
	private static String ucfirst(String word) {
		String ucWord = "";
		if ((word.length() > 0) && (word != null)){
			ucWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
		}
		return ucWord;
	}
	
	private void printArr(Object[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.println(arr[i]);
		}
	}
}