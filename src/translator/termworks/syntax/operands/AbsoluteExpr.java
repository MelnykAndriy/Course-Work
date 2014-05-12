package translator.termworks.syntax.operands;

import java.util.ArrayList;
import java.util.TreeMap;

import translator.exc.*;
import translator.lexer.Lexer;
import translator.table.OperandKind;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.operators.Operator;
import translator.table.tablecomponents.userdefined.Constant;

public class AbsoluteExpr extends Operand {
	
	private int unfixedTokenNumb;

	private static TreeMap < String , Atom > unaryFixTab;
	private static String OParenthesis = "(";
	private static String CParenthesis = ")";
	
	static {
		unaryFixTab = new TreeMap < String , Atom >();
		unaryFixTab.put("+",SymbolTable.getReserved("u+") );
		unaryFixTab.put("-",SymbolTable.getReserved("u-") );
	}
	
	public static boolean isAbsoluteExpr(ArrayList < Atom > operandAtoms) {
		if ( operandAtoms.size() == 0 ) return false;
		for ( Atom atom : operandAtoms )
			if ( !(atom.getType() == AtomType.Constant ||
				 atom.getType() == AtomType.Operator) )
				return false;
		return true;
	}
	
	public AbsoluteExpr(ArrayList<Atom> atoms) {
		super(atoms);
		System.out.println(Lexer.buildStringFromAtoms(operandAtoms));
		unfixedTokenNumb = atoms.size();
	}
	
	public int calcSizeInBytes() {
		return new AbsoluteExpr(unaryFix(operandAtoms)).evalAbsoluteExpr().getSizeInBytes();
	}
			
	private static ArrayList < Atom > unaryFix(ArrayList < Atom > absoluteExpr ) {
		ArrayList < Atom > fixedAbsExpr = new ArrayList < Atom > () ;
		Atom prevAtom = null;
		for ( Atom atom : absoluteExpr ) {
			if ( atom instanceof Operator && unaryFixTab.get(atom.getName()) != null 
					&& !(prevAtom instanceof Constant) )
				fixedAbsExpr.add(unaryFixTab.get( atom.getName() ) );
			else 
				fixedAbsExpr.add(atom);
		} 
		return fixedAbsExpr;
	}
	
//	private static ArrayList < Atom > unaryFix(ArrayList < Atom > absoluteExpr ) {
//		ArrayList < Atom > fixedAbsExpr = new ArrayList < Atom > ();
//		Atom prevAtom = null;
//		Atom postAddedCP = null;
//		Stack< Integer > openClsStack = new Stack< Integer > ();
//		int OPadded = 0;
//		
//		for (Atom atom : absoluteExpr ) {
//			try {
//				if ( atom instanceof Operator && !(prevAtom instanceof Constant) 
//						&& unaryFixTab.get(atom.getName()) != null && (prevAtom == null || prevAtom.getName() != CParenthesis )) {
//					fixedAbsExpr.add(Translator.mainTab.Search("("));		// TODO
//					fixedAbsExpr.add(unaryFixTab.get(atom.getName()));
//					OPadded++;
//					openClsStack.push(0);
//					continue;
//				}
//				if (atom.getName() == OParenthesis && OPadded != 0 ) {
//					openClsCounter++;
//					continue;
//				}
//				if ( atom.getName() == CParenthesis )
//					openClsCounter--;
//				if ((atom instanceof Constant || atom.getName() == CParenthesis) && OPadded && openClsCounter == 0 ) {
//					postAddedCP = Translator.mainTab.Search(")"); 	//TODO
//					OPadded = false;
//				}
//			} finally {
//				fixedAbsExpr.add(atom);
//				prevAtom = atom;
//				if ( postAddedCP != null ) {
//					fixedAbsExpr.add(postAddedCP);
//					postAddedCP = null;
//				}
//			}
//		}
//		return fixedAbsExpr;
//	}
	
	public int tokenNumb() {
		return unfixedTokenNumb;
	}
	
