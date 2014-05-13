package translator.termworks.views;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import translator.lexer.ParsedLine;
import translator.termworks.TermIterator;
import translator.termworks.generating.ListingGenerator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.UndefinedOperand;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.userdefined.Identifier;
import translator.table.tablecomponents.userdefined.Label;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;

public class FirstViewer extends TermIterator {
	private SymbolTable symTab;
	private ArrayList < ParsedLine > term;
	private Segment curProcessSeg;
	private Stack < UndefinedOperand >  FixNeededUndefinedOperands;
	
	public FirstViewer(SymbolTable mainTab) {
		this.term = new ArrayList < ParsedLine > ();
		symTab = mainTab;
	}

	public ArrayList < ParsedLine > getTerm() {
		return term;
	}
		
	public void view(ArrayList < ParsedLine > term) {
		iterateOverTerm(term);
	}

	@Override
	protected void whenLabelMatched() {
		Label curLabel = (Label) matchedLine.getAtomAt(0);
		curProcessSeg.defLabel(curLabel);
		symTab.AddSymbol( curLabel );
		term.add(matchedLine);
	}

	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.matches(defSegEndsPattern) ) {
			if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
				segmentDef();
				return;
			}
			
			if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
				endSegmentProcessing();
				return;
			}
		}
		
		if ( matchedLine.strMatches(defRegex) ) {
			varDef();
			return;
		}

//		if ( matchedLine.strMatches("^\\s*end.*$") ) {
//
//		}

	}

	private void segmentDef() {
		symTab.AddSymbol( curProcessSeg = (Segment) matchedLine.getAtomAt(0) );
		term.add(matchedLine);
	}

	private void endSegmentProcessing() {
		curProcessSeg = null;
		term.add(matchedLine);
	}

	private void varDef() {
		curProcessSeg.defVariable( (Variable) matchedLine.getAtomAt(0) );
		symTab.AddSymbol( matchedLine.getAtomAt(0) );
		term.add( calcAbsExprInLine(matchedLine) );
	}

	@Override
	protected void whenCommandMatched() {
		findFixNeeded();
		term.add(calcAbsExprInLine(matchedLine));		
	}
	
	private void findFixNeeded() {
		for ( Atom atom : matchedLine.getAtoms() )
			if ( atom instanceof UndefinedOperand ) 
				FixNeededUndefinedOperands.push((UndefinedOperand) atom);
	}
	
	private ParsedLine calcAbsExprInLine(ParsedLine line) {
		for (Atom  atom : line.getAtoms() ) {
			if ( atom instanceof AbsoluteExpr ) {
				((AbsoluteExpr) atom).calc();
			}
		}
		return line;
	}
	
	@Override
	public void genOutput(PrintWriter writer) {
		ArrayList < Segment > allSegments = symTab.findAll(AtomType.Segment);
		printSegments(writer,allSegments);
		printSegmentsSymbols(writer,allSegments);
	}
	
	private void printSegments(PrintWriter writer, ArrayList< Segment > segments) {
		writer.println("Segments : ");
		writer.println("                N a m e         	Size	Length");
		for (Segment seg : segments ) 
			writer.printf("%-40s%-8s%-8s\n",
									seg.getName().toUpperCase(),
									seg.identTypeToString(),
									ListingGenerator.buildDefaultHexRep(seg.byteSize(),2));
		writer.println();
	}
	
	private void printSegmentsSymbols(PrintWriter writer,ArrayList < Segment > segments) {
		writer.println("Symbols : ");
		writer.println("                N a m e         	Type	 Value	 Attr");
		for (Segment seg : segments ) {
			for (Identifier sym : seg.getDefSymbols() ) {
				writer.printf("%-40s%-9s%-8s%s\n",
											sym.getName().toUpperCase(),
											sym.identTypeToString(),
											ListingGenerator.buildDefaultHexRep(sym.getOffset(),2),
										  	seg.getName().toUpperCase() );
			}
		}
		writer.println();
	}

}
