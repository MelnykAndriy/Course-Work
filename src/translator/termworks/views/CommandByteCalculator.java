package translator.termworks.views;

import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.*;
import translator.table.tablecomponents.userdefined.Segment;
import translator.termworks.generating.CommandListingGenerator;
import translator.termworks.syntax.operands.*;

public class CommandByteCalculator {
	private Command cmd;
	private ArrayList < Operand > operands;
	
	public CommandByteCalculator() {
		operands = new ArrayList < Operand > ();
	}
	
	public int calculate(ParsedLine ln, Segment curSeg)  {
		preprocessing(ln,curSeg);
		if ( cmd instanceof RelativeCmd ) 
			return relativeCmdCalc( curSeg );
		return simpleCmdCalc(ln,curSeg);
	}

	private int simpleCmdCalc(ParsedLine ln, Segment curSeg) {
		return CommandListingGenerator.calcSimpleLineSize(ln, curSeg);
	}

	private int relativeCmdCalc( Segment seg ) {
		if (( (Relative) operands.get(0)).isDefProcessed() )
			return cmd.getOptionForOperands(operands).opcodeByteSize()
					   + ((Relative) operands.get(0)).calcSizeInBytes();
		return ( (RelativeCmd) cmd).getOpcodeMaxBytes() + seg.size();
	}

	private void preprocessing(ParsedLine line, Segment curSeg) {
		int cmdIndx = line.firstIndexOf(AtomType.Command);
		cmd = (Command) line.getAtomAt( cmdIndx );
		Atom.castCopy(operands,line.subArray(cmdIndx + 1));
				
	}
	
}
