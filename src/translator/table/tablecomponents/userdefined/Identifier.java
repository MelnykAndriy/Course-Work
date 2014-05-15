
package translator.table.tablecomponents.userdefined;

import java.util.regex.Pattern;

import translator.lexer.ParsedLine;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class Identifier extends Atom {
    private int offset;	
	protected boolean isUsed;
	protected ParsedLine whereDef;
	
// Identifier regular expression 	
 	public final static String identRegex = "[a-zA-Z_@$?][a-zA-Z0-9_@$?]{0,32}";

 // Identifier pattern 
 	public final static Pattern allowedIdentifierPattern = Pattern.compile("^" + identRegex + "$");
       
	public static boolean isIdentifierAllowed(String ident) {
		return ident.matches(allowedIdentifierPattern.pattern());
	}

    public Identifier(String _name) {
        super(_name);
        offset = - 1;
        isUsed = false;
    }
	
	public boolean isIdentUsed() {
		return isUsed;
	}

    public void usageFound() {
    	isUsed = true;
    }
    	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
    @Override
    public AtomType getType() {
        return AtomType.Identifier;
    }
    
    public String toString() {
    	return "Identifier";
    }
    
    public String identTypeToString() {
    	return "no type";
    }

    @Override
	public Atom searchedPosAtom() {
		return this;
	}

	public ParsedLine getLineWhereDefined() {
		return whereDef;
	}
	
	public void setLineWhereDefined(ParsedLine line ) {
		whereDef = line;
	}
}