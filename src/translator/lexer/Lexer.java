package translator.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import translator.Printable;
import translator.Translator;
import translator.errorhandling.ErrorsTable.ErrIdent;
import translator.exc.BadConstant;
import translator.exc.NoSuchAtomException;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;

public class Lexer extends Printable {
	private SymbolTable tableRef;
	private ArrayList< ParsedLine > lexerProduct = null ;
	
	public Lexer(SymbolTable tab,File f) {
		tableRef = tab ;
		Scanner srcScn = null;
		try {
			srcScn = new Scanner(f);
			lexerProduct = Analyze(srcScn);
		} catch (FileNotFoundException e) {
 			System.err.println("Source file " + f.getName() + " not found.");
 			System.exit(1);
		} finally {
			if ( srcScn != null ) srcScn.close();
		}
	}
		
	public ArrayList< ParsedLine  > getTerm() {
		return lexerProduct;
	}
	
	public void genOutput(PrintWriter writer) {
		for (  ParsedLine line : lexerProduct ) {
			writer.println("Line ¹ " + line.getLineNumb());
			int tokenNumb = 1;
			writer.printf(" ¹  \t%-15s %-10s %-25s%n","token","length","type");
			for (Atom atm : line.getAtoms() )
				writer.printf(" %-2d \t%-15s %-10s %-25s%n" , tokenNumb++, atm.getName(),
						 									  atm.getName().length(), atm); 
			writer.println();
		}
	}
	
	private ArrayList< ParsedLine  > Analyze(Scanner srcScn) {
		ArrayList< ParsedLine  > retList = new ArrayList< ParsedLine  >();
		int lineNumb = 1;
 		
			while ( srcScn.hasNextLine() ) {
				Matcher commentsMatcher = Pattern.compile("([^;]*).*").matcher(srcScn.nextLine());
				commentsMatcher.matches();
				String lineWithoutComments = commentsMatcher.group(1); 
				try {
					if ( lineWithoutComments.trim().length() != 0 ) {					
						 retList.add(new ParsedLine(lineNumb,ParseLine(lineWithoutComments.trim()),lineWithoutComments));
					}
				} catch (NoSuchAtomException e) {
					Translator.errTab.report(ErrIdent.UnexpectedSymbolInToken, 
											 lineNumb, 
											 lineWithoutComments.indexOf(e.tokenWhereFound) + 1);
				} finally {
					lineNumb++;
				}
			}
			
		return retList ;
	}
	
	private ArrayList < Atom > ParseLine(String line) throws NoSuchAtomException {
		ArrayList < Atom > curParsedLine = new ArrayList< Atom >();
		Matcher labelMatcher = Label.labelPattern.matcher(line);
		
	// check if line starts with Label
		if ( labelMatcher.matches() ) {
			curParsedLine.add( new Label(labelMatcher.group(1)) );
			line = labelMatcher.group(2);
		}
		
		StringTokenizer tokensParser = new StringTokenizer(line," ,[]:+*-/.<>()",true);	
		while ( tokensParser.hasMoreTokens() ) {
			
			String lineToken = tokensParser.nextToken().trim();
			if (lineToken.length() != 0) {
				curParsedLine.add( buildAtom(lineToken) );
			}
		}
		
		return curParsedLine;
	}
			
	public  Atom buildAtom(String token) throws NoSuchAtomException {
		String lowerCaseToken = token.toLowerCase();
		if ( tableRef.Search(lowerCaseToken) != null) return tableRef.Search(lowerCaseToken);
		try {
			return new Constant(token);
		} catch (BadConstant e) {
			if ( Identifier.isIdentifierAllowed(lowerCaseToken) ) return new Identifier(token);
			if ( token.equals(",") ) return new Atom(",") {
				public AtomType getType() { return AtomType.Separator; }
				public String toString() { return "Comma separator"; }
				public Atom searchedPosAtom() { return this; }
			};
			
		}
		
		throw new NoSuchAtomException(token);		
	}
	
	public static String buildStringFromAtoms(ArrayList< Atom > atoms) {
		StringBuffer retStr = new StringBuffer();	
		for (Atom atom : atoms) {
			retStr.append(atom.getName() + " ");
		}
		return retStr.toString();
	}
	

}
