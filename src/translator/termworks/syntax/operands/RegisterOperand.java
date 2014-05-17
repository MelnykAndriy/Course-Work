package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Register;

public class RegisterOperand extends Operand {
	
	
	public RegisterOperand(ArrayList<Atom> atoms) {
		super(atoms);
		operKind = OperandKind.whatKind(OperandKind.REGISTER, ((Register) atoms.get(0)).GetByteSize() );
	}

	public static boolean isRegisterOperand(ArrayList < Atom > atoms) {
		return atoms.size() == 1 && atoms.get(0) instanceof Register; 
	}
	
	@Override
	public int calcSizeInBytes() {
		return ((Register) operandAtoms.get(0)).GetByteSize();
	}

	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}

	public int getRegNumb() {
		return ((Register) operandAtoms.get(0)).getRegNumb();
	}
	
	public int getRegSize() {
		return ((Register) operandAtoms.get(0)).GetByteSize();
	}
		
	@Override
	public AtomType getType() {
		return AtomType.RegOperand;
	}
		
}
