package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.userdefined.Label;
import translator.table.tablecomponents.AtomType;

public class Relative extends Operand {

	public Relative(ArrayList<Atom> atoms) {
		super(atoms);
		operKind = OperandKind.whatKind(OperandKind.RELATIVE,1);
	}

	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}

	@Override
	public int calcSizeInBytes() {
		return OperandKind.sizeForOperandKind(operKind);
	}

	@Override
	public AtomType getType() {
		return AtomType.RelativeOperand;
	}
	
	public int calcDistanceTo(int callOffset,int opcodeSize) {
		return ( (Label) operandAtoms.get(0)).getOffset() - (callOffset + opcodeSize + 1);
	}
	
	public static boolean isRelative(ArrayList<Atom> operandAtoms) {
		if ( operandAtoms.size() == 1 &&
			 operandAtoms.get(0).getType() == AtomType.Label )
			return true;
		return false;
	}

}
