package sml;

/**
 * This class takes subtracts the value of one register from the value of another register and places it in a third register
 * 
 * @author John Spear
 */

public class SubInstruction extends Instruction {

	public SubInstruction(String label, int result, int op1, int op2) {
		super(label,"sub",result, op1, op2);
	}
	
	/**
	 * This function subtracts the contents of one register from the content of another register and places the result in another register
	 * 	 * @param m Machine The machine that this is being executed on 
	 */
	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(this.register, value1 - value2);
	}

	@Override
	public String toString() {
		return super.toString() + " " + op1 + " - " + op2 + " to " + this.register;
	}
}
