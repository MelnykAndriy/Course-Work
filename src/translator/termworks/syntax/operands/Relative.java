package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class Relative extends Operand {

	public Relative(ArrayList<Atom> atoms) {
		super(atoms);
		operKind = OperandKind.whatKind(OperandKind.RELATIVE,1);
	}

	@Override
	public OperandKind getOperandKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int calcSizeInBytes() {
		return 0;
	}

	@Override
	public AtomType getType() {
		return AtomType.RelativeOperand;
	}

	public static boolean isRelative(ArrayList<Atom> operandAtoms) {
		if ( operandAtoms.size() == 1 &&
			 operandAtoms.get(0).getType() == AtomType.Label )
			return true;
		return false;
	}

}
