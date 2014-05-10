
package translator.table.tablecomponents;
import translator.exc.*;

public class Register extends Atom {
    public enum RegUsage { CommonReg, DataReg , StachPtr, SegReg };
	
	private byte bitSize;
    private int regNumb;
    private String pre;
    private RegUsage regType;
    
    //TODO implement enum that will be encapsulate register appointment
    // like (segmentRegister,base, ... )
    public Register(String _name,int bitSize,int regNumb)
    			throws IncorrectBitSize {
        this(_name,bitSize,regNumb,"");
    }    
    
    public Register(String _name,int bitSize,int regNumb, String pre )
    			throws IncorrectBitSize {
    	this(_name,bitSize,regNumb,pre,
				( pre.length() == 0) ?(RegUsage.DataReg):(RegUsage.SegReg));
    }
    
    private Register(String name, int bitSize, int regNumb, String pre, RegUsage regType)
    			throws IncorrectBitSize {
		super(name);
		 if (!CheckBitSize(bitSize)) throw new IncorrectBitSize();
		this.bitSize = (byte) bitSize;
		this.regNumb = regNumb;
		this.pre = pre;
		this.regType = regType;
	}
    
	public int GetBitSize() { 
        return bitSize;
    }
    
    public int GetByteSize() {
        return bitSize*8;
    }
    
    public int getRegNumb() {
    	return regNumb;
    }
    
    @Override
    public AtomType getType() {
        return AtomType.Register;
    }   
    
    public static boolean CheckBitSize(int size) {
        if (size == 8 | size == 16 | size == 32 | size == 80)
            return true;
        return false;
    }
    
    public static boolean CheckByteSize(int size) {
        return CheckBitSize(size*8);
    }

    public String toString() {
    	if (regType == RegUsage.SegReg) return "Segment register.";
    	else 
    		return "Register" + " " + bitSize;
    }
    
    @Override
	public Atom searchedPosAtom() {
		return this;
	}
    
}
