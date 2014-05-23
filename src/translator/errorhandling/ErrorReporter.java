package translator.errorhandling;

import translator.errorhandling.ErrorsTable.ErrIdent;
import translator.lexer.ParsedLine;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.userdefined.Identifier;

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
		line.errFound(errTab.getErrorMsg(ErrIdent.LabelAlreadyDef));
		errTab.report( ErrIdent.LabelAlreadyDef,
								   line.getLineNumb(),
								   line.findPos( line.getAtomAt(0) ) );
	}
	public void reportNotLabelAlreadyDef(ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.AlreadyDefNotLabel));
		errTab.report( ErrIdent.AlreadyDefNotLabel,
					    line.getLineNumb(),
					    line.findPos( line.getAtomAt(0) ) );
	}

	public void reportVariableAlreadyDef(ParsedLine line) {
		 errReported++;
		 line.errFound(errTab.getErrorMsg(ErrIdent.VariableAlreadyDef));
		 errTab.report( ErrIdent.VariableAlreadyDef,
					    line.getLineNumb(),
					    line.findPos( line.getAtomAt(0) ) );
	}
	
	public void reportNotVaraibleAlreadyDef(ParsedLine line) {
		errReported++; 
		line.errFound(errTab.getErrorMsg(ErrIdent.AlreadyDefNotVariable));
		errTab.report( ErrIdent.AlreadyDefNotVariable,
		 			    line.getLineNumb(),
		 			    line.findPos( line.getAtomAt(0) ) );
	}
		
	public void reportInitConstantTooBig(ParsedLine line) {
		errReported++; 
		line.errFound(errTab.getErrorMsg(ErrIdent.InitConstantTooBig));
		errTab.report(ErrIdent.InitConstantTooBig,
								 line.getLineNumb(),
						    	 line.findPos( line.getAtomAt(2)) );
	}
	
	
	public void reportNotInsideSegmentDef(ParsedLine line) {
		errReported++; 
		ErrIdent err = ( line.getAtomAt(0).getType() == AtomType.Variable )?( ErrIdent.NoSegmentDataDefinition ):
					   ( ( line.getAtomAt(0).getType() == AtomType.Label )?(ErrIdent.NoSegmentLabelDefinition ):(null) ) ;
		line.errFound(errTab.getErrorMsg(err));
		errTab.report(err, line.getLineNumb(), line.findPos( line.getAtomAt(0) ));
	}
	
	public void reportSegmentNotClosed(ParsedLine line) {
		errReported++; 
		line.errFound(errTab.getErrorMsg(ErrIdent.PreviousSegNotFinished));
		errTab.report(ErrIdent.PreviousSegNotFinished,
								 line.getLineNumb(),
								 line.findPos(line.getAtomAt(1)));
	}
	
	public void reportSegmentNotOpened(ParsedLine line) {
		errReported++; 
		line.errFound(errTab.getErrorMsg(ErrIdent.SegmentWasntOpened));
		errTab.report(ErrIdent.SegmentWasntOpened,
								 line.getLineNumb(),
								 line.findPos( line.getAtomAt(0) ));
	}
		
	public void reportNotSegmentAlreadyDef(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.AlreadyDefNotSegment));
		errTab.report(ErrIdent.AlreadyDefNotSegment ,
						line.getLineNumb(),
						line.findPos(line.getAtomAt(0)) );
	}
	
	public void reportMissingOperand(ParsedLine line,int opNumb) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.MissingOperand));
		errTab.report(ErrIdent.MissingOperand,
					  line.getLineNumb(),
					  (line.findPos(",",opNumb) != -1)
					  	?(line.findPos(",",opNumb))
			  			:(line.getLine().length() ));
	}
	
	public void reportWrongOperandNumbInCommands(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.OperandNumbCommands));
		errTab.report(ErrIdent.OperandNumbCommands,
					  line.getLineNumb(),
					  line.findPos(  AtomType.Command ));
	}
	
	public void reportWrongOperandNumbInDirective(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.OperandNumbDirective));
		errTab.report(ErrIdent.OperandNumbDirective,
					  line.getLineNumb(),
					  line.findPos(AtomType.Directive ));
	}
	
	public void reportIllFormed(ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.IllFormedLine));
		errTab.report(ErrIdent.IllFormedLine, line.getLineNumb(), 1);
	}
	
	public void reportDirectiveUsage(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.WrongDirectiveUsage));
		errTab.report(ErrIdent.WrongDirectiveUsage,
					  line.getLineNumb(),
					  line.findPos( AtomType.Directive ) );
	}
	
	public void reportLabelExpected(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.LabelExpected));
		errTab.report( ErrIdent.LabelExpected ,
					   line.getLineNumb(),
					   line.findPos(line.getAtomAt(1)));
	}

	public void reportEndDirectiveNotFound(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.EndDirectiveNotFound));
		errTab.report(ErrIdent.EndDirectiveNotFound, 
					  line.getLineNumb(), 1);	
	}
	
	public void reportCodeNotInsideSeg(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.UndeclaredSegCodeEmission));
		errTab.report(ErrIdent.UndeclaredSegCodeEmission,
				      line.getLineNumb(),
					  line.findPos( AtomType.Command ));
	}
	
	public void reportMissedOperator(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.MissedAbsExprOperator));
		errTab.report(ErrIdent.MissedAbsExprOperator,
					  line.getLineNumb(),
					  line.findPos( AtomType.AbsExpr ));
	}
	
	public void reportMissedConstant(ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.MissedAbsExprOperand));
		errTab.report(ErrIdent.MissedAbsExprOperand,
					  line.getLineNumb(),
					  line.findPos( AtomType.AbsExpr ));
	}
	
	public void reportMissedOParenthesis( ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.MissedAbsExprOParenthesis));
		errTab.report(ErrIdent.MissedAbsExprOParenthesis,
					  line.getLineNumb(),
					  line.findPos( AtomType.AbsExpr ));
	}
	
	public void reportMissedCParenthesis( ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.MissedAbsExprCParenthesis));
		errTab.report(ErrIdent.MissedAbsExprCParenthesis,
					  line.getLineNumb(),
					  line.findPos(AtomType.AbsExpr));
	}
	
	public void reportReservedNameConflicts( ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.NameConflictsWithReserved));
		errTab.report(ErrIdent.NameConflictsWithReserved,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(0)));
	}

	public void reportUndefinedOperand(ParsedLine line,int undefOpPos) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.UndefOperand));
		errTab.report(ErrIdent.UndefOperand,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(undefOpPos) ) );
	}
	
	public void reportUnsupportedOperands(ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.UnsupportedOperands));
		errTab.report(ErrIdent.UnsupportedOperands,
					  line.getLineNumb(),
					  line.findPos(AtomType.Command ) );
	}
 	
	public void reportUndefIdent( ParsedLine line, Identifier ident ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.UndefinedIdentifier));
		errTab.report(ErrIdent.UndefinedIdentifier, line.getLineNumb() ,
						line.findPos(ident));
	}
	
	public void reportDefButUnusedSymbol( Identifier ident ) {
		if ( ident.getType() == AtomType.Variable ) 
			reportDefButUnusedVar(ident.getLineWhereDefined());
		
		if ( ident.getType() == AtomType.Label )
			reportDefButUnusedLabel(ident.getLineWhereDefined());
	}
	
	public void reportDefButUnusedVar( ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.DefinedButUnused));
		errTab.report(ErrIdent.DefinedButUnused , line.getLineNumb(), 
					  line.findPos(AtomType.Variable));
	}
		
	public void reportDefButUnusedLabel( ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.DefinedButUnused));
		errTab.report(ErrIdent.DefinedButUnused , line.getLineNumb(), 
					  line.findPos(AtomType.Label));
	}

	public void reportBaseIndexComb(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.AlreadyDefNotLabel));
		errTab.report(ErrIdent.BaseIndexComb,line.getLineNumb(), 
					  line.findPos(AtomType.Memory));
	}
	
	public void reportUndefMemory(ParsedLine line ) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.UndefMemType));
		errTab.report(ErrIdent.UndefMemType, line.getLineNumb(),
						line.findPos(AtomType.Memory));
	}
	
// restrictions of variant
	
	public void reportOnlyAbsExprAllowed(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.AbsExprAllowedOnly));
		errTab.report(ErrIdent.AbsExprAllowedOnly,
					  line.getLineNumb(),
					  line.findPos(line.getAtomAt(2)));
	}
	
	public void reportLongName(ParsedLine line,AtomType tp) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.TooLongNames));
		errTab.report(ErrIdent.TooLongNames, line.getLineNumb() , line.findPos(tp));
	}
	
	public void reportWrongSegNumb(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.NumberOfSegNotAllowed));
		errTab.report(ErrIdent.NumberOfSegNotAllowed, line.getLineNumb() , line.findPos(AtomType.Segment));
	}
	
	public void reportSegReopen(ParsedLine line) {
		errReported++;
		line.errFound(errTab.getErrorMsg(ErrIdent.SegReopenNotAllowed));
		errTab.report(ErrIdent.SegReopenNotAllowed, line.getLineNumb() , line.findPos(AtomType.Segment));
	}

}

