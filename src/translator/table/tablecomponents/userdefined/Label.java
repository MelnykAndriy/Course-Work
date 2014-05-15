
package translator.table.tablecomponents.userdefined;

import java.util.regex.Pattern;

import translator.lexer.ParsedLine;
import translator.table.tablecomponents.AtomType;

public class Label extends Identifier {
	public enum LabelType { NEAR,FAR };
	private LabelType labelType;
	
	public final static String labelRegex = "^\\s*(" + Identifier.identRegex + ")\\s*[:](.*)"; 
	
	public final static Pattern labelPattern = Pattern.compile(labelRegex);
		
    public Label(String _name,LabelType type) {
        super(_name);
        this.setLabelType(type);
    }
	
    public Label(String _name,LabelType type, ParsedLine lineWhereDef) {
        super(_name);
        whereDef = lineWhereDef;
        this.setLabelType(type);
    }
        
	@Override
    public AtomType getType() {
        return AtomType.Label;
    }
    
    
    public String toString() {
    	return "Label";
    }

	public LabelType getLabelType() {
		return labelType;
	}
	
	public void setLabelType(LabelType labelType) {
		this.labelType = labelType;
	}
    
    @Override
    public String identTypeToString() {
    	return labelType.toString();
    }

    
}