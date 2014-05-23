
package translator.termworks.checker;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import translator.errorhandling.*;
import translator.exc.*;
import translator.lexer.ParsedLine;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.*;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.Command;
import translator.table.tablecomponents.userdefined.Identifier;
import translator.table.tablecomponents.userdefined.Label;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;

public class GrammarChecker extends TermIterator {
	public enum AvailableChecks  { FirsViewChecks,SecondViewChecks };
	
	private static int MAX_IDENTIFIER_LENGTH = 5; 
	private static int NUMBER_OF_ALLOWED_SEGS = 2;
	
	private SymbolTable symTab;
	private ErrorReporter reporter;
	private Checker checker;
	
	public GrammarChecker(ErrorsTable errTab,SymbolTable symTab) {
		super();
		this.symTab = symTab;
		reporter = new ErrorReporter(errTab);
	}
	
	public void check(ArrayList < ParsedLine > term, AvailableChecks appliedChecks) {
		selectChecker(appliedChecks);
		iterateOverTerm(term);
		if (term.size() > 0)
			checker.finalChecks(term);
	}
		
	private void selectChecker(AvailableChecks appliedChecks) {
		switch (appliedChecks) {
			case FirsViewChecks:
				if ( !(checker instanceof BeforeFirstViewChecker) )
					checker = new BeforeFirstViewChecker();
				break;
			case SecondViewChecks:
				if ( !(checker instanceof BeforeSecondViewChecker) )
					checker = new BeforeSecondViewChecker();
				break;
			default:
				System.err.println("Error : proposed check is not defined.");
				System.exit(1);
		}
	}

	@Override
	protected void whenNotMatched() {
		reporter.reportIllFormed(matchedLine);
	}

	@Override
	protected void whenLabelMatched() {
		checker.labelErrorsCheck();
	}

	@Override
	protected void whenDirectiveMatched() {
		checker.directiveErrorsCheck();
	}

	@Override
	protected void whenCommandMatched() {
		checker.commandErrorsCheck();		
	}
	
	private interface Checker {
		void labelErrorsCheck();
		void finalChecks(ArrayList<ParsedLine> term);
		void directiveErrorsCheck();
		void commandErrorsCheck();
	}
	
	private class CommonChecker {
		protected boolean AbsoluteExprCheck(AbsoluteExpr operand) {
			try {
				operand.isValidAbsExpr();
			} catch (MissedOperator e) {
				reporter.reportMissedOperator(matchedLine);
				return false;
			} catch (MissedConstant e) {
				reporter.reportMissedConstant(matchedLine);
				return false;
			} catch (UnmatchedCloseParenthesis e) {
				reporter.reportMissedCParenthesis(matchedLine);
				return false;
			} catch (UnmatchedOpenParenthesis e) {
				reporter.reportMissedOParenthesis(matchedLine);
				return false;
			} 
			return true;
		}
	}
	
	private class BeforeFirstViewChecker extends CommonChecker implements Checker {
		private TreeMap <String,AtomType > userDefinedNames;
		private Segment curCheckSeg;
		private boolean isEndProcessed;
			
		public BeforeFirstViewChecker() {
			userDefinedNames = new TreeMap<String,AtomType >();
			curCheckSeg = null;
			isEndProcessed = false;
		}

		@Override
		public void labelErrorsCheck() {
			if ( curCheckSeg == null ) {
				reporter.reportNotInsideSegmentDef( matchedLine );
				return;
			}
			
			Label curLabel = (Label) matchedLine.getAtomAt(0);
			if ( SymbolTable.isReserved(curLabel.getName()) ) {
				reporter.reportReservedNameConflicts(matchedLine);
				return;
			}
			
			if (  checkIfDef(curLabel) ) {
				if ( userDefinedNames.get(curLabel.getName().toLowerCase()) == AtomType.Label  ) 
					reporter.reportAlreadyDefLabel(matchedLine);
				else 
					reporter.reportNotLabelAlreadyDef(matchedLine);
				return;
			}
			
			if ( curLabel.getName().length() > MAX_IDENTIFIER_LENGTH ) {
				reporter.reportLongName(matchedLine,AtomType.Label);
				return;
			}
			
			userDefinedNames.put( curLabel.getName().toLowerCase(),AtomType.Label );
		}

