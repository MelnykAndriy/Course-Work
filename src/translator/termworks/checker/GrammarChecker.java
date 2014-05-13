
package translator.termworks.checker;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import translator.errorhandling.ErrorReporter;
import translator.errorhandling.ErrorsTable;
import translator.exc.*;
import translator.lexer.ParsedLine;
import translator.termworks.TermIterator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;
import translator.termworks.syntax.operands.RegisterOperand;
import translator.termworks.syntax.operands.UndefinedOperand;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.Command;
import translator.table.tablecomponents.userdefined.Label;
import translator.table.tablecomponents.userdefined.Segment;
import translator.table.tablecomponents.userdefined.Variable;

public class GrammarChecker extends TermIterator {
	public enum AvailableChecks  { FirsViewChecks,SecondViewChecks };
	
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
	
	private class BeforeFirstViewChecker implements Checker {
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
			Label curLabel = (Label) matchedLine.getAtomAt(0);
			if ( SymbolTable.isReserved(curLabel.getName()) ) {
				reporter.reportReservedNameConflicts(matchedLine);
				return;
			}
			
			if (  checkIfDef(curLabel) )
				if ( userDefinedNames.get(curLabel.getName().toLowerCase()) == AtomType.Label  ) 
					reporter.reportAlreadyDefLabel(matchedLine);
				else 
					reporter.reportNotLabelAlreadyDef(matchedLine);
			else 
				userDefinedNames.put( curLabel.getName().toLowerCase(),AtomType.Label );
		}

		@Override
		public void commandErrorsCheck() {
			int cmdIndex = matchedLine.firstIndexOf(AtomType.Command);
			Command cmd = (Command) symTab.Search(matchedLine.getIndexName(cmdIndex) ); 
			ArrayList < Atom > operands = matchedLine.subArray(cmdIndex + 1);
			
			if ( curCheckSeg == null) 
				reporter.reportCodeNotInsideSeg(matchedLine);
			
			if ( operands.size() != cmd.getOperandNumb() ) {
				reporter.reportWrongOperandNumbInCommands(matchedLine);
				return;
			}
				
			if ( operandsCheckReport(operands,cmdIndex) ) 
				return;

//			System.out.println("for command " + cmd.getName());
//			int j = 0;
//			for ( Atom atom : operands) {
//				if (atom instanceof RegisterOperand)
//					System.out.println("RegSize : " + ((RegisterOperand) atom).getRegSize());
//				System.out.println(1 + " " + atom.getName() );
//				j++;
//			}
			if ( !cmd.isOperandsCombinationAllowed(operands) ) 
				reporter.reportUnsupportedOperands(matchedLine);
		}
		
		private boolean operandsCheckReport(ArrayList < Atom > operands,int cmdIndex) {
			boolean errorsFound = false;
			for (int i = 0; i < operands.size(); i++ ) {
				if ( ( (Operand) operands.get(i)).isMissing() ) {
					reporter.reportMissingOperand(matchedLine,i + 1);
					errorsFound = true;
					continue;
				}
				if ( operands.get(i) instanceof AbsoluteExpr ) {
					AbsoluteExprCheck((AbsoluteExpr) operands.get(i));
					errorsFound = true;
					continue;
				}
					
				if ( operands.get(i) instanceof UndefinedOperand) {
					reporter.reportUndefinedOperand(matchedLine,cmdIndex + i + 1);
					errorsFound = true;
				}
			}
			return errorsFound;
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
			if ( checkIfDef(seg) && userDefinedNames.get(seg.getName().toLowerCase()) != AtomType.Segment ) {
				
				reporter.reportNotSegmentAlreadyDef(matchedLine);
			} else {
				userDefinedNames.put(seg.getName().toLowerCase(),AtomType.Segment );
			}
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
		
		private boolean AbsoluteExprCheck(AbsoluteExpr initVal) {
			try {
				initVal.isValidAbsExpr();
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
			
			// TODO
//			if ( !tabRef.isLabel(lexerLine.getAtomAt(1).getName()) ) 
//				reporter.reportLabelExpected(lexerLine);
			
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
	
	private class BeforeSecondViewChecker implements Checker {

		@Override
		public void labelErrorsCheck() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void directiveErrorsCheck() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void commandErrorsCheck() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void finalChecks(ArrayList<ParsedLine> term) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void genOutput(PrintWriter writer) {
		writer.println("Check was finished.");
		writer.println(reporter.getErrReported() + " errors were reported.");
	}
	
}



