package translator.termworks.syntax;

import java.util.ArrayList;
import java.util.TreeMap;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;
import translator.table.tablecomponents.Constant;
import translator.table.tablecomponents.Operator;
import translator.termworks.generating.ListingGenerator;

public class AbsoluteExpr extends Operand {
	
	private int unfixedTokenNumb;

	private static TreeMap < String , Atom > unaryFixTab;
	private static String OParenthesis = "(";
	private static String CParenthesis = ")";
	
	static {
		unaryFixTab = new TreeMap < String , Atom >();
		unaryFixTab.put("+",new Constant(0,10));
		unaryFixTab.put("-",new Constant(0,10));
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
		super(unaryFix(atoms));
		unfixedTokenNumb = atoms.size();
	}
	
	public int calcSizeInBytes() {
		return evalAbsoluteExpr().getSizeInBytes();
	}
			
	private static ArrayList < Atom > unaryFix(ArrayList < Atom > absoluteExpr ) {
		ArrayList < Atom > fixedAbsExpr = new ArrayList < Atom > ();
		Atom prevAtom = null;
		
		for (Atom atom : absoluteExpr ) {
			if ( atom instanceof Operator && !(prevAtom instanceof Constant)  
					&& unaryFixTab.get(atom.getName()) != null )
				fixedAbsExpr.add(unaryFixTab.get(atom.getName()));
			fixedAbsExpr.add(atom);
			prevAtom = atom;
		}
		return fixedAbsExpr;
	}
	
	public int tokenNumb() {
		return unfixedTokenNumb;
	}
	
	public void calc() {
		Constant evaluetedConstant = evalAbsoluteExpr();
		operandAtoms.clear();
		operandAtoms.add(evaluetedConstant);
	}
	
	public Constant evalAbsoluteExpr (){
		if ( isAbsoluteTerm() ) 
			return (Constant) operandAtoms.get(0);
		if ( isParenthesisRemoveNeeded() ) 
			removeParenthesis();
		int lowestPrOpPos = posLowPriority();
		Operand leftOp = new AbsoluteExpr(new ArrayList < Atom >(operandAtoms.subList(0,lowestPrOpPos)));
		Operand rightOp = new AbsoluteExpr(new ArrayList < Atom >(operandAtoms.subList(lowestPrOpPos + 1,operandAtoms.size())) );
		return (( AbsoluteExpr) ((Operator) operandAtoms.get(lowestPrOpPos)).eval(leftOp,rightOp)).evalAbsoluteExpr();
	}
	
	private boolean isAbsoluteTerm() {
		if ( operandAtoms.size() == 1 &&
				operandAtoms.get(0) instanceof Constant )
			return true;
		return false;
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
				
		return posLowestPr;
	}
	
	public boolean isValidAbsExpr() {
		// TODO 
		return true;
	}
	
}
