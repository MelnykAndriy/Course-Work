package translator.termworks.generating;

import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.OperandOption;
import translator.table.SymbolTable;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Command;
import translator.termworks.generating.ListingGenerator.SegmentInfo;
import translator.termworks.syntax.operands.*;

public class CommandListingGenerator {
	private static final String STANDARD_SEGMENT_REG = "ds";
	private Command cmd;
	private ArrayList< Operand > operands;
	private int maxOperandSize = 0;
	private SymbolTable symTab;
	private ListingGenerator.SegmentInfo curSeg;

	
	private OperandOption curOption; 
	
	private MemoryOperand mem = null;
	
	public CommandListingGenerator(SymbolTable symTab) {
		this.symTab = symTab;
		operands = new ArrayList < Operand > ();
	}
	
	public String generate(ParsedLine line,ListingGenerator.SegmentInfo segInf ) {
		preprocessing(line,segInf);
		StringBuffer genCommand = new StringBuffer("");
		genCommand.append( segInf.offsetToString() + " " );
		genCommand.append( genPrefix() );
		genCommand.append( genOpCode() );
//		genCommand.append( genModRM() );
//		genCommand.append( genSib());
		genCommand.append( genOffset() );
		genCommand.append( genAbsoluteOper() );
		return genCommand.toString();
	}

	private void preprocessing(ParsedLine line, SegmentInfo segInf) {
		curSeg = segInf;
		int cmdIndx = line.firstIndexOf(AtomType.Command);
		cmd = (Command) line.getAtomAt( cmdIndx );
		Atom.castCopy(operands, line.subArray(cmdIndx + 1));
		curOption = cmd.getOptionForOperands(operands);		
		mem = null;
		
		for ( Operand operand : operands ) 	{
			if ( operand.calcSizeInBytes() > maxOperandSize ) maxOperandSize = operand.calcSizeInBytes();
			if ( operand instanceof MemoryOperand ) {
				mem = (MemoryOperand) operand;
				break;
			}
		}
	}

	private String genPrefix( ) {
		StringBuffer retPrefixes = new StringBuffer("");
		if ( mem != null ) {
			if ( !mem.getReplacementReg().getName().toLowerCase().equals(STANDARD_SEGMENT_REG) )
				retPrefixes.append(mem.getReplacementByte() + ": " );
		}
		if ( iDataSizeOverridePrefixNeeded() ) 
			retPrefixes.append("66| ");
		if ( isAddressSizeOverridePrefixNeeded() )
			retPrefixes.append("67| ");
		return retPrefixes.toString();
	}
	
	private boolean isAddressSizeOverridePrefixNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean iDataSizeOverridePrefixNeeded() {
		if ( maxOperandSize != 1 && maxOperandSize != curSeg.size() )  return true;
		return false;
	}

	private String genOpCode() {
		return curOption.getOpcode() + " ";
	}

	private String genModRM() {
		if ( operands.size() != 0) 
			return genByte(getModFromOperands(),getRegFromOperands(),getRmFromOperands()) + " ";
		return "";
	}

	private int getModFromOperands() {
		if ( mem != null ) {
			if ( mem.isOffsetPresent() ) {
				if (mem.getOffsetInBytes() == 1)
					return 1;	// mod 01 8 bytes offset
				else 
					return 2;	// mod 10 16 | 32 bytes offset
			} else return 0; // mod 00 \ offset is absent
		}
		return 3; // mod = 11 \ only register operands
	}

	private int getRmFromOperands() {
		if ( getModFromOperands() == 3) {
			return ((RegisterOperand) operands.get(0)).getRegNumb();
		}
//		Register base = mem.getBase();
//		Register index = mem.getIndex();
//		if ( base.getName() == "BX")
		
		return 0;
	}

	private int getRegFromOperands() {
		if ( curOption.isAdditionalOpcodeInReg() ) 
			return curOption.getAdditionalOpcodeInReg();
		return ((RegisterOperand) operands.get(1)).getRegNumb();
	}

	private String genSib() {
		if ( mem != null ) {
				int scale = mem.getScale();
				return genByte((scale == 1)?(0):( (scale == 2)?(1):( (scale == 4)?(2):(3) )), 
								mem.getBase().getRegNumb() , mem.getIndex().getRegNumb() ) + " ";
		}
		return "";
	}
		
	private String genByte(int highest2Bits,int middle3Bits, int lowest3Bits ) {
		byte genByte = 0;
		genByte |= highest2Bits;
		genByte <<= 3;
		genByte |= middle3Bits;
		genByte <<= 3;
		genByte |= lowest3Bits;
		return ListingGenerator.buildDefaultHexRep(genByte,1);
	}

	private String genOffset() {		
		if ( mem != null && mem.isOffsetPresent() ) 
			return ListingGenerator.buildDefaultHexRep(mem.getOffset(), curSeg.size()) + " R ";
		return "";
	}

	private String genAbsoluteOper() {
		for ( Operand atom : operands ) {
			if ( atom instanceof AbsoluteExpr ) {
				return ListingGenerator.genHexFromOperand(atom, ((AbsoluteExpr) atom).calcSizeInBytes() ) + " ";
			}
		}
		return "";
	}
	
}
