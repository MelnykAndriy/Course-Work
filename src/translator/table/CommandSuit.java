package translator.table;

import java.util.ArrayList;

import translator.termworks.syntax.operands.Operand;

public class CommandSuit {
	private ArrayList < OperandOption > options;
	
	public CommandSuit () {
		options = new ArrayList < OperandOption > ();
	}
	
	public void addOption(OperandOption option) {
		options.add(option);
	}
	
	public OperandOption getOptionForOperands(ArrayList < Operand > operands) {
		OperandOption retOption = null;
		for (OperandOption option : options ) {
			if ( option.operandsMatches(operands) ) {
				if ( option.isSpecialCase() ) {
					return option;
				} else {
					retOption = option;
				}
			}
		}
		return retOption;
	}
	
	public boolean isOperandsComninationAllowed(ArrayList < Operand > operands) {
		return getOptionForOperands(operands) != null; 
	}
	
}
