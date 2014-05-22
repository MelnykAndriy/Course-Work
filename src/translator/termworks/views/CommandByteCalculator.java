package translator.termworks.views;

import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.OperandOption;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Command;
import translator.table.tablecomponents.reserved.RelativeCmd;
import translator.table.tablecomponents.userdefined.Segment;
import translator.termworks.syntax.operands.Operand;
import translator.termworks.syntax.operands.Relative;
import translator.termworks.syntax.operands.UndefinedOperand;

public class CommandByteCalculator {
	private Command cmd;
	private ArrayList < Operand > operands;
	private OperandOption curOption;
	
	public CommandByteCalculator() {
		operands = new ArrayList < Operand > ();
	}
	
	public int calculate(ParsedLine ln, Segment curSeg)  {
		preprocessing(ln,curSeg);
		if ( cmd instanceof RelativeCmd ) 
			return relativeCmdCalc(curSeg);
		return simpleCmdCalc();
	}

	private int simpleCmdCalc() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int relativeCmdCalc( Segment seg ) {
		if ( operands.get(0) instanceof UndefinedOperand ) 
			return ( (RelativeCmd) cmd).getOpcodeMaxBytes() + seg.size();
		if ( operands.get(0) instanceof Relative ) 
			return cmd.getOptionForOperands(operands).opcodeByteSize()
				   + ((Relative) operands.get(0)).calcSizeInBytes();
		return 0;
	}

	private void preprocessing(ParsedLine line, Segment curSeg) {
		int cmdIndx = line.firstIndexOf(AtomType.Command);
		cmd = (Command) line.getAtomAt( cmdIndx );
		Atom.castCopy(operands,line.subArray(cmdIndx + 1));
				
	}
	
//	private void preprocessing(ParsedLine line, Segment segInf) {
//		curSeg = segInf;
//		int cmdIndx = line.firstIndexOf(AtomType.Command);
//		cmd = (Command) line.getAtomAt( cmdIndx );
//		Atom.castCopy(operands, line.subArray(cmdIndx + 1));
//		curOption = cmd.getOptionForOperands(operands);		
//		mem = null;
//		maxOperandSize = 0;
//		
//		for ( Operand operand : operands ) 	{
//			if ( operand.calcSizeInBytes() > maxOperandSize ) maxOperandSize = operand.calcSizeInBytes();
//			if ( operand instanceof MemoryOperand ) {
//				mem = (MemoryOperand) operand;
//				break;
//			}
//		}
//	}
	
	
//	public int calcLineOffset(ParsedLine line,Segment seg ) {
//		int retVal = 1;
//		preprocessing(line,seg);
//		if ( mem != null && mem.isRegReplacement() ) 
//			retVal++;
//		if ( isDataSizeOverridePrefixNeeded() ) 
//			retVal++;
//		if ( isAddressSizeOverridePrefixNeeded() )
//			retVal++;
//		if ( !curOption.isSpecialCase() && operands.size() != 0) 
//			retVal++;
//		if ( mem != null && mem.isSibNeeded() )
//			retVal++;
//		if ( mem != null && mem.isDirect() ) 
//			retVal += 2;
//		String displacement = genAbsoluteOper().trim();
//		if ( displacement.length() != 0 )
//			retVal += displacement.length() / 2;
//			
//		return retVal;
//	}
}
