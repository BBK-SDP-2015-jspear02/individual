package sml;
 
/**
 * Class for multiplying values and storing them in the set register
 * 
 * @author John Spear
 */

public class MulInstruction extends Instruction {

	public MulInstruction(String label, int result, int op1, int op2) {
		super(label,"mul",result, op1, op2);
	}

	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(this.register, value1 * value2);
	}

	@Override
	public String toString() {
		return super.toString() + " " + op1 + " x " + op2 + " to " + this.register;
	}
}
