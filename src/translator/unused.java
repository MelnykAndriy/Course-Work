	
//	private ParsedLine directiveProcessing(ParsedLine lexerLine) {
//		if ( lexerLine.matches(undefSegEndsPattern) ) {
//			if ( lexerLine.getAtomAt(1).getName().equals("segment") ) {
//				return segmentProcessing(lexerLine);
//			}
//			
//			if ( lexerLine.getAtomAt(1).getName().equals("ends") ) {
//				return endSegmentProcessing ( lexerLine ) ;
//			}
//		}
//
//		if ( lexerLine.strMatches(defRegex) ) {
//			return defDirectiveProcessing(lexerLine);
//		}
//		
//		
//		if ( lexerLine.strMatches("^\\s*end.*$") )
//			return endDirectiveProcessing(lexerLine);
//
//		return lexerLine;
//	}

	
//	private ParsedLine convertAtomsToOperandsInCommands(ParsedLine lexerLine ) {
//		int cmdIndex = lexerLine.firstIndexOf(AtomType.Command);
//		ArrayList < Atom > converted = lexerLine.subArray(0,cmdIndex + 1);
//		ArrayList < Operand > operands = convertToOperands(lexerLine.subArray(cmdIndex + 1));
//		converted.addAll(operands); 
//		return new ParsedLine(lexerLine,converted);
//	}

// without regular expression checks

//protected boolean isIdentifierAllowed(String ident) {
//if ( ident.length() == 0 | Character.isDigit(ident.charAt(0)) ) return false;
//String lowerCaseIdent = ident.toLowerCase();
//int len = ident.length();
//for (int i = 0 ; i < len ; i++) 
//	if ( !Character.isLetter(lowerCaseIdent.charAt(i))
//		 && !allowedChar.contains(lowerCaseIdent.charAt(i))
//		 && !Character.isDigit(lowerCaseIdent.charAt(i) ) ) 
//		return false;
//return true;
//}


//int len = lowerCaseToken.length();
//if ( lowerCaseToken.endsWith(":") ) {
//			if (isIdentifierAllowed(lowerCaseToken.substring(0, len - 2 )) )
//				return AtomType.Label;
//			throw new IncorrectLabelName();
//} else {
//	String presumablyNumb = lowerCaseToken.substring(0,len - 2);
//	try {
//		switch(lowerCaseToken.charAt(len-1)) {
//			case 'd':
//				Integer.parseInt(presumablyNumb,10);
//				return AtomType.Constant;
//			case 'b':
//				Integer.parseInt(presumablyNumb,2);
//				return AtomType.Constant;
//			case 'h':
//				Integer.parseInt(presumablyNumb,16);
//				return AtomType.Constant;
//			case 'o':
//				Integer.parseInt(presumablyNumb,8);
//				return AtomType.Constant;
//			case 'q':
//				Integer.parseInt(presumablyNumb,8);
//				return AtomType.Constant;	
//			default:
//				Integer.parseInt(lowerCaseToken,10);
//				return AtomType.Constant;
//		}
//	} catch (NumberFormatException exc) {
//		if ( Character.isDigit(lowerCaseToken.charAt(0) ) ) throw new BadConstant();
//	}
//}


	
//	
//	
//	
//	public void PrintOriginFile () {
//		for (ParsedLine line : syntaxerProduct) {
//			System.out.println(Lexer.buildStringFromAtoms( line.getAtoms() ));
//		}
//	}







