package sml;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * This class is the superclass of the classes for machine instructions
 * 
 * @author John Spear
 */

public abstract class Instruction {
	protected String label;
	protected String opcode;
	protected int result;
	protected int op1;
	protected int op2;

	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)

	public Instruction(String l, String op) {
		this.label = l;
		this.opcode = op;
	}
	
	public Instruction(String label, String opcode, int result, int op1, int op2) {
		this(label, opcode);
		this.result = result;
		this.op1 = op1;
		this.op2 = op2;
	}

	
	// = the representation "label: opcode" of this Instruction

	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	// Execute this instruction on machine m.

	public abstract void execute(Machine m);
}
