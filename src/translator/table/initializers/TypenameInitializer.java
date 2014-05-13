package translator.table.initializers;

import java.util.TreeMap;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.reserved.Typename;

public abstract class TypenameInitializer {
	public static void initialize(TreeMap<String, Atom> table) {
		
		table.put("byte", new Typename("byte",1));
		table.put("word",new Typename("word", 2));
		table.put("dword",new Typename("dword",4));
	}
}
