package translator.table.tablecomponents;

import translator.exc.PriorityException;
import translator.termworks.syntax.Operand;

interface Calculable {
	int MIN_PRIORITY = 13;
	int MAX_PRIORITY = 1;
    public Operand eval( Operand op1,Operand op2 ) ;
}

public abstract class Operator extends Atom implements Calculable {

	private String opDescription;
	private int priority;
		
	public Operator(String name,String opDescription,int pr) throws PriorityException {
		super(name);
		if ( pr > MIN_PRIORITY || pr < MAX_PRIORITY) throw new PriorityException();
		this.priority = pr;
		this.opDescription = opDescription;
	}
	
	public int getPriority() {
		return priority;
	}

	public String toString() {
		return opDescription;
	}

	@Override
	public AtomType getType() {
		return AtomType.Operator;
	}

}
