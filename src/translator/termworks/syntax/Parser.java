package translator.termworks.syntax;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.Directive;
import translator.table.tablecomponents.reserved.Mnemocode;
import translator.table.tablecomponents.userdefined.Identifier;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;
import translator.termworks.StopIterate;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.Operand;

public class Parser extends TermIterator {
	private ArrayList < ParsedLine > term;
	private boolean isEndProcessed;

	public Parser(SymbolTable tabRef, ArrayList < ParsedLine > lexerProduct) {
		isEndProcessed = false;
		term = new ArrayList < ParsedLine > () ;
		Analyze(lexerProduct);
	}

	public synchronized ArrayList<ParsedLine> getTerm() {
		return term;
	}
	
	private void Analyze(ArrayList < ParsedLine > lexerProduct) {
		iterateOverTerm(lexerProduct);
	}
	
	@Override
	protected void beforeStartMatching() throws StopIterate {
		if ( isEndProcessed ) throw new StopIterate();
	}

	@Override
	protected void whenNotMatched() {
		term.add(matchedLine);
	}

	@Override
	protected void whenLabelMatched() {
		if (matchedLine.getAtoms().size() == 1) {
			term.add(matchedLine);
		}
	}

	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.matches(undefSegEndsPattern) ) {
			if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
				term.add(segmentProcessing(matchedLine));
				return;
			}
			
			if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
				term.add(endSegmentProcessing ( matchedLine ));
				return;
			}
		}

		if ( matchedLine.strMatches(defRegex) ) {
			term.add(defDirectiveProcessing(matchedLine));
			return;
		}
		
		
		if ( matchedLine.strMatches("^\\s*end.*$") ) {
			term.add(endDirectiveProcessing(matchedLine));
			return;
		}

		term.add(matchedLine);
	}

	@Override
	protected void whenCommandMatched() {
		int cmdIndex = matchedLine.firstIndexOf(AtomType.Command);
		ArrayList < Atom > converted = matchedLine.subArray(0,cmdIndex + 1);
		ArrayList < Operand > operands = convertToOperands(matchedLine.subArray(cmdIndex + 1));
		converted.addAll(operands); 
		term.add(new ParsedLine(matchedLine,converted));		
	}
	
	private ArrayList < Operand > convertToOperands(ArrayList < Atom > from) {
		ArrayList < Operand > retLst = new ArrayList < Operand > ();
		int prevSeparator = 0 , i = 0;

		while ( i < from.size() ) {
			if (from.get(i).getType() == AtomType.Separator ) {
				retLst.add(Operand.makeOperand(new ArrayList < Atom >(from.subList(prevSeparator,i)) ));
				prevSeparator = i + 1;
			}
			i++;
		}
		if ( !( i == 0 && prevSeparator == 0 ) )  
			retLst.add(Operand.makeOperand(new ArrayList < Atom >(from.subList(prevSeparator,i))) );
		
		return retLst;
	}
	
	private ParsedLine defDirectiveProcessing(ParsedLine lexerLine) {
		Variable defVariable = new Variable( lexerLine.getAtomAt(0).getName(),
											 Variable.whatType((Directive) lexerLine.getAtomAt(1)));
		ArrayList < Atom > processed = new ArrayList < Atom > ();
		processed.add(defVariable);
		processed.add(lexerLine.getAtomAt(1));
		processed.addAll(convertToOperands(lexerLine.subArray(2)));
		return new ParsedLine(lexerLine,processed);
	}
	
	private ParsedLine segmentProcessing(ParsedLine lexerLine) {
		Segment seg = new Segment(lexerLine.getAtomAt(0).getName(),Segment.SegmentType.bit32);	
		ArrayList < Atom > atoms = new ArrayList < Atom > ();
		atoms.add(seg);
		atoms.add(lexerLine.getAtomAt(1));
		return new ParsedLine(lexerLine,atoms); 
	}
	
	private ParsedLine endSegmentProcessing(ParsedLine lexerLine) {
		Segment seg = new Segment(lexerLine.getAtomAt(0).getName(),Segment.SegmentType.bit32);
		ArrayList < Atom > atoms = new ArrayList < Atom > ();
		atoms.add(seg);
		atoms.add(lexerLine.getAtomAt(1));
		return new ParsedLine(lexerLine,atoms);
	}
		
	private ParsedLine endDirectiveProcessing(ParsedLine lexerLine) {
		isEndProcessed = true;
		ArrayList < Atom > atoms = new ArrayList < Atom > () ;		
		atoms.add(lexerLine.getAtomAt(0));
		atoms.addAll(convertToOperands(lexerLine.subArray(1)));
		return new ParsedLine(lexerLine,atoms);
	}

	@Override
	public void genOutput(PrintWriter writer) {
		prnHeader(writer);
		for ( ParsedLine line : term ) {
			try {
				prnLineStructureInfo(writer,line);
			} catch (IndexOutOfBoundsException e) {
				continue;
			} finally  {
				writer.println();
			}
		}
	}
	
	private void prnHeader(PrintWriter writer) {
		writer.printf("%-8s %-20s %-20s %-20s %-20s %-20s\n","| Line ¹","| Name/Label field","| Mnemocode field",
				  "| First operand","| Second operand","| Rest operands |");
		writer.printf("%-8s %-20s %-10s%-10s %-10s%-10s %-10s%-10s %-10s%-10s\n","|","| token ¹","| first  |","  number","| first  |","  number"
								,"| first  |","  number","|","...");
	}
	
	private void prnLineStructureInfo(PrintWriter writer,ParsedLine line) {
		writer.printf("%-3s%-5d ","|",line.getLineNumb() );
		ArrayList < Atom > atoms = line.getAtoms();
			int tokenNumb = 1;
		int iter = 0;
		if ( atoms.get(iter) instanceof Identifier) {
			writer.printf("| %8d%-10s ",tokenNumb++,"");
			iter++;
		} else 
			writer.printf("|%20s","");

		if ( atoms.get(iter) instanceof Mnemocode) {
			writer.printf("| %-7d| %-9d ",tokenNumb++,1);
			iter++;
		}
		for ( int i = iter; i < atoms.size(); i++ ) {
			if ( atoms.get(i) instanceof Operand) {
				writer.printf("| %-7d| %-9d ",tokenNumb,((Operand) atoms.get(i)).tokenNumb());
				tokenNumb +=((Operand) atoms.get(i) ).tokenNumb() + 1;
			}
		}
	}

}