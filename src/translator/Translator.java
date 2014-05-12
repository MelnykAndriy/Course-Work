/* 
 * File : Translator.java
 * Autor : Andriy Melnyk
 * Purpose : Assembler Compiler main class definition
 * created : 20.02.14
 *
 */

package translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import translator.termworks.views.*;
import translator.errorhandling.*;
import translator.termworks.generating.ListingGenerator;
import translator.lexer.Lexer;
import translator.termworks.syntax.Parser;
import translator.table.SymbolTable;
import translator.termworks.checker.GrammarChecker;
    
public class Translator {
	public static SymbolTable mainTab = new SymbolTable(); 
	public static CompilerFlags flags = new CompilerFlags();
	public static ErrorsTable errTab = new ErrorsTable();
	
    public static void main(String[] args) {
    	flags.ParseArgs(args);
		
    	// lexical analyzer entry point
    	Lexer lex = new Lexer(mainTab,new File(flags.getIFile()));
    	if ( flags.isPrnLexer() ) prnProduct(lex,"lexer");
    	if ( errTab.isCritical() ) stopBuild();
    	    	
    	// parser entry point
    	Parser syn = new Parser(mainTab,lex.getTerm()); 
    	if ( flags.isPrnSyntaxer() ) prnProduct(syn,"syntaxer");
  	
    	GrammarChecker checker = new GrammarChecker(errTab,mainTab);
    	checker.check(syn.getTerm(), GrammarChecker.AvailableChecks.FirsViewChecks );
    	if ( errTab.isCritical() ) stopBuild();
    	    	
    	FirstViewer firstViewer = new FirstViewer(mainTab);
    	firstViewer.view(syn.getTerm());
    	firstViewer.genOutput(System.out);
    	
    	checker.check(syn.getTerm(), GrammarChecker.AvailableChecks.SecondViewChecks );
    	if ( errTab.isCritical() ) stopBuild();

 //   	SecondViewer secondViewer = new SecondViewer(mainTab);
  //  	secondViewer.view(firstViewer.getTerm());

    
//    	try {
    		ListingGenerator listingGenerator = new ListingGenerator(mainTab,firstViewer.getTerm());
    		listingGenerator.genOutput(System.out);
    		errTab.PrintFoundErrors();
//    		listingGenerator.genOutput(flags.getOFile());	// generating of listing file
//		} catch (FileNotFoundException exc) {
//			System.err.println("Error while creating output file : " + exc.getMessage());
//		}
  	
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