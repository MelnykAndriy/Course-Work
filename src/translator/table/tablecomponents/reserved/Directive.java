
package translator.table.tablecomponents.reserved;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class Directive extends Mnemocode {
    
    public Directive(String _name) {
        super(_name);
    }

    @Override
    public AtomType getType() {
        return AtomType.Directive;
    }

    public String toString() {
    	return "Directive";
    }
    
    @Override
	public Atom searchedPosAtom() {
		return this;
	}
}