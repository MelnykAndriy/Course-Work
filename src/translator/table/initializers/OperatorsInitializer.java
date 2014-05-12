package translator.table.initializers;

import java.util.ArrayList;
import java.util.TreeMap;

import translator.exc.PriorityException;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.Constant;
import translator.table.tablecomponents.Operator;
import translator.termworks.syntax.operands.AbsoluteExpr;
import translator.termworks.syntax.operands.Operand;

public class OperatorsInitializer  {


	public static void initialize(TreeMap<String, Atom> table) {
	   try {
	
	//
	// begin of definition of operators with priority 1
        	
			table.put("]" , new Operator("]","Addres operator close parentheses.",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("[" , new Operator("[","Addres operator open parentheses",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("length" , new Operator("length","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("size" , new Operator("size","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("with" , new Operator("with","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("mask" , new Operator("mask","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("(" , new Operator("(","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put(")" , new Operator(")","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("<" , new Operator("<","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put(">" , new Operator(">","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			
	//
	// end of definition of operators with priority 1
        	
	//
	// begin of definition of operators with priority 2
        	
			table.put("." , new Operator(".","",2) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
	//
	// end of definition of operators with priority 2
        	
	//
	// begin of definition of operators with priority 3
        	
			table.put(":",new Operator(":","Segment override operator",3) {
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
        	
			table.put("ptr",new Operator("ptr","Pointer operator.",4) {

				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("offset",new Operator("offset","",4) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("seg",new Operator("seg","",4) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("type",new Operator("type","",4) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("this",new Operator("this","",4) {
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
	// begin of definition of operators with priority 5
        	
			table.put("high",new Operator("high","",5) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("low",new Operator("low","",5) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
	//
	// end of definition of operators with priority 5
	
	//
	// begin of definition of operators with priority 6
        	
			table.put("u+" , new Operator("+","",1) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("u-" , new Operator("-","",5) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			
	//
	// end of definition of operators with priority 6
	
	//
	// begin of definition of operators with priority 7
        	
			table.put("*" , new Operator("*","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() * ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("/" , new Operator("/","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() / ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("mod" , new Operator("mod","",7) {

				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("shl" , new Operator("shl","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("shr" , new Operator("shr","",7) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
	//
	// end of definition of operators with priority 7
	
	//
	// begin of definition of operators with priority 8
        	
			table.put("+" , new Operator("+","Sum operator.",8) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					ArrayList < Atom > retOpAtoms = new ArrayList < Atom > ();
					retOpAtoms.add(new Constant (
					(((AbsoluteExpr)op1).evalAbsoluteExpr().GetVaue() + ((AbsoluteExpr)op2).evalAbsoluteExpr().GetVaue()),10));
					return new AbsoluteExpr(retOpAtoms);
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("-" , new Operator("-","",8) {
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
	
	//
	// begin of definition of operators with priority 9
        	
			table.put("eq" , new Operator("eq","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("ne" , new Operator("ne","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("lt" , new Operator("lt","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("le" , new Operator("le","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("gt" , new Operator("gt","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
					
			table.put("ge" , new Operator("ge","",9) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
						
	//
	// end of definition of operators with priority 9
	
	//
	// begin of definition of operators with priority 10
        	
			table.put("not" , new Operator("not","",10) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
						
	//
	// end of definition of operators with priority 10
	
	//
	// begin of definition of operators with priority 11
        	
//						table.put("and" , new Operator("and","",11) {
//							@Override
//							public Atom eval(String besideOp) throws BadConstant {
//								// TODO Auto-generated method stub
//								return null;
//							}
//						});
					
	//
	// end of definition of operators with priority 11
	
	//
	// begin of definition of operators with priority 12
        	
//						table.put("or" , new Operator("or","",12) {
//							@Override
//							public Atom eval(String besideOp) throws BadConstant {
//								// TODO Auto-generated method stub
//								return null;
//							}
//						});
			
//						table.put("xor" , new Operator("xor","",12) {
//							@Override
//							public Atom eval(String besideOp) throws BadConstant {
//								// TODO Auto-generated method stub
//								return null;
//							}
//						});
			
	//
	// end of definition of operators with priority 12
	
	//
	// begin of definition of operators with priority 13
        	
			table.put("type" , new Operator("type","",13) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
			table.put("short" , new Operator("short","",13) {
				@Override
				public Operand eval(Operand op1, Operand op2) {
					// TODO Auto-generated method stub
					return null;
				}
				public Atom searchedPosAtom() { return this; }
			});
			
	//
	// end of definition of operators with priority 13
 				
			
		} catch (PriorityException e) {
			System.err.println("Error : Attempt to provide incorrect priority in the Operator constructor.");
			System.exit(1);
		}        
               
    }
}
