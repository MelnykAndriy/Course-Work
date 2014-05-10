package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.tablecomponents.Atom;

public class MemoryOperand extends Operand {
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
		// TODO Auto-generated constructor stub
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) {
		
		return false;
	}
		
	@Override
	public int calcSizeInBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

}