		@Override
		public void commandErrorsCheck() {
			int cmdIndex = matchedLine.firstIndexOf(AtomType.Command);
			Command cmd = (Command) symTab.Search(matchedLine.getIndexName(cmdIndex) ); 
			ArrayList < Operand > operands = new ArrayList < Operand >();
			Atom.castCopy(operands,matchedLine.subArray(cmdIndex + 1) );
			
			if ( curCheckSeg == null) 
				reporter.reportCodeNotInsideSeg(matchedLine);
			
			if ( operands.size() != cmd.getOperandNumb() ) {
				reporter.reportWrongOperandNumbInCommands(matchedLine);
				return;
			}
			
			int i = 0;
			for ( Operand operand : operands ) {
				if ( operand.isMissing() ) {
					reporter.reportMissingOperand(matchedLine,i + 1);
					continue;
				}
				if ( operand instanceof AbsoluteExpr && !AbsoluteExprCheck((AbsoluteExpr) operand) ) {
					continue;
				}
			}
	
		}
		
		@Override
		public void directiveErrorsCheck() {
			if ( matchedLine.matches(defSegEndsPattern) ) {
				if ( matchedLine.getAtomAt(1).getName().equals("segment") ) {
					segmentErrorsCheck();
					return;
				}
				
				if ( matchedLine.getAtomAt(1).getName().equals("ends") ) {
					endSegmentErrorsCheck () ;
					return;
				}
			}
			
			if ( matchedLine.strMatches(defRegex) ) {
				defDirectiveErrorsCheck();
				return;
			}

			if ( matchedLine.strMatches("^\\s*end.*$") ) {
				 endDirectiveErrorsCheck();
				 return;
			}

			reporter.reportDirectiveUsage(matchedLine);
		}
		
		private void segmentErrorsCheck() {
			if ( curCheckSeg != null ) 
				reporter.reportSegmentNotClosed(matchedLine);
						
			Segment seg = (Segment) matchedLine.getAtomAt(0);
					
			if ( seg.getName().length() > MAX_IDENTIFIER_LENGTH ) {
				reporter.reportLongName(matchedLine,AtomType.Segment);
				return;
			}
						
			if ( checkIfDef(seg) ) {
				if (  userDefinedNames.get(seg.getName().toLowerCase() ) == AtomType.Segment ) 
					reporter.reportSegReopen(matchedLine);
				else 
					reporter.reportNotSegmentAlreadyDef(matchedLine);
				return;
			} else 
				userDefinedNames.put(seg.getName().toLowerCase(),AtomType.Segment );
			curCheckSeg = seg;
		}

		private void endSegmentErrorsCheck() {
			if ( curCheckSeg == null || 
					 !curCheckSeg.getName().equals(matchedLine.getAtomAt(0).getName() )) 
					reporter.reportSegmentNotOpened( matchedLine );
			else 
				curCheckSeg = null;
		}

		private void defDirectiveErrorsCheck() {
			Variable defVariable = (Variable) matchedLine.getAtomAt(0);
			ArrayList < Atom > initVals = matchedLine.subArray(2);
			Operand initVal;

			if ( curCheckSeg == null) {
				reporter.reportNotInsideSegmentDef(matchedLine);
				return;
			}
			
			if ( initVals.size() != 1) {
				reporter.reportWrongOperandNumbInDirective(matchedLine);
				return;
			}
			
			initVal = (Operand) initVals.get(0);
			
			if (  !(initVal instanceof AbsoluteExpr) )  {
				reporter.reportOnlyAbsExprAllowed(matchedLine);
				return;
			}
			
			if ( !AbsoluteExprCheck((AbsoluteExpr) initVal) ) {
				return;
			}
			
			if ( defVariable.Size() < initVal.calcSizeInBytes() ) {
				reporter.reportInitConstantTooBig(matchedLine);
				return;
			}
			
			if ( defVariable.getName().length() > MAX_IDENTIFIER_LENGTH ) {
				reporter.reportLongName(matchedLine,AtomType.Variable);
				return;
			}
			
			if ( checkIfDef(defVariable) ) {
				if (userDefinedNames.get(defVariable.getName().toLowerCase()) == AtomType.Variable )
					reporter.reportVariableAlreadyDef(matchedLine);
				else
					reporter.reportNotVaraibleAlreadyDef(matchedLine);
				return;
			}
			
			if ( SymbolTable.isReserved(defVariable.getName()) ) {
				reporter.reportReservedNameConflicts(matchedLine);
				return;
			}
			userDefinedNames.put(defVariable.getName().toLowerCase(),AtomType.Variable);	
		}
		
		private boolean checkIfDef(Atom atom) {
			return userDefinedNames.get(atom.getName().toLowerCase()) != null;
		}
		

		private void endDirectiveErrorsCheck() {
			isEndProcessed = true;
			ArrayList < Atom > operands = matchedLine.subArray(1);
			
			if ( operands.size() == 0) 
				reporter.reportMissingOperand(matchedLine ,-1);
			else {
				if ( operands.size() != 1)
					reporter.reportWrongOperandNumbInDirective(matchedLine);
			}
		}
		
