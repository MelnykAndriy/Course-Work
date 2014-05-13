package translator.table.tablecomponents.reserved;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class Typename extends Atom {
	private int size;
	
	public Typename(String name,int size) {
		super(name);
		this.size = size;
	}

	public int getSize() {
		return size;
	}
	
	@Override
	public AtomType getType() {
		return AtomType.Typename;
	}

	@Override
	public Atom searchedPosAtom() {
		return this;
	}

}
