package sml;

/**
 * This class ....
 * 
 * @author John Spear
 */

public class OutInstruction extends Instruction {
	private int register;

	public OutInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public OutInstruction(String label, int register) {
		super(label, "out", register);

	}

	@Override
	public void execute(Machine m) {
		System.out.println(m.getRegisters().getRegister(this.register));
	}

	@Override
	public String toString() {
		return super.toString() + " Prints contents of " + register + " to java console";
	}
}
