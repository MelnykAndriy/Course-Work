package translator.termworks;

import java.util.ArrayList;

import translator.Printable;
import translator.lexer.ParsedLine;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.userdefined.Identifier;

public abstract class TermIterator extends Printable {
	public ParsedLine matchedLine;
	

// Regular expressions
	public final static String defRegex = "^\\s*("+ Identifier.identRegex + ")\\s+([dD][bBwWdD])\\s+.*\\s*$";
// Patterns 
	public final static ArrayList < AtomType > labelCmdPattern;
	public final static ArrayList < AtomType > cmdPattern;
	public final static ArrayList < AtomType > labelPattern;
	public final static ArrayList < AtomType > undefSegEndsPattern;
	public final static ArrayList < AtomType > defSegEndsPattern;
	
	static {
		labelCmdPattern = new ArrayList < AtomType > () ;
		labelCmdPattern.add(AtomType.Label);
		labelCmdPattern.add(AtomType.Command);
		
		cmdPattern = new  ArrayList < AtomType >();
		cmdPattern.add(AtomType.Command);

		labelPattern = new ArrayList < AtomType > () ;
		labelPattern.add(AtomType.Label);
		
		undefSegEndsPattern = new ArrayList < AtomType > ();
		undefSegEndsPattern.add(AtomType.Identifier);
		undefSegEndsPattern.add(AtomType.Directive);
		
		defSegEndsPattern = new ArrayList < AtomType > ();
		defSegEndsPattern.add(AtomType.Segment);
		defSegEndsPattern.add(AtomType.Directive);
	}

	
	protected void iterateOverTerm(ArrayList < ParsedLine > term) {
		try {
			for ( ParsedLine  line : term  ) {
				matchedLine = line;
				beforeStartMatching();
				if ( matchedLine.isInvalid()  ) {
					whenBadFormed();
					continue;
				}
				
				if (line.startsWith( labelPattern ) ) {
					whenLabelMatched();
					if ( line.getAtoms().size() == 1) 
						continue;
				}
					
				if ( line.firstIndexOf(AtomType.Directive) != -1 ) {
					whenDirectiveMatched();
					continue;
				}
				
				if ( line.startsWith(labelCmdPattern) | line.startsWith(cmdPattern) ) {
					whenCommandMatched();
					continue;
				}
				
				whenNotMatched();
				matchedLine = null;
			}
		} catch (StopIterate e) { 	}
	}
	
	protected void whenBadFormed() { }

	protected void beforeStartMatching() throws StopIterate { } 
	
	protected void whenNotMatched() { } 
	
	protected abstract void whenLabelMatched() ;
	
	protected abstract void whenDirectiveMatched() ;

	protected abstract void whenCommandMatched() ;
	
	public class StopIterate extends Exception {
		private static final long serialVersionUID = 6943052227835058732L;
	}
	
}