		@Override
		public void finalChecks(ArrayList<ParsedLine> term) {
			if ( curCheckSeg != null ) 
				reporter.reportSegmentNotClosed(term.get(term.size() - 1) );
			if ( !isEndProcessed ) 
				reporter.reportEndDirectiveNotFound( term.get(term.size() -1 ));
			userDefinedNames.clear();
		}
	}
	
	private class BeforeSecondViewChecker extends CommonChecker implements Checker {

		@Override
		public void labelErrorsCheck() {	}

		@Override
		public void directiveErrorsCheck() {	

			if ( matchedLine.strMatches("^\\s*end.*$") &&
				 matchedLine.getAtomAt(1).getType() != AtomType.RelativeOperand)
				reporter.reportLabelExpected(matchedLine);
		}

		@Override
		public void commandErrorsCheck() {
			int cmdIndex = matchedLine.firstIndexOf(AtomType.Command);
			Command cmd = (Command) symTab.Search(matchedLine.getIndexName(cmdIndex) ); 
			ArrayList < Operand > operands = new ArrayList < Operand >();
			Atom.castCopy(operands,matchedLine.subArray(cmdIndex + 1) );
			
			if ( operandsCheckReport(operands,cmdIndex) ) 
				return;
			
			if (unresolvedMemoryTypeCheck(operands)) 
				return;
			
			if ( !cmd.isOperandsCombinationAllowed(operands) ) 
				reporter.reportUnsupportedOperands(matchedLine);
			
		}

		private boolean operandsCheckReport(ArrayList < Operand > operands,int cmdIndex) {
			boolean errorsFound = false;
			int i = 0;
		
			for (Operand operand : operands) {
			
				if ( operand instanceof MemoryOperand && !MemoryOperandCheck((MemoryOperand) operand )) {
					errorsFound = true;
					continue;
				}
									
				if ( operand instanceof UndefinedOperand) {
					Collection < Identifier > undefIdents = ((UndefinedOperand) operand).findIdentifiers();
					if ( undefIdents.isEmpty() ) 
						reporter.reportUndefinedOperand(matchedLine,cmdIndex + i + 1);
					else 
						for (Identifier ident : undefIdents) {
							reporter.reportUndefIdent(matchedLine,ident);
						}
					errorsFound = true;
				}
				i++;
			}

			return errorsFound;
		}
		
		private boolean unresolvedMemoryTypeCheck(ArrayList < Operand > operands) {
			if ( operands.size() == 1 && operands.get(0) instanceof MemoryOperand && 
					!((MemoryOperand) operands.get(0)).canDetermineType()  ) {
				reporter.reportUndefMemory(matchedLine);
				return true;
			}
			
			if ( operands.size() == 2 )  
				return twoOperandCheck(operands.get(0),operands.get(1));
			return false;
		}
		
		private boolean twoOperandCheck(Operand op1,Operand op2) {
			if ( relativeOpCheck(op1, op2) || relativeOpCheck(op2, op1) )
				return false;
			return false;
		}
		
		private boolean relativeOpCheck(Operand op1,Operand op2) {
			if (op1 instanceof MemoryOperand && !((MemoryOperand) op1).canDetermineType() ) 
				if ( op2 instanceof RegisterOperand ) {
					((MemoryOperand) op1).determineType(op2.calcSizeInBytes());
				} else {
					reporter.reportUndefMemory(matchedLine);
					return true;
				}
			return false;
		}

		private boolean MemoryOperandCheck(MemoryOperand operand) {
			try {
				operand.isValidMemory();
			} catch (BaseIndexCombinationNotAllowed e) {
				reporter.reportBaseIndexComb(matchedLine);
			}
			return true;
		}
		
		@Override
		public void finalChecks(ArrayList<ParsedLine> term) {
			identChecks();
			segNumbChecks();
		}
		
		private void identChecks() {
			ArrayList < Identifier > idents = new ArrayList < Identifier >();
			Atom.castCopy(idents,symTab.findAll(AtomType.Variable));
			Atom.castAppend(idents, symTab.findAll(AtomType.Label));
			
			for ( Identifier ident : idents) {	
				if ( !ident.isIdentUsed() ) 
					reporter.reportDefButUnusedSymbol(ident);	
			}	
		}
		
		private void segNumbChecks() {
			ArrayList < Segment > segs = new ArrayList < Segment > ();
			Atom.castCopy(segs,symTab.findAll(AtomType.Segment));
			
			if ( segs.size() != NUMBER_OF_ALLOWED_SEGS && segs.size() != 0) {
				reporter.reportWrongSegNumb(segs.get(0).getLineWhereDefined());
			}
			
		}
	}

	@Override
	public void genOutput(PrintWriter writer) {
		writer.println("Check was finished.");
		writer.println(reporter.getErrReported() + " errors were reported.");
	}
	
}



