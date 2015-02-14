package sml;

/** 
 * This class ....
 * 
 * @author John Spear
 */

public class OutInstruction extends Instruction {

	public OutInstruction(String label, int register) {
		super(label, "out", register);
	}

	@Override
	public void execute(Machine m) {
		System.out.println(m.getRegisters().getRegister(this.register));
	}

	@Override
	public String toString() {
		return super.toString() + " prints contents of " + register + " to java console";
	}
}
