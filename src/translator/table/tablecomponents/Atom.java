
package translator.table.tablecomponents;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Atom {
    private String name;
    
    public abstract AtomType getType();
    
    public abstract Atom searchedPosAtom() ;
    
    public String getName() {
        return name;
    }
    
    public Atom(String name) {
        this.name = name.toLowerCase();
    }
    
    @SuppressWarnings("unchecked")
	public static <T1 extends Atom ,T2 extends Atom > Collection<T1> castCopy(Collection <T1> dest,Collection < T2 > source ) {
    	dest.clear();
    	for (T2 sourceComponent : source) 
    		dest.add((T1) sourceComponent);
    	return dest;
    }
    
    @SuppressWarnings("unchecked")
	public static <T1 extends Atom ,T2 extends Atom > Collection<T1> castAppend(Collection <T1> dest,Collection < T2 > source ) {
    	for (T2 sourceComponent : source) 
    		dest.add((T1) sourceComponent);
    	return dest;
    }
    
	public static String buildStringFromAtoms(ArrayList< Atom > atoms) {
		StringBuffer retStr = new StringBuffer();	
		int i = 0;
		for (Atom atom : atoms) {
			retStr.append(atom.getName() + ((i++ > 0 )?(" "):("")));
		}
		return retStr.toString();
	}
}