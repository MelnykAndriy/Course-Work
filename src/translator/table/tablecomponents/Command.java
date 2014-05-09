
package translator.table.tablecomponents;
//import java.util.Set;

public class Command extends Mnemocode {
    public enum OperandKind { reg,mem,imm }
 //   private int commandNumb = 0;
    private int operandNumb = 0;
//    private Set<OperandKind> firstOperandKinds = null;
//    private Set<OperandKind> secondOperandKinds = null;
//    private Atom firstOper = null;
//    private Atom secondOper = null;
    
    
    public synchronized int getOperandNumb() {
		return operandNumb;
	}

	public Command(String _name,int operandNumb) {
        super(_name.toLowerCase());
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
	
}