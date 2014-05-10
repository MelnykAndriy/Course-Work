package translator.termworks.generating;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.table.tablecomponents.Variable;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;

public class ListingGenerator extends TermIterator {
	private SymbolTable symTab;
	private PrintWriter dest;
	private ArrayList < ParsedLine > term;
	
	public ListingGenerator(SymbolTable symTab, ArrayList<ParsedLine> term) {
		this.symTab = symTab;
		this.term = term;
	}

	@Override
	public void genOutput(PrintWriter writer) {
		dest = writer;
		iterateOverTerm(term);
	}

	
	@Override
	protected void whenLabelMatched() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void whenCommandMatched() {
		dest.printf("%s %s %s %s %s",
				genOpCode(),
				genModRM(),
				genSib(),
				genOffset(),
				genAbsoluteOper());
	}
	
	private String genOpCode() {
		// TODO Auto-generated method stub
		return null;
	}

	private String genModRM() {
		
		// TODO Auto-generated method stub
		return null;
	}

	private String genSib() {
		
		// TODO Auto-generated method stub
		return null;
	}

	private String genOffset() {
		
		// TODO Auto-generated method stub
		return null;
	}

	private String genAbsoluteOper() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.matches(defSegEndsPattern) ) {
			if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
				
				return;
			}
			
			if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
		
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
		
		dest.printf("%s %s\n",
						buildDefaultHexRep(var.getOffset(),2),
						genHexFromOperand(oper,var.Size()));
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
	
	private String genHexFromOperand(Operand oper,int byteSize) {
		if ( oper instanceof AbsoluteExpr ) {
			return ListingGenerator.buildDefaultHexRep(
							(int) ((AbsoluteExpr) oper).evalAbsoluteExpr().GetVaue(),
							byteSize
						);
		}
		return " ERROR ";
	}
	
}
