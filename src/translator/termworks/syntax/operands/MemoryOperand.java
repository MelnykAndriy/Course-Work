package translator.termworks.syntax.operands;

import java.util.ArrayList;
import java.util.regex.*; 

import translator.table.OperandKind;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.*;
import translator.table.tablecomponents.userdefined.*;

public class MemoryOperand extends Operand {
	private Register segChanger;
	private Register base;
	private Register index;
	private Typename type;
	private int scale;
	
	private Variable direct;
	
	private static String segmentRegs = "((ds)|(ss)|(es)|(fs)|(gs)|(cs))";
	private static String reg32 = "((eax)|(ebx)|(ecx)|(edx)|(esp)|(ebp)|(esi)|(edi))";
	private static String reg16 = "((ax)|(bx)|(dx)|(cx)|(si)|(di)|(sp)|(bp))";
	private static String regs32sum = "(" + reg32 + "\\s*[+]\\s*" + reg32 + ")";
	private static String regs16sum = "(" + reg16 + "\\s*[+]\\s*" + reg16 + ")";
	private static String allowedTypes = "((byte)|(word)|(dword))";
	
	public static String baseIndexAddressingWithOutPtr = "\\s*" + segmentRegs + "\\s*:\\s*\\[\\s*(" + regs32sum + "|" + regs16sum + ")\\s*\\]\\s*";
	public static String baseIndexAddressingWithPtr = "\\s*" + allowedTypes + "\\s*ptr\\s*" + baseIndexAddressingWithOutPtr;
	public static String directAddressingWithOutPtr = "\\s*" + segmentRegs + "\\s*:\\s*" + Identifier.identRegex + "\\s*";
	public static String directAddressingWithPtr = "\\s*" + allowedTypes + "\\s*ptr\\s*" + directAddressingWithOutPtr;
	
	private static Pattern baseIndexAddrWithOutPtrRegex = Pattern.compile("^" + baseIndexAddressingWithOutPtr + "$");
	private static Pattern baseIndexAddrWithPtrRegex = Pattern.compile("^" + baseIndexAddressingWithPtr + "$");
	private static Pattern directAddrWithOutPtrRegex = Pattern.compile("^" + directAddressingWithOutPtr + "$");
	private static Pattern directAddrWithPtrRegex = Pattern.compile("^" + directAddressingWithPtr + "$");
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
		String checkOperand = buildStringFromAtoms(operandAtoms);
		Matcher baseIndexWith = baseIndexAddrWithPtrRegex.matcher(checkOperand);
		Matcher baseIndexWithout = baseIndexAddrWithOutPtrRegex.matcher(checkOperand);
		Matcher directWith = directAddrWithPtrRegex.matcher(checkOperand);
		Matcher directWithout = directAddrWithOutPtrRegex.matcher(checkOperand);
		
		if ( baseIndexWith.matches() )
			baseIndexWithPtrInit(atoms);
		if ( baseIndexWithout.matches() )
			baseIndexWituotPtrInit(atoms,0);
		if ( directWith.matches() )
			directWithPtrInit(atoms);
		if ( directWithout.matches() ) 
			directWithoutPtrInit(atoms,0);
		// throw SomethingWrong();
	}
	
	private void directWithPtrInit(ArrayList<Atom> atoms) {
		type = (Typename) atoms.get(0);		
		directWithoutPtrInit(atoms,2);
		operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
	}

	private  void directWithoutPtrInit(ArrayList<Atom> atoms, int start) {
		direct = (Variable) atoms.get(start + 2);
		if ( type == null ) {
			type = Typename.makeTypename(direct.Size());
			operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
		}
	}
	
	private void baseIndexWithPtrInit(ArrayList<Atom> atoms) {
		type = (Typename) atoms.get(0);
		baseIndexWituotPtrInit(atoms,2);		
		operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
	}
		
	private void baseIndexWituotPtrInit(ArrayList<Atom> atoms, int start) {
		segChanger = (Register) atoms.get(start + 0);
		base = (Register) atoms.get(start + 0);
		index = (Register) atoms.get(start + 0);
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) {
		String checkOperand = buildStringFromAtoms(operandAtoms);
		Matcher baseIndexWith = baseIndexAddrWithPtrRegex.matcher(checkOperand);
		Matcher baseIndexWithout = baseIndexAddrWithOutPtrRegex.matcher(checkOperand);
		Matcher directWith = directAddrWithPtrRegex.matcher(checkOperand);
		Matcher directWithout = directAddrWithOutPtrRegex.matcher(checkOperand);
				
		return baseIndexWith.matches() || baseIndexWithout.matches() || 
				(directWith.matches() && ((Identifier) operandAtoms.get(4)).getType() == AtomType.Variable ) || 
				(directWithout.matches() && ((Identifier) operandAtoms.get(2)).getType() == AtomType.Variable ) ;
	}
		
	public int getOffset() {
		return direct.getOffset();
	}
	
	public int getOffsetInBytes() {	// please, rename this method
		// TODO
		return 0;
	}
	
	public boolean isOffsetPresent() { 	
		return direct != null;
	}
	
	public boolean isSibNeeded() {
		//TODO
		return false;
	}
	
	public int getScale() {
		return scale;
	}
	
	public Register getBase() {
		return base;
	}
	
	public Register getIndex() {
		return index;
	}
		
	
	public boolean canDetermineType() {
		if ( type != null )
			return true;
		return false;
	}
	
	public void determineType(int size) {
		type = Typename.makeTypename(size);
		operKind = OperandKind.whatKind(OperandKind.MEMORY,size);
	}
	
	@Override
	public int calcSizeInBytes() {
		return type.getSize();
	}

	public Register getSegReplacement() {
		return segChanger;
	}

	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}
	
	public void isValidMemory() throws Exception {
		
	}

	@Override
	public AtomType getType() {
		return AtomType.Memory;
	}

}
