package test;
import java.util.ArrayList;

import sml.Instruction;
import sml.Machine;
import sml.BnzInstruction;
import sml.LinInstruction;
import sml.Registers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testBnzInstruction {
	private Machine m;
	private ArrayList<Instruction> prog;	
	@Before
	public void before(){
		m = new Machine();
		m.setRegisters(new Registers());
		m.setPc(3);
		m.getProg().add(new LinInstruction("f1",1,5));
		m.getProg().add(new LinInstruction("f2",2,0));
		m.getProg().get(0).execute(m);
		m.getProg().get(1).execute(m);		
		m.getProg().add(new BnzInstruction("f3",1,"f2"));
		m.getProg().add(new BnzInstruction("f4",2,"f1"));		
	}
	
	@Test
	public void testBnz_notZero(){
		m.getProg().get(2).execute(m);
		assertEquals(1, m.getPc());
	}
	@Test
	public void testBnz_isZero(){
		m.getProg().get(3).execute(m);
		assertEquals(3, m.getPc());
	}

}
