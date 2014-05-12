package translator.errorhandling;

import translator.errorhandling.ErrorsTable.ErrIdent;
import translator.lexer.ParsedLine;
import translator.table.tablecomponents.AtomType;

public class ErrorReporter {
	private ErrorsTable errTab;
	private int errReported;
	
	public ErrorReporter(ErrorsTable errTab) {
		errReported = 0;
		this.errTab = errTab;
	}
	
	public int getErrReported() {
		return errReported;
	}

	public void reportAlreadyDefLabel(ParsedLine line) {
		errReported++;
		 errTab.report( ErrIdent.LabelAlreadyDef,
								   line.getLineNumb(),
								   line.findPos( line.getAtomAt(0) ) );
	}
	public void reportNotLabelAlreadyDef(ParsedLine line ) {
		errReported++; 
		errTab.report( ErrIdent.AlreadyDefNotLabel,
					    line.getLineNumb(),
					    line.findPos( line.getAtomAt(0) ) );
	}

	public void reportVariableAlreadyDef(ParsedLine line) {
		 errReported++;
		 errTab.report( ErrIdent.VariableAlreadyDef,
					    line.getLineNumb(),
					    line.findPos( line.getAtomAt(0) ) );
	}
	
	public void reportNotVaraibleAlreadyDef(ParsedLine line) {
		errReported++; 
		errTab.report( ErrIdent.AlreadyDefNotVariable,
		 			    line.getLineNumb(),
		 			    line.findPos( line.getAtomAt(0) ) );
	}
		
	public void reportInitConstantTooBig(ParsedLine line) {
		errReported++; 
		errTab.report(ErrIdent.InitConstantTooBig,
								 line.getLineNumb(),
						    	 line.findPos( line.getAtomAt(2)) );
	}
	
	
	public void reportNotInsideSegmentDef(ParsedLine line) {
		errReported++; 
		errTab.report(ErrIdent.NoSegmentDataDefinition,
								 line.getLineNumb(),
						         line.findPos( line.getAtomAt(0) ));
	}
	
	public void reportSegmentNotClosed(ParsedLine line) {
		errReported++; 
		errTab.report(ErrIdent.PreviousSegNotFinished,
								 line.getLineNumb(),
								 line.findPos(line.getAtomAt(1)));
	}
	
	public void reportSegmentNotOpened(ParsedLine line) {
		errReported++; 
		errTab.report(ErrIdent.SegmentWasntOpened,
								 line.getLineNumb(),
								 line.findPos( line.getAtomAt(0) ));
	}
		
	public void reportNotSegmentAlreadyDef(ParsedLine lexerLine) {
		errReported++;
		errTab.report(ErrIdent.AlreadyDefNotSegment ,
				 lexerLine.getLineNumb(),
				 lexerLine.findPos(lexerLine.getAtomAt(0)) );
	}
	
	public void reportMissingOperand(ParsedLine line,int opNumb) {
		errReported++;
		errTab.report(ErrIdent.MissingOperand,
					  line.getLineNumb(),
					  (line.findPos(",",opNumb) != -1)
					  	?(line.findPos(",",opNumb))
			  			:(line.getLine().length() ));
	}
	
	public void reportWrongOperandNumbInCommands(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.OperandNumbCommands,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Command))));
	}
	
	public void reportWrongOperandNumbInDirective(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.OperandNumbDirective,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Directive))));
	}
	
	public void reportIllFormed(ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.IllFormedLine, line.getLineNumb(), 1);
	}
	
	public void reportDirectiveUsage(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.WrongDirectiveUsage,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Directive))) );
	}
	
	public void reportLabelExpected(ParsedLine line) {
		errReported++;
		errTab.report( ErrIdent.LabelExpected ,
					   line.getLineNumb(),
						  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Identifier))));
	}

	public void reportEndDirectiveNotFound(ParsedLine parsedLine) {
		errReported++;
		errTab.report(ErrIdent.EndDirectiveNotFound, 
					  parsedLine.getLineNumb(), 1);	
	}
	
	public void reportCodeNotInsideSeg(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.UndeclaredSegCodeEmission,
				      line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Command))));
	}
	
	public void reportOnlyAbsExprAllowed(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.AbsExprAllowedOnly,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(2)));
	}

	public void reportMissedOperator(ParsedLine line) {
		errReported++;
		errTab.report(ErrIdent.MissedAbsExprOperator,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.AbsExpr))));
	}
	
	public void reportMissedConstant(ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.MissedAbsExprOperand,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.AbsExpr))));
	}
	
	public void reportMissedOParenthesis( ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.MissedAbsExprOParenthesis,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.AbsExpr))));
	}
	
	public void reportMissedCParenthesis( ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.MissedAbsExprCParenthesis,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.AbsExpr))));
	}
	
	public void reportReservedNameConflicts( ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.NameConflictsWithReserved,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(0)));
	}

	public void reportUndefinedOperand(ParsedLine line,int undefOpPos) {
		errReported++;
		errTab.report(ErrIdent.UndefOperand,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(undefOpPos) ) );
	}
	
	public void reportUnsupportedOperands(ParsedLine line ) {
		errReported++;
		errTab.report(ErrIdent.UnsupportedOperands,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(line.firstIndexOf(AtomType.Command)) ) );
	}
 	
}
