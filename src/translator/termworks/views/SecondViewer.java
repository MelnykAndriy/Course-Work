package translator.termworks.views;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.lexer.ParsedLine;
import translator.table.SymbolTable;
import translator.termworks.TermIterator;

public class SecondViewer extends TermIterator {
	private SymbolTable symTab;
	private ArrayList < ParsedLine > term;
	
	public SecondViewer(SymbolTable mainTab) {
		symTab = mainTab;
		this.term = new ArrayList < ParsedLine > ();
	}

	public void view(ArrayList < ParsedLine > term) {
		iterateOverTerm(term);
	}
		
	public synchronized ArrayList<ParsedLine> getTerm() {
		return term;
	}

	@Override
	protected void whenLabelMatched() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void whenDirectiveMatched() {
		
		
		
	}

	@Override
	protected void whenCommandMatched() {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void genOutput(PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}
	
}
