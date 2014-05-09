
package translator.table.tablecomponents;

import java.util.regex.Pattern;

public class Identifier extends Atom {
    private int offset;	
	
// Identifier regular expression 	
 	public final static String identRegex = "[a-zA-Z_@$?][a-zA-Z0-9_@$?]{0,32}";

 // Identifier pattern 
 	public final static Pattern allowedIdentifierPattern = Pattern.compile("^" + identRegex + "$");
       
	public static boolean isIdentifierAllowed(String ident) {
		return ident.matches(allowedIdentifierPattern.pattern());
	}
        
    @Override
    public AtomType getType() {
        return AtomType.Identifier;
    }
    
    public Identifier(String _name) {
        super(_name);
    }

    public String toString() {
    	return "Identifier";
    }

    @Override
	public Atom searchedPosAtom() {
		return this;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
    
}