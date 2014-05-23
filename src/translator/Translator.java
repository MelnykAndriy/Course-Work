/* 
 * File : Translator.java
 * Author : Andriy Melnyk
 * Purpose : Assembler Compiler main class definition
 * created : 20.02.14
 *
 */

package translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import translator.termworks.views.*;
import translator.errorhandling.*;
import translator.termworks.generating.ListingGenerator;
import translator.lexer.Lexer;
import translator.termworks.syntax.Parser;
import translator.table.SymbolTable;
import translator.termworks.checker.GrammarChecker;
    
public class Translator {
	public static ErrorsTable errTab = new ErrorsTable();
	
    public static void main(String[] args) { 
    	long start = System.currentTimeMillis();
    	
    	CompilerFlags flags = new CompilerFlags();
    	flags.ParseArgs(args);
    	SymbolTable mainTab = new SymbolTable();
    	
    	// lexical analyzer entry point
    	Lexer lex = new Lexer(mainTab,new File(flags.getIFile()));
    	if ( flags.isPrnLexer() ) prnProduct(lex,"lexer");
    	    	
    	// parser entry point
    	Parser syn = new Parser(mainTab,lex.getTerm()); 
    	if ( flags.isPrnSyntaxer() ) prnProduct(syn,"syntaxer");
  	
    	GrammarChecker checker = new GrammarChecker(errTab,mainTab);
    	checker.check(syn.getTerm(), GrammarChecker.AvailableChecks.FirsViewChecks );
    	
    	FirstViewer firstViewer = new FirstViewer(mainTab);
    	firstViewer.view(syn.getTerm());
    	
    	checker.check(syn.getTerm(), GrammarChecker.AvailableChecks.SecondViewChecks );
    	SecondViewer secondViewer = new SecondViewer(mainTab);
    	secondViewer.view( firstViewer.getTerm() );

    	try {
    		ListingGenerator listingGenerator = new ListingGenerator(firstViewer.getTerm());
    		errTab.PrintFoundErrors();
    		listingGenerator.genOutput(flags.getOFile());	// generating of listing file
    		PrintWriter toAppend = new PrintWriter(new FileOutputStream(flags.getOFile(), true));
    		toAppend.println();
    		errTab.prnSummary(toAppend);
    		secondViewer.genOutput(toAppend);
    		toAppend.close();
    	} catch (FileNotFoundException exc) {
			System.err.println("Error while creating output file : " + exc.getMessage());
		}
    	
    	System.out.println("Time spent	 " + (System.currentTimeMillis() - start));
    }
    
    public static void stopBuild() {
    	errTab.PrintFoundErrors();
		System.err.println("Build was stopped.");
		System.exit(1);
    }
    
    public static void  prnProduct(Printable whatToPrn,String transPart) {
		try {
			whatToPrn.genOutput(transPart + "-output.log");
		} catch (IOException e) {
			System.err.println("Error while creating " + transPart + " output file : " + e.getMessage());
		}
    }
    
}