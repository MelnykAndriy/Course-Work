package translator.termworks.views;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.userdefined.*;
import translator.termworks.TermIterator;
import translator.termworks.generating.CommandListingGenerator;
import translator.termworks.generating.ListingGenerator;

public class SecondViewer extends TermIterator {
	private ArrayList < ParsedLine > term;
	private SymbolTable symTab;
	private CommandListingGenerator lineSizeCounter;
	private Segment curSeg;
	
	public SecondViewer(SymbolTable symTab) {
		this.symTab = symTab;
		this.term = new ArrayList < ParsedLine > ();
		lineSizeCounter = new CommandListingGenerator();
	}

	public void view(ArrayList < ParsedLine > term) {
		iterateOverTerm(term);
	}
		
	public synchronized ArrayList<ParsedLine> getTerm() {
		return term;
	}

	@Override
	protected void whenLabelMatched() {
		((Label) matchedLine.getAtomAt(0)).setOffset(curSeg.getByteSize());
	}

	@Override
	protected void whenDirectiveMatched() {

		if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
			curSeg = (Segment) matchedLine.getAtomAt(0);
			return;
		}

		if ( matchedLine.strMatches(defRegex) ) {
			((Variable ) matchedLine.getAtomAt(0)).setOffset(curSeg.getByteSize());
			matchedLine.setLineByteSize(((Variable) matchedLine.getAtomAt(0) ).Size());
			curSeg.incSize(matchedLine.getLineByteSize());
			
			return;
		}
	}

	@Override
	protected void whenCommandMatched() { 
		matchedLine.setLineByteSize(lineSizeCounter.calcLineOffset(matchedLine,curSeg));
		curSeg.incSize(matchedLine.getLineByteSize());
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
