package translator.table.tablecomponents.reserved.operators;

import translator.exc.PriorityException;
import translator.table.tablecomponents.Atom;
import translator.termworks.syntax.operands.Operand;

public abstract class UnaryOperator extends Operator {

	public UnaryOperator(String name, String opDescription, int pr)
			throws PriorityException {
		super(name, opDescription, pr);
	}

	@Override
	public boolean isUnary() {
		return true;
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public Atom searchedPosAtom() {
		return this;
	}

	public abstract Operand eval(Operand op1);

	@Override
	public Operand eval(Operand op1, Operand op2) {
		System.err.println("Binary eval have been called for unary operator.");
		System.exit(1);
		return null;
	}
	
}