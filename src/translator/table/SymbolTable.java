
package translator.table;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import translator.table.initializers.*;
import translator.table.tablecomponents.*;


public class SymbolTable {
   // new variant
    private TreeMap<String,Atom> table;
    private static TreeSet < String > reserved = new TreeSet < String >();
    
    static {
    	reserved.add("mov");
    	// etc
    }
        
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
        CommandInitializer.initialize(table);
        OperatorsInitializer.initialize(table);
        RegisterInitializer.initialize(table);
        DirectiveInitialier.initialize(table);
    }

}