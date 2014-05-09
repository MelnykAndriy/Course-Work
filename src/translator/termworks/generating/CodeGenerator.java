package translator.termworks.generating;

import java.io.PrintWriter;
import java.util.ArrayList;

import translator.Printable;
import translator.lexer.ParsedLine;
import translator.table.SymbolTable;

public class CodeGenerator extends Printable {
	private SymbolTable symTab;
	private ArrayList < ParsedLine > term;
	
	public CodeGenerator(SymbolTable symTab, ArrayList<ParsedLine> term) {
		super();
		this.symTab = symTab;
		this.term = term;
	}

	public void genOutput(PrintWriter writer) {
		for ( ParsedLine line : term) {
			// TODO generate listing line
		}
	}
	
	

}
