package translator.termworks.generating;

import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.Register;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.MemoryOperand;
import translator.termworks.syntax.operands.Operand;

public class CommandListingGenerator {
	private ArrayList < Atom > lineAtoms;
	private SymbolTable symTab;
	private int operandsSize;
	
	public CommandListingGenerator(SymbolTable symTab) {
		this.symTab = symTab;
		operandsSize = calcOperandsSize();
	}

	private int calcOperandsSize() {
		// TODO Auto-generated method stub
		return 4;
	}
	
	public String generate(ParsedLine line) {
		lineAtoms = line.getAtoms();
		StringBuffer genCommand = new StringBuffer("");
		genCommand.append( genPrefix() );
		genCommand.append( genOpCode() );
		genCommand.append(genModRM() );
		genCommand.append(genSib());
		genCommand.append( genOffset() );
		genCommand.append(genAbsoluteOper() );
		return genCommand.toString();
	}

	private String genPrefix( ) {
	
		return null;
	}
	
	private String genOpCode() {
		// TODO Auto-generated method stub
		return "op code here";
	}

	private String genModRM() {
		byte modRmByte = 0;
		
		// TODO Auto-generated method stub

		return ListingGenerator.buildDefaultHexRep(modRmByte, 1);
	}

	private String genSib() {
		for ( Atom atom : lineAtoms ) {
			if ( atom instanceof MemoryOperand && 
					((MemoryOperand) atom).isSibNeeded() ) {
				MemoryOperand mem = (MemoryOperand) atom;
				return genSib(mem.getScale(),mem.getBase(),mem.getIndex());
			}
		}
		return "";

	}
	
	private String genSib(int scale, Register base, Register index) {
		byte sibByte = 0;
		sibByte |= (scale == 1)?(0):( (scale == 2)?(1):( (scale == 4)?(2):(3) ));
		sibByte |= index.getRegNumb();
		sibByte <<= 3;
		sibByte |= base.getRegNumb();
		sibByte <<= 3;
		return ListingGenerator.buildDefaultHexRep(sibByte,1);
	}

	private String genOffset() {
		for ( Atom atom : lineAtoms ) {
			if ( atom instanceof MemoryOperand && 
				 ((MemoryOperand) atom).isOffsetPresent() )
				return ListingGenerator.buildDefaultHexRep(
						((MemoryOperand)atom).getOffset(),
						operandsSize);
		}
		return "";
	}

	private String genAbsoluteOper() {
		for ( Atom atom : lineAtoms ) {
			if ( atom instanceof AbsoluteExpr ) {
				return ListingGenerator.genHexFromOperand((Operand)atom,operandsSize);
			}
		}
		return "";
	}
	
}
