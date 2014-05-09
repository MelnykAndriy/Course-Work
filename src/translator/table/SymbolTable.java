
package translator.table;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import translator.exc.IncorrectBitSize;
import translator.exc.PriorityException;
import translator.termworks.syntax.Operand;
import translator.termworks.syntax.AbsoluteExpr;
import translator.table.tablecomponents.*;


public class SymbolTable {
   // new variant
    private TreeMap<String,Atom> table;
    
        
    public Atom Search(String name) {
        if ( name == null ) return null;
    	return table.get(name);
    }
    
    public void AddSymbol( Atom sym ) {
    	try {
        	table.put(sym.getName(),sym);
    	} catch (NullPointerException exc) {
    		System.err.println("Error : Null was provided into AddSymbol.");
    		exc.printStackTrace();
    		System.exit(1);
    	}
    }
    
    public boolean isInTable(String name) {
    	try {
    		return table.get(name) != null;
    	} catch (NullPointerException exc) {
    		return false;
    	}
    }
    
    public boolean isIdentifier(String name) {
    	if ( name == null ) return false;
    	return table.get(name).getType() == AtomType.Identifier;
    }
    
    public boolean isCommand(String name) {
    	if ( name == null ) return false;
    	return table.get(name).getType() == AtomType.Command;
    }
    
    public boolean isRegister(String name) {
    	if ( name == null ) return false;
    	return table.get(name).getType() == AtomType.Register;
    }
    
    public boolean isLabel(String name) {
    	if ( name == null ) return false;
		return table.get(name).getType() == AtomType.Label;
    }
    
    public boolean isSegment(String name) {
    	if ( name == null ) return false;
		return table.get(name).getType() == AtomType.Segment;
    }
    
	public boolean isVariable(String name) {
    	if ( name == null ) return false;
		return table.get(name).getType() == AtomType.Variable;
	}
        

	public ArrayList<Segment> findAll(AtomType segment) {
		ArrayList < Segment > segments = new ArrayList < Segment >();
		for ( Atom atom : table.values() ) 
			if ( atom.getType() == AtomType.Segment )
				segments.add((Segment) atom);
		
		return segments;
	}    
    
    public SymbolTable() {
        table = new TreeMap<String,Atom>();
        
        //TODO : implement mechanism of filling predefined tokens
        
        // commands filling
        
        // TODO implement structure than encapsulates allowed sets of operands 
        //      and appropriate command number
		//	like 
		//	A8 ib — TEST AL,imm8
		//	A9 iw — TEST AX,imm16
		//	A9 id — TEST EAX,imm32
		//	F6 /0 ib — TEST r/m8,imm8
		//	F7 /0 iw — TEST r/m16,imm16
		//	F7 /0 id — TEST r/m32,imm32
		//	84 /r — TEST r/m8,r8
		//	85 /r — TEST r/m16,r16
		//	85 /r — TEST r/m32,r32
        // and provide this structure into Command constructor
                
        table.put("mov",new Command("mov",2));
        table.put("sti",new Command("sti",0));
        table.put("div", new Command("div",1));
        table.put("mul",new Command("mul",1));
        table.put("adc",new Command("adc",2));
        table.put("and",new Command("and",2));
        table.put("test",new Command("test",2));
        table.put("or",new Command("or",2));
        table.put("jmp",new Command("jmp",1));
        table.put("jae",new Command("jae",1));
     
        // directives filling
        StringTokenizer directives = new StringTokenizer("end;segment;ends;db;dw;dd",";");
        while ( directives.hasMoreTokens() ) {
        	String directive = directives.nextToken();
        	table.put(directive,new Directive(directive) );
        }
        
        // registers filling
        try {        	
	// registers 8
     
        	table.put("al",new Register("al",8,0));
        	table.put("cl",new Register("cl",8,1));
          	table.put("dl",new Register("dl",8,2));
        	table.put("bl",new Register("bl",8,3));
        	table.put("ah",new Register("ah",8,4));
        	table.put("ch",new Register("ch",8,5));
        	table.put("dh",new Register("dh",8,6));
        	table.put("bh",new Register("bh",8,7));      
        
    // registers 16

        	table.put("ax",new Register("ax",16,0));
        	table.put("cx",new Register("cx",16,1));
        	table.put("dx",new Register("dx",16,2));
        	table.put("bx",new Register("bx",16,3));
        	table.put("sp",new Register("sp",16,4));
        	table.put("bp",new Register("bp",16,5));
           	table.put("si",new Register("si",16,6));
        	table.put("di",new Register("di",16,7));
        	
	// register 32

        	table.put("eax",new Register("eax",32,0));
        	table.put("ecx",new Register("ecx",32,1));
        	table.put("edx",new Register("edx",32,2));
        	table.put("ebx",new Register("ebx",32,3));
        	table.put("esp",new Register("esp",32,4));
        	table.put("ebp",new Register("ebp",32,5));
        	table.put("esi",new Register("esi",32,6));
        	table.put("edi",new Register("edi",32,7));
        	
	// segment register 
        	
        	table.put("es", new Register("es",80,0,"26h"));
        	table.put("cs", new Register("cs",80,1,"2Eh"));
        	table.put("ss", new Register("ss",80,2,"36h"));
        	table.put("ds", new Register("ds",80,3,"3Eh"));
        	table.put("fs", new Register("fs",80,4,"64h"));
        	table.put("gs", new Register("gs",80,5,"65h"));        
	         
        } catch ( IncorrectBitSize exc ) {
        	System.err.println("Error : Attempt to provide incorrect bit size in the Register constructor.");
        	exc.printStackTrace();
        	System.exit(1);
        }      
        
 // operator filling
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
        	
//			table.put("and" , new Operator("and","",11) {
//				@Override
//				public Atom eval(String besideOp) throws BadConstant {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			});
					
	//
	// end of definition of operators with priority 11
	
	//
	// begin of definition of operators with priority 12
        	
//			table.put("or" , new Operator("or","",12) {
//				@Override
//				public Atom eval(String besideOp) throws BadConstant {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			});
			
//			table.put("xor" , new Operator("xor","",12) {
//				@Override
//				public Atom eval(String besideOp) throws BadConstant {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			});
			
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