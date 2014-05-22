package translator.termworks.generating;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;

public class ListingGenerator extends TermIterator {
	private PrintWriter dest;
	private ArrayList < ParsedLine > term;
	private CommandListingGenerator localCmdGen;
	private Segment curSeg;
	private int curOffset;
	private int lineIter;
	
	public ListingGenerator(ArrayList<ParsedLine> term) {
		this.term = term;
	}

	@Override
	public void genOutput(PrintWriter writer) {
		dest = writer;
		localCmdGen = new CommandListingGenerator();
		lineIter = 1;
		iterateOverTerm(term);
	}
	
	@Override
	protected void beforeStartMatching() throws StopIterate {
		for ( ; lineIter < matchedLine.getLineNumb() ; lineIter++ )
			dest.printf("%-4s%-8d\n","",lineIter);
		dest.printf("%-4s%-8d","",lineIter++);
	}

	@Override
	protected void whenLabelMatched() {
		if ( matchedLine.getAtoms().size() == 1 ) 
			dest.printf("%-26s %s\n",buildDefaultHexRep(curOffset, curSeg.size()),matchedLine);
	}

	@Override
	protected void whenCommandMatched() {
		dest.printf("%-30s %s\n", localCmdGen.generate(matchedLine,curSeg,curOffset),matchedLine);
		curOffset += matchedLine.getLineByteSize();
	}
	
	@Override
	protected void whenBadFormed() {
		dest.printf("%-30s %s\n",buildDefaultHexRep(curOffset,(curSeg != null)?(curSeg.size()):(2)), matchedLine );
		dest.printf("%-12sError : %s\n","",matchedLine.getErr());
	}
	
	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.matches(defSegEndsPattern) ) {
			if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
				curSeg = (Segment) matchedLine.getAtomAt(0);
				curOffset = 0;
				dest.printf("%-26s %s\n",buildDefaultHexRep(curOffset, curSeg.size()),matchedLine);
				return;
			}
			
			if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
				dest.printf("%-26s %s\n",buildDefaultHexRep(curOffset, curSeg.size()),matchedLine);
				return;
			}
		}
		
		if ( matchedLine.strMatches(defRegex) ) {
			generateVarDef();
			return;
		}
		
		if ( matchedLine.strMatches("^\\s*end.*$") ) {
			dest.printf("%-30s %s\n","",matchedLine);
		}

	}

	private void generateVarDef() {
		Variable var = (Variable) matchedLine.getAtomAt(0);
		Operand oper = (Operand) matchedLine.getAtomAt(2);
		String bytes = String.format("%s %s", 
				buildDefaultHexRep(curOffset, curSeg.size()),
				genHexFromOperand(oper,var.Size()));
		dest.printf("%-30s %s\n",bytes,matchedLine);
		curOffset += matchedLine.getLineByteSize();
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
		
}
