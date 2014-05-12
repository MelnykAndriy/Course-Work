package translator.table.tablecomponents;

public class Variable extends Identifier {
    public enum DataType { BYTE,WORD,DWORD }
    private DataType type;
	
	public Variable(Identifier ident, DataType type) {
		super( ident.getName() );
		this.type = type;
	}
	
	public Variable(String name,DataType type) {
		super( name );
		this.type = type;
	}
		
    public static int GetSize(Variable obj) {
        byte retVal = 0;
        switch (obj.type) {
            case BYTE :
                retVal = 1;
                break;
            case WORD :
                retVal = 2;
                break;
            case DWORD :
                retVal = 4;
                break;
            default :
                System.exit(1);
        }
        return retVal;
    }
    
    public static DataType whatType(Directive defDirective) {
    	switch (defDirective.getName() ) {
	    	case "db": return DataType.BYTE; 
	    	case "dw": return DataType.WORD;
	    	case "dd": return DataType.DWORD;
    	}
    	return null;
    }
    
    
    public int Size() {
        return GetSize(this);
    }
    
    public String toString() {
    	return "Variable";
    } 
    
    @Override
    public String identTypeToString() {
    	return type.toString();
    }
    
    @Override
    public AtomType getType() {
        return AtomType.Variable;
    }
    
}
