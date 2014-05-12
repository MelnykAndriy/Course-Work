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
	
//	public String getOpcodeForOperands(ArrayList < Operand > operands) {
//		String retOpcode = "Error : opcode for operands not found.";
//		for (OperandOption option : options ) {
//			if ( option.operandsMatches(operands) ) {
//				if ( option.isSpecialCase() ) {
//					return option.getOpcode();
//				} else {
//					retOpcode = option.getOpcode();
//				}
//			}
//		}
//		return retOpcode;
//	}
	
//	public boolean isAdditionalOpcodeForOperandsNeeded(ArrayList < Operand > operands) {
//		for (OperandOption option : options) {
//			if ( option.operandsMatches(operands) )
//				return option.isAdditionalOpcodeInReg();
//		}
//		return false;		
//	}
//	
}
