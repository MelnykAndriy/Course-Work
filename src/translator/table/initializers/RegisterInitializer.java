package translator.table.initializers;

import java.util.TreeMap;

import translator.exc.IncorrectBitSize;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.reserved.Register;

public abstract class RegisterInitializer {

	public static void initialize(TreeMap<String, Atom> table) {
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
		
	}
	
}
