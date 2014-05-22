package translator.errorhandling;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

interface Reportable { 
	public void report();
}

//encapsulates message and type of standard error
abstract class ErrorMessage {
	final private String msg;
	ErrorMessage(String msg) {
		this.msg = msg;
	}
	public String getMessage() {
		return msg;
	}
	abstract boolean isWarning();
}
class Error extends ErrorMessage {
	 public Error(String msg) {
	 	super(msg);
	 }
	@Override
	public boolean isWarning() { return false; }
}

class Warning extends ErrorMessage {
	Warning(String msg) {
		super(msg);
	}
	@Override
	public boolean isWarning() { return true; }
}


class ErrorInfo implements Reportable {
	 private int line;
	 private int pos;
	 ErrorMessage msgContainer;
	 ErrorInfo(ErrorMessage predef,int line,int pos) {
	     msgContainer = predef;
	     this.line = line;
	     this.pos = pos;
	 }
	 
	 public void report() {
	 	System.err.println( ((msgContainer.isWarning() )?("WARNING"):("ERROR")) + 
	 						" : " + msgContainer.getMessage() + "\n" +
	 		     	        "\tline : " + line + ", " + "position : " + pos + "\n");
	 }

}

public class ErrorsTable {
	// All kinds of error
	public enum ErrIdent { 	ExtraCharacters,
						 	UnexpectedSymbolInToken,
					 		MissingOperand,
					 		OperandNumbCommands,
					 		OperandNumbDirective,
					 		LabelAlreadyDef,
					 		AlreadyDefNotLabel,
					 		VariableAlreadyDef,
					 		AlreadyDefNotVariable,
					 		InitConstantTooBig,
					 		NoSegmentDataDefinition,
					 		PreviousSegNotFinished,
					 		AlreadyDefNotSegment,
					 		SegmentWasntOpened,
					 		IllFormedLine,
					 		EndDirectiveNotFound,
					 		WrongDirectiveUsage,
					 		LabelExpected,
					 		UndefinedIdentifier,
					 		UndeclaredSegCodeEmission,
					 		AbsExprAllowedOnly,
					 		MissedAbsExprOperator,
					 		MissedAbsExprOperand,
					 		MissedAbsExprOParenthesis,
					 		MissedAbsExprCParenthesis,
					 		NameConflictsWithReserved,
					 		UndefOperand,
					 		UnsupportedOperands,
					 		DefinedButUnused, 
					 		BaseIndexComb,
					 		UndefMemType, 
					 		TooLongNames,
					 		NumberOfSegNotAllowed,
					 		SegReopenNotAllowed
						 };
	
	private ArrayList<Reportable> FoundErrors;
	private final static TreeMap<ErrIdent,ErrorMessage> Errors; //container for standard errors
	private int warningsCounter;
	private int errorsCounter;
	
	public ErrorsTable() {
		FoundErrors = new ArrayList<Reportable>();
		warningsCounter = errorsCounter = 0;
	}
	
	public void report(ErrIdent err, int line , int pos ) {
		ErrorMessage predef = Errors.get(err);
		if ( predef.isWarning() ) 
			warningsCounter++;
		else 
			errorsCounter++;
		FoundErrors.add( new ErrorInfo(predef,line,pos) );
	}
	
	public String getErrorMsg(ErrIdent err) {
		return Errors.get(err).getMessage();
	}
	
	public void PrintFoundErrors() {
		for (Reportable err : FoundErrors ) {
			err.report();
		}
		System.err.println( getSummary() );
	}
	
	private String getSummary () {
		return "Summary : " + warningsCounter + " Warnings, " +
			    + errorsCounter + " Errors were found.";
	}
	
	public void prnSummary(PrintWriter where) {
		where.println( getSummary() );
	}

	public boolean isCritical() {
		return errorsCounter > 0;
	}
	