//
//
//package translator.syntax;
//
//import java.io.PrintWriter;
//import java.util.ArrayList;
//
//import translator.Printable;
//import translator.Translator;
//import translator.errorhandling.ErrorReporter;
//import translator.lexer.ParsedLine;
//import translator.table.SymbolTable;
//import translator.table.tablecomponents.Atom;
//import translator.table.tablecomponents.AtomType;
//import translator.table.tablecomponents.Command;
//import translator.table.tablecomponents.Directive;
//import translator.table.tablecomponents.Identifier;
//import translator.table.tablecomponents.Label;		
//import translator.table.tablecomponents.Mnemocode;
//import translator.table.tablecomponents.Segment;
//import translator.table.tablecomponents.Variable;
//
//public class Parser extends Printable {
//	private SymbolTable tabRef;
//	private ArrayList < ParsedLine > syntaxerProduct;
//
//	
////	private Segment curParsedSegment;
////	private ErrorReporter reporter;
////	private boolean isEndProcessed;
//	
//// Patterns 
//	final static ArrayList < AtomType > labelCmdPattern;
//	final static ArrayList < AtomType > cmdPattern;
//	final static ArrayList < AtomType > labelPattern;
//	final static ArrayList < AtomType > segEndsDirectivePattern;
//	
//// Regular expressions
//	final static String defRegex = "^\\s*("+ Identifier.identRegex + ")\\s+([dD][bBwWdD])\\s+\\S*\\s*$";
//	final static String endDirectiveRegex = "^\\s*end\\s*"+ "(" + Identifier.identRegex + ")\\s*$";
//	
//	static {
//		labelCmdPattern = new ArrayList < AtomType > () ;
//		labelCmdPattern.add(AtomType.Label);
//		labelCmdPattern.add(AtomType.Command);
//		
//		cmdPattern = new  ArrayList < AtomType >();
//		cmdPattern.add(AtomType.Command);
//
//		labelPattern = new ArrayList < AtomType > () ;
//		labelPattern.add(AtomType.Label);
//		
//		segEndsDirectivePattern = new ArrayList < AtomType > ();
//		segEndsDirectivePattern.add(AtomType.Identifier);
//		segEndsDirectivePattern.add(AtomType.Directive);
//	}	
//	
//	public Parser(SymbolTable tabRef, ArrayList < ParsedLine > lexerProduct) {
//		this.tabRef = tabRef;
////		reporter = new ErrorReporter(Translator.errTab, tabRef);
////		curParsedSegment = null;
////		isEndProcessed = false;
//		syntaxerProduct = new ArrayList < ParsedLine > () ;
//		Analyze(lexerProduct);
//	}
//
//	public synchronized ArrayList<ParsedLine> getTerm() {
//		return syntaxerProduct;
//	}
//	
//	private void Analyze(ArrayList < ParsedLine > lexerProduct) {
//		
//		for ( ParsedLine lexerLine : lexerProduct) {
//			if ( isEndProcessed ) break;
//			
//			if (lexerLine.startsWith( labelPattern ) ) {
//				LabelProcessing(lexerLine);
//				if (lexerLine.getAtoms().size() == 1) {
//					syntaxerProduct.add(lexerLine);
//					continue;
//				}
//			}
//						
//			if ( lexerLine.startsWith(labelCmdPattern) | lexerLine.startsWith(cmdPattern) ) {
//				syntaxerProduct.add(convertAtomsToOperandsInCommands(lexerLine));
//				continue;
//			}
//			
//			if ( lexerLine.strMatches(defRegex) ) {
//				syntaxerProduct.add(defDirectiveProcessing(lexerLine));
//				continue;
//			}
//			
//			if ( lexerLine.firstIndexOf(AtomType.Directive) != -1 ) {
//				syntaxerProduct.add(directiveProcessing(lexerLine));
//				continue;
//			}
//			
//		}
//
//		
//	}
//	
//	private boolean checkIfDef(Atom atom) {
//		return tabRef.isInTable( atom.getName() );
//	}
//	
//	private ParsedLine convertAtomsToOperandsInCommands(ParsedLine lexerLine ) {
//		int cmdIndex = lexerLine.firstIndexOf(AtomType.Command);
//		Command cmd = (Command) tabRef.Search(lexerLine.getIndexName(cmdIndex) ); 
//		ArrayList < Atom > converted = lexerLine.subArray(0,cmdIndex + 1);
//		ArrayList < Operand > operands = convertToOperands(lexerLine.subArray(cmdIndex + 1));
//		
//		if ( curParsedSegment == null) 
//			reporter.reportCodeNotInsideSeg(lexerLine);
//		
//		if ( operands.size() != cmd.getOperandNumb() ) 
//			reporter.reportWrongOperandNumb(lexerLine);
//		
//		for (int i = 0; i < operands.size(); i++ )
//			if ( operands.get(i).isMissing() )
//				reporter.reportMissingOperand(lexerLine,i + 1);
//
//		converted.addAll(operands); 
//		return new ParsedLine(lexerLine,converted);
//	}
//	
//	private ArrayList < Operand > convertToOperands(ArrayList < Atom > from) {
//		ArrayList < Operand > retLst = new ArrayList < Operand > ();
//		int prevSeparator = 0 , i = 0;
//
//		while ( i < from.size() ) {
//			if (from.get(i).getType() == AtomType.Separator ) {
//				retLst.add(new Operand(new ArrayList < Atom >(from.subList(prevSeparator,i)) ));
//				prevSeparator = i + 1;
//			}
//			i++;
//		}
//		if ( !( i == 0 & prevSeparator == 0 ) )  
//			retLst.add(new Operand(new ArrayList < Atom >(from.subList(prevSeparator,i))));
//		
//		return retLst;
//	}
//	
//	private ParsedLine defDirectiveProcessing(ParsedLine lexerLine) {		 
//		Variable defVariable = new Variable( (Identifier)lexerLine.getAtomAt(0),
//											 Variable.whatType((Directive) lexerLine.getAtomAt(1)));
//		ArrayList < Operand > initVals = convertToOperands(lexerLine.subArray(2));
//		if ( initVals.size() > 1) 
//			reporter.reportWrongOperandNumb(lexerLine);
//		Operand initVal = initVals.get(0);
//		
//		if ( curParsedSegment == null) 
//			reporter.reportNotInsideSegmentDef(lexerLine);
//		else {
//			if ( defVariable.Size() < initVal.calcsSizeInBytes() )
//				reporter.reportInitConstantTooBig(lexerLine);
//			else {
//				if ( checkIfDef( defVariable ) ) 
//					reporter.reportVariableAlreadyDef(lexerLine);
//				else {
//					curParsedSegment.defVariable(defVariable);					
//					tabRef.AddSymbol( defVariable );
//				}
//			}
//		}
//					
//		ArrayList < Atom > processed = new ArrayList < Atom > ();
//		processed.add(defVariable);
//		processed.add(lexerLine.getAtomAt(1));
//		processed.add(initVal);
//		return new ParsedLine(lexerLine,processed); 
//	}
//	
//	private ParsedLine directiveProcessing(ParsedLine lexerLine) {
//		if ( lexerLine.matches(segEndsDirectivePattern) ) {
//			if ( lexerLine.getAtomAt(1).getName().equals("segment") ) {
//				return segmentProcessing(lexerLine);
//			}
//			
//			if ( lexerLine.getAtomAt(1).getName().equals("ends") ) {
//				return endSegmentProcessing ( lexerLine ) ;
//			}
//		}
//		
//		if ( isEndDirective(lexerLine) )
//			return endDirectiveProcessing(lexerLine);
//		
//		reporter.reportDirectiveUsage(lexerLine);
//		return lexerLine;
//	}
//	
//	private ParsedLine segmentProcessing(ParsedLine lexerLine) {
//		if ( curParsedSegment != null ) 
//			reporter.reportSegmentNotClosed(lexerLine);
//		
//		Identifier ident = (Identifier) lexerLine.getAtomAt(0);
//		if ( checkIfDef(ident) && !tabRef.isSegment( ident.getName() ) )
//			reporter.reportSegmentAlreadyDef(lexerLine);
//		else {
//			Segment seg = new Segment(ident.getName(),Segment.SegmentType.dataSeg);	
//			ArrayList < Atom > atoms = new ArrayList < Atom > ();
//			atoms.add(seg);
//			atoms.add(lexerLine.getAtomAt(1));
//			tabRef.AddSymbol( curParsedSegment = seg);
//			return new ParsedLine(lexerLine,atoms); 
//		} 
//		return lexerLine;
//	}
//	
//	private ParsedLine endSegmentProcessing(ParsedLine lexerLine) {
//		if ( curParsedSegment == null | 
//			 !curParsedSegment.getName().equals(lexerLine.getAtomAt(0).getName() )) 
//			reporter.reportSegmentNotOpened( lexerLine );
//		else {
//			ArrayList < Atom > atoms = new ArrayList < Atom > ();
//			atoms.add(curParsedSegment);
//			atoms.add(lexerLine.getAtomAt(1));
//			curParsedSegment = null;
//			return new ParsedLine(lexerLine,atoms);
//		}
//		return lexerLine;
//	}
//		
//	private boolean isEndDirective(ParsedLine line) {
//		if ( line.strMatches("^\\s*end.*$") ) {
//			return true;
//		}
//		return false;
//	}
//	private ParsedLine endDirectiveProcessing(ParsedLine lexerLine) {
//		isEndProcessed = true;
//		if ( lexerLine.strMatches("^\\s*end\\s*$") ) {
//			reporter.reportMissingOperand(lexerLine ,-1);
//		} else {
//			if ( lexerLine.getLine().matches(endDirectiveRegex) ) 
//				return correctEndDirectiveUsage(lexerLine);
//		}
//		return lexerLine;
//	}
//	
//	private ParsedLine correctEndDirectiveUsage (ParsedLine lexerLine) {
//		ArrayList < Atom > atoms = new ArrayList < Atom > () ;
//		Atom operand;
//		atoms.add(lexerLine.getAtomAt(0));
//		if ( !tabRef.isLabel(lexerLine.getAtomAt(1).getName()) ) {
//			reporter.reportLabelExpected(lexerLine);
//			operand = lexerLine.getAtomAt(1);
//		} else 
//			operand = tabRef.Search(lexerLine.getAtomAt(1).getName());
//
//		ArrayList < Atom > imagineOpAtoms = new ArrayList< Atom >();
//		imagineOpAtoms.add(operand);
//		atoms.add( new Operand(imagineOpAtoms));
//		return new ParsedLine(lexerLine,atoms);
//	}	
//	
//	private void LabelProcessing(ParsedLine lexerLine) {
//		Label curLabel = (Label) lexerLine.getAtomAt(0);
//		if (  checkIfDef(curLabel) )
//			reporter.reportAlreadyDefLabel(lexerLine);
//		else 
//			tabRef.AddSymbol( curLabel );
//	}
//			
//	
//	@Override
//	public void genOutput(PrintWriter writer) {
//		prnHeader(writer);
//		for ( ParsedLine line : syntaxerProduct ) {
//			try {
//				prnLineStructureInfo(writer,line);
//			} catch (IndexOutOfBoundsException e) {
//				continue;
//			} finally  {
//				writer.println();
//			}
//		}
//	}
//	
//	private void prnHeader(PrintWriter writer) {
//		writer.printf("%-8s %-20s %-20s %-20s %-20s %-20s\n","| Line ¹","| Name/Label field","| Mnemocode field",
//				  "| First operand","| Second operand","| Rest operands |");
//		writer.printf("%-8s %-20s %-10s%-10s %-10s%-10s %-10s%-10s %-10s%-10s\n","|","| token ¹","| first  |","  number","| first  |","  number"
//								,"| first  |","  number","|","...");
//	}
//	
//	private void prnLineStructureInfo(PrintWriter writer,ParsedLine line) {
//		writer.printf("%-3s%-5d ","|",line.getLineNumb() );
//		ArrayList < Atom > atoms = line.getAtoms();
//			int tokenNumb = 1;
//		int iter = 0;
//		if ( atoms.get(iter) instanceof Identifier) {
//			writer.printf("| %8d%-10s ",tokenNumb++,"");
//			iter++;
//		} else 
//			writer.printf("|%20s","");
//
//		if ( atoms.get(iter) instanceof Mnemocode) {
//			writer.printf("| %-7d| %-9d ",tokenNumb++,1);
//			iter++;
//		}
//		for ( int i = iter; i < atoms.size(); i++ ) {
//			if ( atoms.get(i) instanceof Operand) {
//				writer.printf("| %-7d| %-9d ",tokenNumb,((Operand) atoms.get(i)).tokenNumb());
//				tokenNumb +=((Operand) atoms.get(i) ).tokenNumb() + 1;
//			}
//		
//		}
//	}
//
//}


	