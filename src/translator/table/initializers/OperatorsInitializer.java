package translator.table.initializers;

import java.util.ArrayList;
import java.util.TreeMap;

import translator.exc.PriorityException;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.reserved.operators.BinaryOperator;
import translator.table.tablecomponents.reserved.operators.UnaryOperator;
import translator.table.tablecomponents.userdefined.Constant;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;

public class OperatorsInitializer  {


	public static void initialize(TreeMap<String, Atom> table) {
	   try {
	
	//
	// begin of definition of operators with priority 1
        	
			table.put("]" , new UnaryOperator("]","Addres operator close parentheses.",1) {

				public Atom searchedPosAtom() { return this; }

				@Override
				public Operand eval(Operand op1) {
					// TODO Auto-generated method stub
					return null;
				}
			});
			
			table.put("[" , new UnaryOperator("[","Addres operator open parentheses",1) {

				public Atom searchedPosAtom() { return this; }

				@Override
				public Operand eval(Operand op1) {
					// TODO Auto-generated method stub
					return null;
				}
			});
						
			table.put("(" , new UnaryOperator("(","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
				@Override
				public Operand eval(Operand op1) {
					// TODO Auto-generated method stub
					return null;
				}
			});
			
			table.put(")" , new UnaryOperator(")","",1) {

				public Atom searchedPosAtom() { return this; }

				@Override
				public Operand eval(Operand op1) {
					// TODO Auto-generated method stub
					return null;
				}
			});
					
			
	//
	// end of definition of operators with priority 1
        	
	//
        	
	//
	// begin of definition of operators with priority 3
        	
			table.put(":",new BinaryOperator(":","Segment override operator",3) {
					@Override
					public Operand eval(Operand op1, Operand op2) {
						// TODO Auto-generated method stub
						return null;
					}
					public Atom searchedPosAtom() { return this; }
			});
			
	//
	// end of definition of operators with priority 3
	
	//
	// begin of definition of operators with priority 4
        	
			table.put("ptr",new BinaryOperator("ptr","Pointer operator.",4) {

				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
				
	//
	// end of definition of operators with priority 4

	
	//
	// begin of definition of operators with priority 6
        	
			table.put("u+" , new UnaryOperator("u+","",1) {

							public Atom searchedPosAtom() { return this; }

							@Override
							public Operand eval(Operand op1) {
								ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
								retOpAtoms.add(new Constant (( (AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() ,10 ) );
								return new AbsoluteExpr( retOpAtoms );
							}
			});
					
			table.put("u-" , new UnaryOperator("u-","",5) {
				
						public Atom searchedPosAtom() { return this; }

						@Override
						public Operand eval(Operand op1) {
							ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
							retOpAtoms.add(new Constant ( - ((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() ,10 ) );
							return new AbsoluteExpr( retOpAtoms );
						}
			});
					
			
	//
	// end of definition of operators with priority 6
	
	//
	// begin of definition of operators with priority 7
        	
			table.put("*" , new BinaryOperator("*","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() * ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("/" , new BinaryOperator("/","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() / ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("mod" , new BinaryOperator("mod","",7) {

				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() % ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
//			table.put("shl" , new Operator("shl","",7) {
//				@Override
//				public Operand eval(Operand op1, Operand op2) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				public Atom searchedPosAtom() { return this; }
//			});
//							consider implementing
//			table.put("shr" , new Operator("shr","",7) {
//				@Override
//				public Operand eval(Operand op1, Operand op2) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				public Atom searchedPosAtom() { return this; }
//			});
			
	//
	// end of definition of operators with priority 7
	
	//
	// begin of definition of operators with priority 8
        	
			table.put("+" , new BinaryOperator("+","Sum operator.",8) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() + ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("-" , new BinaryOperator("-","",8) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() - ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
							
	//
	// end of definition of operators with priority 8
				
		} catch (PriorityException e) {
			System.err.println("Error : Attempt to provide incorrect priority in the Operator constructor.");
			System.exit(1);
		}        
               
    }
}
