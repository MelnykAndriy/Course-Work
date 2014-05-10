
package translator.table.tablecomponents;

import java.util.regex.Pattern;

public class Label extends Identifier {
	public enum LabelType { NEAR,FAR };
	private LabelType labelType;
	
	public final static String labelRegex = "^\\s*(" + Identifier.identRegex + ")\\s*[:](.*)"; 
	
	public final static Pattern labelPattern = Pattern.compile(labelRegex);
		
    public Label(String _name,LabelType type) {
        super(_name);
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