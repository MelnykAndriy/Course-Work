
package translator.termworks.checker;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import translator.errorhandling.ErrorReporter;
import translator.errorhandling.ErrorsTable;
import translator.lexer.ParsedLine;
import translator.termworks.TermIterator;
import translator.termworks.syntax.*;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;

public class GrammarChecker extends TermIterator {
	public enum AvailableChecks  { FirsViewChecks,SecondViewChecks };
	
	private SymbolTable symTab;
	private ErrorReporter reporter;
	private Checkable checker;
	
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
		checker.labelErrorsCheck(matchedLine);
	}

	@Override
	protected void whenDirectiveMatched() {
		checker.directiveErrorsCheck(matchedLine);
		
	}

	@Override
	protected void whenCommandMatched() {
		checker.commandErrorsCheck(matchedLine);		
	}
	
	private interface Checkable {
		void labelErrorsCheck(ParsedLine line);
		void finalChecks(ArrayList<ParsedLine> term);
		void directiveErrorsCheck(ParsedLine line);
		void commandErrorsCheck(ParsedLine line);
	}
	
	private class BeforeFirstViewChecker implements Checkable {
		private TreeMap <String,AtomType > userDefinedNames;
		private Segment curCheckSeg;
		private boolean isEndProcessed;
			
		public BeforeFirstViewChecker() {
			userDefinedNames = new TreeMap<String,AtomType >();
			curCheckSeg = null;
			isEndProcessed = false;
		}

		@Override
		public void labelErrorsCheck(ParsedLine line) {
			Label curLabel = (Label) line.getAtomAt(0);
			if (  checkIfDef(curLabel) )
				if ( userDefinedNames.get(curLabel.getName().toLowerCase()) == AtomType.Label  ) 
					reporter.reportAlreadyDefLabel(line);
				else 
					reporter.reportNotLabelAlreadyDef(line);
			else 
				userDefinedNames.put( curLabel.getName().toLowerCase(),AtomType.Label );
		}

		@Override
		public void commandErrorsCheck(ParsedLine line) {
			int cmdIndex = line.firstIndexOf(AtomType.Command);
			Command cmd = (Command) symTab.Search(line.getIndexName(cmdIndex) ); 
			ArrayList < Atom > operands = line.subArray(cmdIndex + 1);
			
			if ( curCheckSeg == null) 
				reporter.reportCodeNotInsideSeg(line);
			
			if ( operands.size() != cmd.getOperandNumb() ) 
				reporter.reportWrongOperandNumbInCommands(line);
			
			for (int i = 0; i < operands.size(); i++ )
				if ( ( (Operand) operands.get(i)).isMissing() )
					reporter.reportMissingOperand(line,i + 1);
		}
		
		@Override
		public void directiveErrorsCheck(ParsedLine line) {
			if ( line.matches(defSegEndsPattern) ) {
				if ( line.getAtomAt(1).getName().equals("segment") ) {
					segmentErrorsCheck( line );
					return;
				}
				
				if ( line.getAtomAt(1).getName().equals("ends") ) {
					endSegmentErrorsCheck ( line ) ;
					return;
				}
			}
			
			if ( line.strMatches(defRegex) ) {
				defDirectiveErrorsCheck(line);
				return;
			}

			if ( line.strMatches("^\\s*end.*$") ) {
				 endDirectiveErrorsCheck(line);
				 return;
			}

			reporter.reportDirectiveUsage(line);
		}
		
		private void segmentErrorsCheck(ParsedLine line) {
			if ( curCheckSeg != null ) 
				reporter.reportSegmentNotClosed(line);
			
			Segment seg = (Segment) line.getAtomAt(0);
			if ( checkIfDef(seg) && userDefinedNames.get(seg.getName().toLowerCase()) != AtomType.Segment ) {
				
				reporter.reportNotSegmentAlreadyDef(line);
			} else {
				userDefinedNames.put(seg.getName().toLowerCase(),AtomType.Segment );
			}
			curCheckSeg = seg;
			
		}

		private void endSegmentErrorsCheck(ParsedLine line) {
			if ( curCheckSeg == null || 
					 !curCheckSeg.getName().equals(line.getAtomAt(0).getName() )) 
					reporter.reportSegmentNotOpened( line );
			else 
				curCheckSeg = null;
		}

		private void defDirectiveErrorsCheck(ParsedLine line) {
			Variable defVariable = (Variable) line.getAtomAt(0);
			ArrayList < Atom > initVals = line.subArray(2);
			Operand initVal;

			if ( curCheckSeg == null) 
				reporter.reportNotInsideSegmentDef(line);
			else {
				if ( initVals.size() != 1) 
					reporter.reportWrongOperandNumbInDirective(line);
				else {
					initVal = (Operand) initVals.get(0);
					
					if (  !(initVal instanceof AbsoluteExpr) ) 
						reporter.reportOnlyAbsExprAllowed(line);
					else {
						if ( defVariable.Size() <  initVal.calcSizeInBytes() )
							reporter.reportInitConstantTooBig(line);
						else {
							if ( checkIfDef( defVariable ) ) {
								if (userDefinedNames.get(defVariable.getName().toLowerCase()) == AtomType.Variable )
									reporter.reportVariableAlreadyDef(line);
								else
									reporter.reportNotVaraibleAlreadyDef(line);
							}
							else {					
								userDefinedNames.put(defVariable.getName().toLowerCase(),AtomType.Variable);
							}
						}
					}
				}
			}
		}
		
		private boolean checkIfDef(Atom atom) {
			return userDefinedNames.get(atom.getName().toLowerCase()) != null;
		}
		

		private void endDirectiveErrorsCheck(ParsedLine line) {
			isEndProcessed = true;
			ArrayList < Atom > operands = line.subArray(1);
			
			if ( operands.size() == 0) 
				reporter.reportMissingOperand(line ,-1);
			else {
				if ( operands.size() != 1)
					reporter.reportWrongOperandNumbInDirective(line);
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
	
	private class BeforeSecondViewChecker implements Checkable {

		@Override
		public void labelErrorsCheck(ParsedLine line) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void directiveErrorsCheck(ParsedLine line) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void commandErrorsCheck(ParsedLine line) {
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



