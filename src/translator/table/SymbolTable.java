
package translator.table;

import java.util.ArrayList;
import java.util.TreeMap;

import translator.table.initializers.*;
import translator.table.tablecomponents.*;

public class SymbolTable {
   // new variant
    private TreeMap<String,Atom> table;
    private static TreeMap < String , Atom > predefined; 
       
    static {
    	predefined = new TreeMap< String, Atom > ();
        CommandInitializer.initialize(predefined);
        OperatorsInitializer.initialize(predefined);
        RegisterInitializer.initialize(predefined);
        DirectiveInitialier.initialize(predefined);
        TypenameInitializer.initialize(predefined);
    }
       
    public SymbolTable() {
        table = new TreeMap<String,Atom>();
        table.putAll(predefined);
    }    
    
    public Atom Search(String name) {
        if ( name == null ) return null;
    	return table.get(name.toLowerCase());
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
    
    public static boolean isReserved(String name) {
    	return predefined.containsKey(name);
    }
    
    public static Atom getReserved(String name) {
    	return predefined.get(name);
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
        
	public ArrayList< Atom > findAll(AtomType what) {
		return findInMap(table,what);
	}    

	public static ArrayList< Atom > findAllThroughPregefined(AtomType what) {
		return findInMap(predefined, what);
	}
	
	private static ArrayList < Atom > findInMap(TreeMap < String, Atom > tab, AtomType what) {
		ArrayList < Atom > segments = new ArrayList < Atom >();
		for ( Atom atom : tab.values() ) 
			if ( atom.getType() == what)
				segments.add( atom);
		
		return segments;
	}
}