package sml;

import java.util.ArrayList;

/**
 * This class ....
 * 
 * @author John Spear
 */

public class BnzInstruction extends Instruction {

	private int register;
	private String nextLabel;

	public BnzInstruction(String label, String op) {
		super(label, op);
	}

	public BnzInstruction(String label, int register, String nextLabel) {
		this(label, "bnz");
		this.register = register;
		this.nextLabel = nextLabel;
	}

	@Override
	public void execute(Machine m) {
		//If the value in the register is not zero the PC will need to be set to that of the instruction specified
		//Otherwise do nothing. 
		if (m.getRegisters().getRegister(this.register) != 0) {
			ArrayList<Instruction> prog = m.getProg();
			for (int i = 0; i < prog.size(); i++){
				if (prog.get(i).label.equals(this.nextLabel)){
					m.setPc(i);
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return super.toString() + " if " + this.register + " not zero then next instruction is " + this.label;
	}
}