	static {
		Errors = new TreeMap<ErrIdent,ErrorMessage>();
	// warnings initialization
		Errors.put(ErrIdent.ExtraCharacters , new Warning("Extra Characters" ) );
		Errors.put(ErrIdent.DefinedButUnused,new Warning("Symbol was defined but never used. "));
		
	// errors initialization
		Errors.put(ErrIdent.UnexpectedSymbolInToken , new Error("Unexpected symbol was found in token.") );
		Errors.put(ErrIdent.MissingOperand , new Error("Operand is missing."));
		Errors.put(ErrIdent.OperandNumbCommands , new Error("Number of provided operands does not match the number of operands for the command.") );
		Errors.put(ErrIdent.OperandNumbDirective, new Error("Number of provided operands does not match the number of operands for the directive."));
		Errors.put(ErrIdent.LabelAlreadyDef, new Error("Label with such name has already defined.") );
		Errors.put(ErrIdent.AlreadyDefNotLabel, new Error("Not label identifier has already defined."));
		Errors.put(ErrIdent.InitConstantTooBig, new Error("Value out of range.") );
		Errors.put(ErrIdent.NoSegmentDataDefinition ,new Error("Data defined not inside segment."));
		Errors.put(ErrIdent.PreviousSegNotFinished ,new Error("Previous segment wasn't ended."));
		Errors.put(ErrIdent.AlreadyDefNotSegment ,new Error("Not segment identifier has already defined."));
		Errors.put(ErrIdent.SegmentWasntOpened, new Error("Segment with such name was not declared.")); 
		Errors.put(ErrIdent.VariableAlreadyDef, new Error("Variable with such name has already defined."));
		Errors.put(ErrIdent.AlreadyDefNotVariable, new Error("Not variable identifier has already defined."));
		Errors.put(ErrIdent.IllFormedLine, new Error("Invalid line."));
		Errors.put(ErrIdent.EndDirectiveNotFound, new Error("End directive not found."));
		Errors.put(ErrIdent.WrongDirectiveUsage, new Error("Wrong directive usage."));
		Errors.put(ErrIdent.LabelExpected, new Error("Wrong operand type. Label expected."));
		Errors.put(ErrIdent.UndeclaredSegCodeEmission, new Error("Code emission to undeclared segment."));
		Errors.put(ErrIdent.MissedAbsExprOperator, new Error("Operator was missed in the absolute expression."));
		Errors.put(ErrIdent.MissedAbsExprOperand, new Error("Constant was missed in the absolute expression."));
		Errors.put(ErrIdent.MissedAbsExprCParenthesis, new Error("Missed close parenthesis in the absolute expression."));
		Errors.put(ErrIdent.MissedAbsExprOParenthesis, new Error("Missed open parenthesis in the absolute expression."));
		Errors.put(ErrIdent.NameConflictsWithReserved, new Error("Name conflict with reserved name."));
		Errors.put(ErrIdent.UndefOperand, new Error("Operand type is not defined or not supported."));
		Errors.put(ErrIdent.UnsupportedOperands , new Error("Unsupported combination of operands."));
		Errors.put(ErrIdent.UndefinedIdentifier, new Error("Identifier was not declared."));
		Errors.put(ErrIdent.BaseIndexComb,new Error("Combination of base and index register isn't allowed.") );
		Errors.put(ErrIdent.UndefMemType, new Error("Memory type is not defined."));
	
	// restrictions of variant
		Errors.put(ErrIdent.AbsExprAllowedOnly, new Error("Absolute expression or constant expected. (restriction of variant)"));
		Errors.put(ErrIdent.TooLongNames , new Error("Identifier name is too long. (restriction of variant)") );
		Errors.put(ErrIdent.NumberOfSegNotAllowed, new Error("Wrong number of segments. (restriction of variant)"));
		Errors.put(ErrIdent.SegReopenNotAllowed, new Error("Reopening of segments is not allowed. (restriction of variant)"));
	}
	
}
