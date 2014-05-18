
package translator.table.tablecomponents.userdefined;

import java.util.regex.Pattern;

import translator.exc.BadConstant;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class Constant extends Atom {
    private long value;
    private int rd;
    
  // constant regular expression 
	public final static String constantRegex = "[+-]?([01]+[Bb]|[0-9]+[Dd]?|[0-7]+[QqOo]|\\d[0-9a-fA-F]{0,}[Hh])";
    
  // constant pattern
	public final static Pattern constantPattern = Pattern.compile("^" + constantRegex + "$");;
    	
    public Constant(long val,int radix) {
        super(Long.toString(val,radix));
        this.value = val;
        rd = radix;
    }
    
    public Constant(String strRepresentation) throws BadConstant {
    	super(strRepresentation);
    	value = ConstantValue(strRepresentation);
    	rd = getConstantRadix(strRepresentation);
    }
    
    public int GetRadix() {
        return rd;
    }    
    
    public long GetVaue() {
    	return value;
    }
    
    public String GetStringRepresentation() {
    	return getName();
    }

    @Override
    public AtomType getType() {
        return AtomType.Constant;
    }
	
    public static boolean isConstant(String token) {
    	return token.matches(constantPattern.pattern());
    }
    
    final public static long ConstantValue(String constant) throws BadConstant {
		return Long.valueOf((Character.isDigit( constant.charAt(constant.length() - 1 ) ))
								?(constant):(constant.substring(0, constant.length() - 1 )) , getConstantRadix(constant) );
    }
    
    public int getSizeInBytes() {
    	if ( value >= -128 && value <= 255 ) return 1;
    	if ( value >= -32768 && value <= 65535 ) return 2;
    	if ( value >= (long) -2147483648 && value <= (long) 2*2147483647) return 4;
    	return 8;
    }
    
    public static int getConstantRadix(String constant) throws BadConstant {
    	if ( !isConstant(constant) ) throw new BadConstant();
    	if ( constant.matches(".*[hH]$") ) return 16;
    	if ( constant.matches(".*[bB]$") ) return 2;
    	if ( constant.matches(".*[QqOo]") ) return 8;
    
    	return 10;
    }
    
    public String toString() {
    	return "Constant with radix " + rd; 
    }
    
    @Override
	public Atom searchedPosAtom() {
		return this;
	}
    
}