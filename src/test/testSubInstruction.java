package test;
import sml.Instruction;
import sml.Machine;
import sml.SubInstruction;
import sml.Registers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testSubInstruction {
	private Machine m;
	
	@Before
	public void before(){
		m = new Machine();
		m.setRegisters(new Registers());
		m.getRegisters().setRegister(10, 12);
		m.getRegisters().setRegister(11, 2);
		m.getRegisters().setRegister(12, 0);
		m.getRegisters().setRegister(13, -4);
		m.getRegisters().setRegister(14, -2);
	}
	
	@Test
	public void testSub_Basic(){
		Instruction ins = new SubInstruction("f3",21,10,11);
		ins.execute(m);
		assertEquals(10, m.getRegisters().getRegister(21));
	}
	
	@Test	
	public void testSub_ByZero(){
		Instruction ins = new SubInstruction("f3",21,10,12);
		ins.execute(m);
		assertEquals(12, m.getRegisters().getRegister(21));
	}
	
	@Test	
	public void testSub_Negative(){
		Instruction ins = new SubInstruction("f3",5,13,11);
		ins.execute(m);
		assertEquals(-6, m.getRegisters().getRegister(5));
	}
	
	@Test	
	public void testSub_Negative_Negative(){
		Instruction ins = new SubInstruction("f3",5,13,14);
		ins.execute(m);
		assertEquals(-2, m.getRegisters().getRegister(5));
	}

}
