package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.Register;

public class RegisterOperand extends Operand {

	public RegisterOperand(ArrayList<Atom> atoms) {
		super(atoms);
	}

	public static boolean isRegisterOperand(ArrayList < Atom > atoms) {
		return atoms.size() == 1 && atoms.get(0) instanceof Register; 
	}
	
	@Override
	public int calcSizeInBytes() {
		return ((Register) operandAtoms.get(0)).GetByteSize();
	}

}
