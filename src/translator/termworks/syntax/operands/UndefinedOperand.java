package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.tablecomponents.Atom;

public class UndefinedOperand extends Operand {

	public UndefinedOperand(ArrayList<Atom> atoms) {
		super(atoms);
	}

	@Override
	public int calcSizeInBytes() {
		return -1;
	}



}
