package translator.termworks.syntax.operands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import translator.table.OperandKind;
import translator.table.SymbolTable;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Register;
import translator.table.tablecomponents.reserved.operators.Operator;
import translator.table.tablecomponents.userdefined.Variable;

public class MemoryOperand extends Operand {
	private Register segChanger;
	private Register base;
	private Register index;
	private int scale;
	
	private Variable direct;
	
	private static Collection < Atom > allowedOperators;
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
	}
	
	static {
		allowedOperators = new Stack < Atom > ();
		allowedOperators.add( SymbolTable.getReserved("+"));
		allowedOperators.add( SymbolTable.getReserved(":"));
		allowedOperators.add( SymbolTable.getReserved("["));
		allowedOperators.add( SymbolTable.getReserved("]"));
		allowedOperators.add( SymbolTable.getReserved("ptr"));
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) { 
		for ( Atom atom : operandAtoms ) {
			if ( !(atom instanceof Variable || atom instanceof Register || 
					allowedOperators.contains(atom))) {
				return false;
			}
		}
		return true;
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
