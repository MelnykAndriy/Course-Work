
package translator.table.tablecomponents;

import java.util.regex.Pattern;

public class Label extends Identifier {

	public final static String labelRegex = "^\\s*(" + Identifier.identRegex + ")\\s*[:](.*)"; 
	
	public final static Pattern labelPattern = Pattern.compile(labelRegex);
	
	
    public Label(String _name) {
        super(_name);
    }
    
    @Override
    public AtomType getType() {
        return AtomType.Label;
    }
    
    public String toString() {
    	return "Label";
    }
    
}