	public void calc() {
		operandAtoms = unaryFix(operandAtoms);
		Constant evaluetedConstant = evalAbsoluteExpr();
		operandAtoms.clear();
		operandAtoms.add(evaluetedConstant);
		operKind = (evaluetedConstant.GetVaue() == 1)?(OperandKind.imm8):( 
						(evaluetedConstant.GetVaue() == 2)?(OperandKind.imm16):(OperandKind.imm32) );
	}
	
	public Constant evalAbsoluteExpr (){
		return new Constant(0,10);
//		if ( isAbsoluteTerm() ) 
//			return (Constant) operandAtoms.get(0);
//
//		int lowestPrOpPos = posLowPriority();
//		Operand leftOp = new AbsoluteExpr(new ArrayList < Atom >(operandAtoms.subList(0,lowestPrOpPos)));
//		Operand rightOp = new AbsoluteExpr(new ArrayList < Atom >(operandAtoms.subList(lowestPrOpPos + 1,operandAtoms.size())) );
//		return (( AbsoluteExpr) ((Operator) operandAtoms.get(lowestPrOpPos)).eval(leftOp,rightOp)).evalAbsoluteExpr();
	}
	
	private boolean isAbsoluteTerm() {
		return operandAtoms.size() == 1 && operandAtoms.get(0) instanceof Constant;
	}
	
	private boolean isParenthesisRemoveNeeded() {
		return  operandAtoms.get(0).getName() == OParenthesis &&
				operandAtoms.get(operandAtoms.size() - 1 ).getName() == CParenthesis;
	}
	
	private void removeParenthesis() {
		operandAtoms.remove(0);
		operandAtoms.remove(operandAtoms.size() - 1);
	}
		
	private int posLowPriority() {
		int lowestPr = 0;
		int posLowestPr = -1;
		int i = 0;
		int openClsCounter = 0;
						
		for (Atom atom : operandAtoms ) {
			try { 
				if ( atom instanceof Operator ) {	
					if ( OParenthesis.equals(atom.getName()) ) {
						openClsCounter++;
						continue;
					}
					if ( CParenthesis.equals(atom.getName()) ) {
						openClsCounter--;
						continue;
					}
					if ( openClsCounter == 0 && ((Operator) atom).getPriority() >= lowestPr ) {
						lowestPr = ((Operator) atom).getPriority();
						posLowestPr = i;
					}
					
				}
			} finally {
				i++;				
			}
		}
		if ( posLowestPr == -1) {
			if ( isParenthesisRemoveNeeded() ) 
				removeParenthesis();
			return posLowPriority();
		}
		return posLowestPr;
	}
	
	public void isValidAbsExpr () throws MissedOperator, UnmatchedOpenParenthesis, MissedConstant, UnmatchedCloseParenthesis  {
		int openClsCounter = 0;
		Atom prevAtom = null;
		
		for (Atom atom : operandAtoms) {
			try {
				if ( atom.getName() == OParenthesis ) {
					if (prevAtom != null &&  prevAtom.getType() != AtomType.Operator) 
						throw new MissedOperator();
					openClsCounter++;
					continue;
				}
				if ( atom.getName() == CParenthesis ) {
					if ( openClsCounter == 0) {
						throw new UnmatchedOpenParenthesis() ;
					} else 
						openClsCounter--;
					if (prevAtom != null && prevAtom.getType() != AtomType.Constant)
						throw new MissedConstant();
					continue;
				}
			
				if ( atom.getType() == AtomType.Operator)
					if (!(prevAtom == null || prevAtom.getType() == AtomType.Constant || prevAtom.getName() ==  CParenthesis) && 
						 unaryFixTab.get(atom.getName()) == null ) {
							throw new MissedConstant();
					}
				
				if ( atom.getType() == AtomType.Constant ) {
					if (prevAtom != null && !(prevAtom.getType() == AtomType.Operator || prevAtom.getName() == OParenthesis) ) { 
						throw new MissedOperator();
					}
				}
			} finally {
				prevAtom = null;
			}
		}
		
		if ( openClsCounter != 0) throw new UnmatchedCloseParenthesis();
	}

	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}

	@Override
	public AtomType getType() {
		return AtomType.AbsExpr;
	}
	
}
