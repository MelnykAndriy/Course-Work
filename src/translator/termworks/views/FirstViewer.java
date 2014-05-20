package translator.termworks.views;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import translator.lexer.ParsedLine;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;
import translator.termworks.syntax.operands.UndefinedOperand;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.userdefined.*;

public class FirstViewer extends TermIterator {
	private SymbolTable symTab;
	private ArrayList < ParsedLine > term;
	private Segment curProcessSeg;
	private Stack < UndefinedOperandUpdater >  FixNeededUndefinedOperands;
	
	private class UndefinedOperandUpdater {
		private ArrayList < Atom > lineAtoms;
		private int pos;
		
		public UndefinedOperandUpdater(ArrayList<Atom> lineAtoms, int pos) {
			this.lineAtoms = lineAtoms;
			this.pos = pos;
		}

		public void update() {
			lineAtoms.set(pos, Operand.makeOperand( ((UndefinedOperand) lineAtoms.get(pos)).updateSymbolsFromTab(symTab) ));
		}
		
	}
	
	public FirstViewer(SymbolTable mainTab) {
		this.term = new ArrayList < ParsedLine > ();
		FixNeededUndefinedOperands = new Stack < UndefinedOperandUpdater >();
		symTab = mainTab;
	}

	public ArrayList < ParsedLine > getTerm() {
		return term;
	}
		
	public void view(ArrayList < ParsedLine > term) {
		FixNeededUndefinedOperands.clear();
		iterateOverTerm(term);
		termUpdate();
	}

	@Override
	protected void whenLabelMatched() {
		Label curLabel = (Label) matchedLine.getAtomAt(0);
		curLabel.setLineWhereDefined(matchedLine);
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

		if ( matchedLine.strMatches("^\\s*end.*$") ) {
			endDirectiveProcessing();
		}	

	}

	private void endDirectiveProcessing() {
		if ( matchedLine.getAtomAt(1) instanceof UndefinedOperand ) 
			FixNeededUndefinedOperands.push(new UndefinedOperandUpdater(matchedLine.getAtoms(), 1));
		term.add(matchedLine);
	}

	private void segmentDef() {
		symTab.AddSymbol( curProcessSeg = (Segment) matchedLine.getAtomAt(0) );
		term.add(matchedLine);
	}

	private void endSegmentProcessing() {
		matchedLine.getAtoms().set(0,curProcessSeg);
		term.add(matchedLine);
		curProcessSeg = null;
	}

	private void varDef() {
		Variable var = (Variable) matchedLine.getAtomAt(0);
		var.setLineWhereDefined(matchedLine);
		curProcessSeg.defVariable( var );
		symTab.AddSymbol( var );
		term.add( calcAbsExprInLine(matchedLine) );
	}

	@Override
	protected void whenCommandMatched() {
		findFixNeededUndefinedOperands();
		term.add(calcAbsExprInLine(matchedLine));		
	}
	
	private void findFixNeededUndefinedOperands() {
		int i = 0;
		for ( Atom atom : matchedLine.getAtoms() ) {
			if ( atom instanceof UndefinedOperand ) 
				FixNeededUndefinedOperands.push(new UndefinedOperandUpdater(matchedLine.getAtoms(),i));
			i++;
		}
	}
	
	private ParsedLine calcAbsExprInLine(ParsedLine line) {
		for (Atom  atom : line.getAtoms() ) {
			if ( atom instanceof AbsoluteExpr ) {
				((AbsoluteExpr) atom).calc();
			}
		}
		return line;
	}
	
	private void termUpdate() {
		for ( UndefinedOperandUpdater updater : FixNeededUndefinedOperands  ) {
			updater.update();
		}
	}
	
	@Override
	public void genOutput(PrintWriter writer) {
		ArrayList < Segment > allSegments = (ArrayList<Segment>) Atom.castCopy(new ArrayList< Segment >(),symTab.findAll(AtomType.Segment)); 
		printSegments(writer,allSegments);
		printSegmentsSymbols(writer,allSegments);
	}
	
	private void printSegments(PrintWriter writer, ArrayList< Segment > segments) {
		writer.println("Segments : ");
		writer.println("                N a m e         		Size");
		for (Segment seg : segments ) 
			writer.printf("%-40s%-8s\n",
									seg.getName().toUpperCase(),
									seg.identTypeToString());
		writer.println();
	}
	
	private void printSegmentsSymbols(PrintWriter writer,ArrayList < Segment > segments) {
		writer.println("Symbols : ");
		writer.println("                N a m e         		Type	Attr");
		for (Segment seg : segments ) {
			for (Identifier sym : seg.getDefSymbols() ) {
				writer.printf("%-40s%-9s%s\n",
											sym.getName().toUpperCase(),
											sym.identTypeToString(),
										  	seg.getName().toUpperCase() );
			}
		}
		writer.println();
	}

}
