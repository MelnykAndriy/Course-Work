package translator.table;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import translator.termworks.syntax.operands.Operand;

public final class OperandOption {
	private String opcode;
	private byte regAdditionalOpcode;
	private ArrayList < Entry< OperandKind, String > > operandsSuit = new ArrayList < Entry< OperandKind, String > > ();
	private boolean specialCase = false;
	
	public OperandOption(String opcode) {
		regAdditionalOpcode = -1;
		this.opcode = opcode;
	}
	
	public OperandOption(String opcode,String rmModifier ,OperandKind firstOperTP) {
		regAdditionalOpcode = parseRmModifier(rmModifier);
		this.opcode = opcode;	
		operandsSuit.add(new SimpleEntry<OperandKind,String>(firstOperTP,null));
	}
	
	public OperandOption(String opcode,String rmModifier ,
						 OperandKind firstOperTP,OperandKind secondOperTP) {
		regAdditionalOpcode = parseRmModifier(rmModifier);
		this.opcode = opcode;
		operandsSuit.add(new SimpleEntry<OperandKind,String>(firstOperTP,null));
		operandsSuit.add( new SimpleEntry<OperandKind,String>(secondOperTP,null) );
	}
	
	public OperandOption(String opcode,String rmModifier ,
						 OperandKind firstOperTP,String specialName,
						 OperandKind secondOperTP) {
		specialCase = true;
		regAdditionalOpcode = parseRmModifier(rmModifier);
		this.opcode = opcode;
		operandsSuit.add(new SimpleEntry<OperandKind,String>(firstOperTP,specialName));
		operandsSuit.add( new SimpleEntry<OperandKind,String>(secondOperTP,null) );
	}
	
	public boolean operandsMatches(ArrayList < Operand > operands) {
		if ( operands.size() != operandsSuit.size() ) return false;
		for (int i = 0; i < operands.size() ; i++ ) {
//			System.out.println("from suit : " + operandsSuit.get(i).getKey());
//			System.out.println("from operand : " + operands.get(i).getOperandKind());
			if ( operands.get(i).getOperandKind() != operandsSuit.get(i).getKey() || 
					operandsSuit.get(i).getValue() != null && 
					!operandsSuit.get(i).getValue().equals(operands.get(i).getName()) )
				return false;
		}
		return true;
	}
	
	private byte parseRmModifier(String rmModifier) {
		if ( rmModifier.toLowerCase().equals("/r") )
			return -1;
		
		if ( rmModifier.matches("/[0-7]") )
			return (byte) Character.digit(rmModifier.charAt(1),10);
		
		System.err.println("ERROR : undefined rmModifer was provided into OperandOption constructor.");
		System.exit(1);
		return 0;
	}
	
	public boolean isAdditionalOpcodeInReg() {
		return regAdditionalOpcode != -1;
	}
	
	public int getAdditionalOpcodeInReg() {
		return regAdditionalOpcode;		
	}
	
	public String getOpcode() {
		return opcode;
	}
	
	public boolean isSpecialCase() {
		return specialCase;
	}
	
}
