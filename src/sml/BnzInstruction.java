package sml;

import java.util.ArrayList;

/**
 * This class checks the value of a particular register and if it is zero it does nothing. 
 * If it isn't  zero it changes the program counter to the instruction with the label that is specified as the next label.
 * Obvious problems here : what if two instructions are given the same label?
 * @author John Spear
 */

public class BnzInstruction extends Instruction {

	private String nextLabel;

	public BnzInstruction(String label, int register, String nextLabel) {
		super(label, "bnz", register);
		this.nextLabel = nextLabel;
	}
	/**
	 * This function checks whether the value in the register is zero. If it isn't it takes the next label and loops
	 * through the machines instructions until it finds that one. It then sets the program counter to that of the instruction.
	 * @param m Machine The machine that this is being executed on 
	 */
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
