
package translator.table.tablecomponents;

import java.util.ArrayList;

import translator.table.CommandSuit;
import translator.table.OperandOption;
import translator.termworks.syntax.operands.Operand;

public class Command extends Mnemocode {
//    public enum OperandKind { reg,mem,imm }
 //   private int commandNumb = 0;
    private int operandNumb = 0;
    private CommandSuit suit;
//    private Set<OperandKind> firstOperandKinds = null;
//    private Set<OperandKind> secondOperandKinds = null;
//    private Atom firstOper = null;
//    private Atom secondOper = null;
    
    
    public synchronized int getOperandNumb() {
		return operandNumb;
	}

	public Command(String _name,int operandNumb,CommandSuit suit) {
        super(_name.toLowerCase());
        this.suit = suit;
        if ( operandNumb < 0) {
        	System.err.println("Number of operands in command should not be negative.");
        	System.exit(1);
        }
        this.operandNumb = operandNumb;
    }
    
   // public int GetCommandNumb() {
   //     return commandNumb;
   // }
    
    @Override
    public AtomType getType() {
        return AtomType.Command;
    }
    
    public String toString() {
    	return "Command";
    }

	@Override
	public Atom searchedPosAtom() {
		return this;
	}
	
	public OperandOption getOptionForOperands(ArrayList< Operand > operands) {
		return suit.getOptionForOperands(operands);
	}
	
	public String getOpCodeForOperands(ArrayList< Operand > operands) {
		return suit.getOptionForOperands(operands).getOpcode();
	}

	public boolean isOperandsCombinationAllowed(ArrayList<Atom> atomOperands) {
		ArrayList < Operand > operands = new ArrayList < Operand > ();
		for ( Atom atom : atomOperands )
			operands.add((Operand) atom);
		return suit.isOperandsComninationAllowed(operands);
	}
	
}