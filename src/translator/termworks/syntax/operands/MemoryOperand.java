package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Register;
import translator.table.tablecomponents.userdefined.Variable;

public class MemoryOperand extends Operand {
	private Register segChanger;
	private Register base;
	private Register index;
	private int scale;
	
	private Variable direct;
	
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
		// TODO Auto-generated constructor stub
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) { 
		
			// TODO ATTENTION NOT IMPLEMENTED RETURNS always true 
		
		
		
		
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
		return false;
	}
	
	public boolean isSibNeeded() {
		//TODO
		return false;
	}
	
	public int getScale() {
		return scale;
	}
	
	public Register getBase() {
		return base;
	}
	
	public Register getIndex() {
		return index;
	}
		
	@Override
	public int calcSizeInBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Register getSegReplacement() {
		return segChanger;
	}

	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}
	
	public void isValidMemory() throws Exception {
		
	}

	@Override
	public AtomType getType() {
		return AtomType.Memory;
	}

}
