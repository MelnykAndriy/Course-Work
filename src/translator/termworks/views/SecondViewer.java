package translator.termworks.views;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.AbstractMap.SimpleEntry;

import translator.lexer.ParsedLine;
import translator.table.*;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.*;
import translator.table.tablecomponents.userdefined.*;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.*;
import translator.termworks.generating.ListingGenerator;

public class SecondViewer extends TermIterator {
	private ArrayList < ParsedLine > term;
	private SymbolTable symTab;
	private CommandByteCalculator lineSizeCalculator;
	private Segment curSeg;
	private Stack < Entry< Integer, ParsedLine >  > postRelativeResolve;
	
	public SecondViewer(SymbolTable symTab) {
		this.symTab = symTab;
		this.term = new ArrayList < ParsedLine > ();
		lineSizeCalculator = new CommandByteCalculator();
		postRelativeResolve = new Stack < Entry< Integer, ParsedLine > > ();
	}

	public void view(ArrayList < ParsedLine > term) {
		postRelativeResolve.clear();
		iterateOverTerm(term);
		resolvePostRelative();
	}
		
	private void resolvePostRelative() {
		for ( Entry< Integer , ParsedLine > entry : postRelativeResolve ) 
			((Relative)entry.getValue().getFirst(AtomType.RelativeOperand)).clarifyOperKind(resolveOperKind(entry.getKey(),entry.getValue() ));
	}

	public synchronized ArrayList<ParsedLine> getTerm() {
		return term;
	}

	@Override
	protected void whenLabelMatched() {
		((Label) matchedLine.getAtomAt(0)).setOffset(curSeg.byteSize());
	}

	@Override
	protected void whenDirectiveMatched() {
		if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
			curSeg = (Segment) matchedLine.getAtomAt(0);
			return;
		}

		if ( matchedLine.strMatches(defRegex) ) {
			((Variable ) matchedLine.getAtomAt(0)).setOffset(curSeg.byteSize());
			matchedLine.setLineByteSize(((Variable) matchedLine.getAtomAt(0) ).Size());
			curSeg.incSize(matchedLine.getLineByteSize());
			
			return;
		}
	}

	@Override
	protected void whenCommandMatched() { 
		if ( matchedLine.getFirst(AtomType.Command) instanceof RelativeCmd ) 
			resolveRelative();
		matchedLine.setLineByteSize(lineSizeCalculator.calculate(matchedLine,curSeg));
		curSeg.incSize(matchedLine.getLineByteSize());
	}
		
	private void resolveRelative() {
		Relative rel = (Relative) matchedLine.getFirst(AtomType.RelativeOperand);
		if ( rel.isDefProcessed() ) 
			rel.clarifyOperKind(resolveOperKind(curSeg.byteSize(),matchedLine));
		else 
			postRelativeResolve.push(new SimpleEntry< Integer, ParsedLine >(curSeg.byteSize(),matchedLine));
	}
	
	private OperandKind resolveOperKind(int fromWhere,ParsedLine line) {
		int cmdIndex = line.firstIndexOf(AtomType.Command);
		Relative rel = (Relative) line.getFirst(AtomType.RelativeOperand);
		Command cmd = (Command) line.getFirst(AtomType.Command);
		OperandOption option = cmd.getOptionForOperands((ArrayList<Operand>) Atom.castCopy(new ArrayList < Operand >(),
				line.subArray(cmdIndex + 1)));
		int distance =  rel.calcDistanceTo(fromWhere,option.opcodeByteSize(),rel.calcSizeInBytes());
		return OperandKind.whatKind(OperandKind.RELATIVE, (distance >= -128 && distance <= 127)?(1):(2));
	}
	
	@Override
	public void genOutput(PrintWriter writer) {
		ArrayList < Segment > allSegments = (ArrayList<Segment>) Atom.castCopy(new ArrayList< Segment >(),symTab.findAll(AtomType.Segment)); 
		writer.println("\n");
		printSegments(writer,allSegments);
		printSegmentsSymbols(writer,allSegments);
	}
	
	private void printSegments(PrintWriter writer, ArrayList< Segment > segments) {
		writer.println("Segments : ");
		writer.println("                N a m e         		Size	Length");
		for (Segment seg : segments ) 
			writer.printf("%-40s%-8s%-8s\n",
									seg.getName().toUpperCase(),
									seg.identTypeToString(),
									ListingGenerator.buildDefaultHexRep(seg.byteSize(),2));
		writer.println();
	}
	
	private void printSegmentsSymbols(PrintWriter writer,ArrayList < Segment > segments) {
		writer.println("Symbols : ");
		writer.println("                N a m e         		Type	 Value	 Attr");
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
