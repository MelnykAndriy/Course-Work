package translator.termworks.generating;

import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.OperandOption;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.reserved.Command;
import translator.table.tablecomponents.userdefined.Segment;
import translator.termworks.syntax.operands.*;

public class CommandListingGenerator {
	private Command cmd;
	private ArrayList< Operand > operands;
	private int maxOperandSize = 0;
	private Segment curSeg;
	
	private OperandOption curOption; 
	
	private MemoryOperand mem = null;
	
	public CommandListingGenerator() {
		operands = new ArrayList < Operand > ();
	}
	
	public String generate(ParsedLine line,Segment segInf,int offset ) {
		preprocessing(line,segInf);
		return ( isRelativeCmd() )?( genRelativeCmd(offset) ):( genCommonCmd(offset) );
	}
	
	private String genCommonCmd(int offset ) {
		StringBuffer genCommand = new StringBuffer("");
		genCommand.append( ListingGenerator.buildDefaultHexRep(offset, curSeg.size()) + " " );
		genCommand.append( genPrefix() );
		genCommand.append( genOpCode() );
		genCommand.append( genModRM() );
		genCommand.append( genSib() );
		genCommand.append( genOffset() );
		genCommand.append( genAbsoluteOper() );
		return genCommand.toString();
	}
	
	private String genRelativeCmd( int offset ) {
		StringBuffer genCommand = new StringBuffer("");
		genCommand.append( ListingGenerator.buildDefaultHexRep(offset, curSeg.size()) + " " );
		genCommand.append( genOpCode() );
		genCommand.append( genDistance(offset) );
		return genCommand.toString();
	}
	
	private String genDistance( int offset ) {
		int distance =  ((Relative)operands.get(0)).calcDistanceTo(offset);
		return ListingGenerator.buildDefaultHexRep( (0 > distance)?(distance - 2):(distance + 1), 1);
	}

	private boolean isRelativeCmd() {
		return operands.size() == 1 && operands.get(0) instanceof Relative;
	}

	private void preprocessing(ParsedLine line, Segment segInf) {
		curSeg = segInf;
		int cmdIndx = line.firstIndexOf(AtomType.Command);
		cmd = (Command) line.getAtomAt( cmdIndx );
		Atom.castCopy(operands, line.subArray(cmdIndx + 1));
		curOption = cmd.getOptionForOperands(operands);		
		mem = null;
		maxOperandSize = 0;
		
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
		if ( mem != null && mem.isRegReplacement() ) {
			retPrefixes.append(mem.getReplacementByte() + ": " );
		}
		if ( isDataSizeOverridePrefixNeeded() ) 
			retPrefixes.append("66| ");
		if ( isAddressSizeOverridePrefixNeeded() )
			retPrefixes.append("67| ");
		return retPrefixes.toString();
	}
	
	private boolean isAddressSizeOverridePrefixNeeded() {
		if ( mem != null && mem.getBase() != null && mem.getBase().GetByteSize() != curSeg.size() )
			return true;
		return false;
	}

	private boolean isDataSizeOverridePrefixNeeded() {
		if ( maxOperandSize > 1 && maxOperandSize != curSeg.size() )  return true;
		return false;
	}

	private String genOpCode() {
		return curOption.getOpcode() + " ";
	}

	private String genModRM() {
		if ( !curOption.isSpecialCase() && operands.size() != 0) 
			return genByte(getModFromOperands(),getRegFromOperands(),getRmFromOperands()) + " ";
		return "";
	}

	private int getModFromOperands() {
		if ( mem != null  ) {
			if ( mem.isOffsetPresent() && !mem.isDirect() ) {
				if (mem.getOffsetInComand() == 1)
					return 1;	// mod 01 8 bytes offset
				else 
					return 2;	// mod 10 16 | 32 bytes offset
			} else return 0; // mod 00 \ offset is absent
		}
		return 3; // mod = 11 \ only register operands
	}

	private int getRmFromOperands() {
		if ( mem != null && mem.isDirect() ) return 6;
		if ( getModFromOperands() == 3) {
			return ((RegisterOperand) operands.get(0)).getRegNumb();
		}
		if ( mem.getBase() != null  )
			return RmContainer.getRm(mem.getBase(),mem.getIndex());
		return 0;
	}

	private int getRegFromOperands() {
		if ( curOption.isAdditionalOpcodeInReg() ) 
			return curOption.getAdditionalOpcodeInReg();
		for (int iter = operands.size() - 1 ; iter >=0 ; iter-- ) {
			if ( operands.get(iter) instanceof RegisterOperand ) {
				return ((RegisterOperand) operands.get(iter)).getRegNumb() ;
			}
		}
		return -1;
	}

	private String genSib() {
		if ( mem != null && mem.isSibNeeded() ) {
				int scale = mem.getScale();
				return genByte((scale == 1)?(0):( (scale == 2)?(1):( (scale == 4)?(2):(3) )), 
								mem.getIndex().getRegNumb(), mem.getBase().getRegNumb()  ) + " ";
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
		if ( mem != null && mem.isDirect() ) 
			return ListingGenerator.buildDefaultHexRep(mem.getDirectOffset() , curSeg.size()) + " R";
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
	
	public int calcLineOffset(ParsedLine line,Segment seg ) {
		int retVal = 1;
		preprocessing(line,seg);
		if ( mem != null && mem.isRegReplacement() ) 
			retVal++;
		if ( isDataSizeOverridePrefixNeeded() ) 
			retVal++;
		if ( isAddressSizeOverridePrefixNeeded() )
			retVal++;
		if ( !curOption.isSpecialCase() && operands.size() != 0) 
			retVal++;
		if ( mem != null && mem.isSibNeeded() )
			retVal++;
		if ( mem != null && mem.isDirect() ) 
			retVal += 2;
		String displacement = genAbsoluteOper().trim();
		if ( displacement.length() != 0 )
			retVal += displacement.length() / 2;
			
		return retVal;
	}
	
}
