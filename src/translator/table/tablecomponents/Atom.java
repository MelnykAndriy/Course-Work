
package translator.table.tablecomponents;

public abstract class Atom {
    private String name;
    
    public abstract AtomType getType();
    
    public abstract Atom searchedPosAtom() ;
    
    public String getName() {
        return name;
    }
    
    public Atom(String name) {
        this.name = name;
    }

}