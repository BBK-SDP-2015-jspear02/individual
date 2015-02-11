package sml;

/**
 * Class for dividing values and storing them in the set register
 * 
 * @author John Spear
 */

public class DivInstruction extends Instruction {

	public DivInstruction(String label, int result, int op1, int op2) {
		super(label,"div",result, op1, op2);
	}

	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		if (value2 == 0){
			//Stop arithmetic overflow caused by dividing by zero
			m.getRegisters().setRegister(this.register, 0);	
		}else {
			m.getRegisters().setRegister(this.register, value1 / value2);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " " + op1 + " / " + op2 + " to " + this.register;
	}
}

