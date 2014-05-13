package translator.table.tablecomponents.reserved.operators;

import translator.exc.PriorityException;
import translator.table.tablecomponents.Atom;
import translator.termworks.syntax.operands.Operand;

public abstract class BinaryOperator extends Operator {
	
	public BinaryOperator(String name, String opDescription, int pr)
			throws PriorityException {
		super(name, opDescription, pr);
	}

	@Override
	public boolean isUnary() {
		return false;
	}

	@Override
	public boolean isBinary() {
		return true;
	}

	@Override
	public Atom searchedPosAtom() {
		return this;
	}

	final public Operand eval(Operand op1) {
		System.err.println("Unary eval have been called for binary operator.");
		System.exit(1);
		return null;
	}

	@Override
	public abstract Operand eval(Operand op1, Operand op2);
}
