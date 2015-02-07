package test;
import sml.Instruction;
import sml.Machine;
import sml.MulInstruction;
import sml.Registers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testMulInstruction {
	private Machine m;
	
	@Before
	public void before(){
		m = new Machine();
		m.setRegisters(new Registers());
		m.getRegisters().setRegister(10, 12);
		m.getRegisters().setRegister(11, 2);
		m.getRegisters().setRegister(12, 0);
		m.getRegisters().setRegister(13, -4);
		
	}
	
	@Test
	public void testMul_basic(){
		Instruction ins = new MulInstruction("f3",21,10,11);
		ins.execute(m);
		assertEquals(24, m.getRegisters().getRegister(21));
	}
	
	public void testMul_zero(){
		Instruction ins = new MulInstruction("f3",21,10,13);
		ins.execute(m);
		assertEquals(0, m.getRegisters().getRegister(21));
	}
	
	public void testMul_negative(){
		Instruction ins = new MulInstruction("f3",5,11,13);
		ins.execute(m);
		assertEquals(-8, m.getRegisters().getRegister(21));
	}

}
