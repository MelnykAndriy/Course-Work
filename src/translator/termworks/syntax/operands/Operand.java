package translator.termworks.syntax.operands;

import java.util.ArrayList;

import translator.table.OperandKind;
import translator.table.tablecomponents.Atom;

public abstract class Operand extends Atom {
	protected ArrayList < Atom > operandAtoms;
	protected OperandKind operKind;
	
	public abstract OperandKind getOperandKind() ;
	public abstract int calcSizeInBytes() ;

	public Operand(ArrayList < Atom > atoms) {
		super(buildStringFromAtoms(atoms));
		operandAtoms = atoms;
	}
	
	public int tokenNumb() {
		return operandAtoms.size();
	}
	
	public static Operand makeOperand(UndefinedOperand oper) {
		return makeOperand(oper.operandAtoms);
	}
		
	public static Operand makeOperand(ArrayList < Atom > operandAtoms) {
		if ( AbsoluteExpr.isAbsoluteExpr(operandAtoms) )
			return new AbsoluteExpr(operandAtoms);
		if ( RegisterOperand.isRegisterOperand(operandAtoms))
			return new RegisterOperand(operandAtoms);
		if ( Relative.isRelative(operandAtoms)) 
			return new Relative(operandAtoms);
		if ( MemoryOperand.isMemoryOperand(operandAtoms) )  
			return new MemoryOperand(operandAtoms);
				
		return new UndefinedOperand(operandAtoms);
	}

	public boolean isMissing() {
		return operandAtoms.size() == 0;
	}
	
    @Override
	public Atom searchedPosAtom() {
		if ( operandAtoms.size() != 0 )
			return  operandAtoms.get(0);
		return null;
    }
    
}
