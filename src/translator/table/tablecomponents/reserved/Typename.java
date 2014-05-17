package translator.table.tablecomponents.reserved;

import java.util.ArrayList;

import translator.table.SymbolTable;
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
	
	public static Typename makeTypename(int size) {
		ArrayList < Typename > availableTypes = new ArrayList < Typename > ();
		castCopy(availableTypes,SymbolTable.findAllThroughPregefined(AtomType.Typename));
		for ( Typename tpn : availableTypes  )
			if ( tpn.getSize() == size) {
				return tpn;
			}
		return null;
	}
	
	@Override
	public AtomType getType() {
		return AtomType.Typename;
	}
	
	@Override
	public String toString() {
		return "Typename " + size;
	}

	@Override
	public Atom searchedPosAtom() {
		return this;
	}

}
