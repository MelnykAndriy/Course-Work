package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.Register;

public class MemoryOperand extends Operand {
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
		// TODO Auto-generated constructor stub
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) {
		// TODO
		return false;
	}
		
	public int getOffset() {
		// TODO
		return 0;
	}
	
	public int getOffsetInBytes() {
		// TODO
		return 0;
	}
	
	public boolean isOffsetPresent() {
		// TODO
		return false;
	}
	
	public boolean isSibNeeded() {
		//TODO
		return false;
	}
	
	public int getScale() {
		// TODO
		return 0;
	}
	
	public Register getBase() {
		// TODO
		return null;
	}
	
	public Register getIndex() {
		// TODO
		return null;
	}
		
	@Override
	public int calcSizeInBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getSegReplacement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperandKind getOperandKind() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void isValidMemory() throws Exception {
		
	}

	@Override
	public AtomType getType() {
		return AtomType.Memory;
	}

}
