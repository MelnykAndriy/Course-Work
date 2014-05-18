package translator.termworks.generating;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;

public class ListingGenerator extends TermIterator {
	private SymbolTable symTab;
	private PrintWriter dest;
	private ArrayList < ParsedLine > term;
	private CommandListingGenerator localCmdGen;
	private SegmentInfo curSegInf;
	
	public ListingGenerator(SymbolTable symTab, ArrayList<ParsedLine> term) {
		this.symTab = symTab;
		this.term = term;
	}

	@Override
	public void genOutput(PrintWriter writer) {
		dest = writer;
		localCmdGen = new CommandListingGenerator(symTab);
		iterateOverTerm(term);
	}

	
	@Override
	protected void whenLabelMatched() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void whenCommandMatched() {
		dest.printf("%-50s %s\n", localCmdGen.generate(matchedLine,curSegInf),matchedLine);
	}
	
	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.matches(defSegEndsPattern) ) {
			if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
				curSegInf = new SegmentInfo(0, (((Segment) matchedLine.getAtomAt(0)).getSegmentType() == Segment.SegmentType.bit16)?(2):(4) );
				dest.printf("%-46s %s\n",curSegInf.offsetToString(),matchedLine);
				return;
			}
			
			if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
				dest.printf("%-46s %s\n",curSegInf.offsetToString(),matchedLine);
				return;
			}
		}
		
		if ( matchedLine.strMatches(defRegex) ) {
			generateVarDef();
			return;
		}
		
//		if ( matchedLine.strMatches("^\\s*end.*$") ) {
//
//		}

	}

	private void generateVarDef() {
		Variable var = (Variable) matchedLine.getAtomAt(0);
		Operand oper = (Operand) matchedLine.getAtomAt(2);
		String bytes = String.format("%s %s", 
				buildDefaultHexRep(curSegInf.offset(),curSegInf.size()),
				genHexFromOperand(oper,var.Size()));
		dest.printf("%-50s %s\n",bytes,matchedLine);
		curSegInf.inc(var.Size());
	}
	
	public static String buildDefaultHexRep(int value,int byteSize) {
		StringBuffer fixed = new StringBuffer(Integer.toHexString(value).toUpperCase());
		int DEFALT_CHARACTER_NUMB = 2*byteSize;
		fixed.reverse();
		if ( value < 0 ) {
			return fixed.reverse().toString().substring(fixed.length() - DEFALT_CHARACTER_NUMB);
		} else {
			while (DEFALT_CHARACTER_NUMB > fixed.length() )
				fixed.append("0");
			return fixed.reverse().toString();
		}
	}
	
	static String genHexFromOperand(Operand oper,int byteSize) {
		if ( oper instanceof AbsoluteExpr ) {
			return ListingGenerator.buildDefaultHexRep(
							(int) ((AbsoluteExpr) oper).evalAbsoluteExpr().GetVaue(),
							byteSize
						);
		}
		return " ERROR ";
	}
	
	public class SegmentInfo { 
		private int curSegmentOffset;
		private int segmentSize;
		
		public SegmentInfo(int offset,int size) {
			curSegmentOffset = offset;
			segmentSize = size;
		}
		
		public void inc(int n) {
			curSegmentOffset += n;
		}
		
		public int offset() {
			return curSegmentOffset;
		}
		
		public int size() {
			return segmentSize;
		}
		
		public String offsetToString() {
			return buildDefaultHexRep(curSegInf.offset(),curSegInf.size());
		}
	}
	
}
