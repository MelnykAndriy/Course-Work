package translator.termworks.syntax.operands;

import java.util.ArrayList;
import java.util.regex.*; 

import translator.exc.BaseIndexCombinationNotAllowed;
import translator.table.OperandKind;
import translator.table.SymbolTable;
import translator.table.tablecomponents.*;
import translator.table.tablecomponents.reserved.*;
import translator.table.tablecomponents.userdefined.*;

public class MemoryOperand extends Operand {
	private Register segChanger;
	private Register base;
	private Register index;
	private Typename type;
	private int scale = 1;
	private Constant offsetInCommand = null;
	
	private Variable direct;
	
	private static String segmentRegs = "((ds)|(ss)|(es)|(fs)|(gs)|(cs))";
	private static String reg32 = "((eax)|(ebx)|(ecx)|(edx)|(esp)|(ebp)|(esi)|(edi))";
	private static String reg16 = "((ax)|(bx)|(dx)|(cx)|(si)|(di)|(sp)|(bp))";
	private static String regs32sum = "(" + reg32 + "\\s*[+]\\s*" + reg32 + ")";
	private static String regs16sum = "(" + reg16 + "\\s*[+]\\s*" + reg16 + ")";
	private static String regs32doubleParenthesis = "\\[\\s*" + reg32 + "\\s*\\]\\s*\\[\\s*" + reg32 + "\\s*\\]";
	private static String regs16doubleParenthesis = "\\[\\s*" + reg16 + "\\s*\\]\\s*\\[\\s*" + reg16 + "\\s*\\]";
	private static String allowedTypes = "((byte)|(word)|(dword))";
	
	public static String sumBaseIndexAddressingWithOutPtrR = "\\s*" + segmentRegs + "\\s*:\\s*\\[\\s*(" + regs32sum + "|" + regs16sum + ")\\s*\\]\\s*";
	public static String sumBaseIndexAddressingWithPtrR = "\\s*" + allowedTypes + "\\s*ptr\\s*" + sumBaseIndexAddressingWithOutPtrR;
	public static String doubleParenthesisBaseIndexWithOutR = "\\s*" + segmentRegs + "\\s*:\\s*((" + regs32doubleParenthesis + ")|(" + regs16doubleParenthesis + "))\\s*";
	public static String doubleParenthesisBaseIndexWithR = "\\s*" + allowedTypes + "\\s*ptr\\s*" + doubleParenthesisBaseIndexWithOutR;
	
	// [0000]
	public static String directAddressingWithOutPtr = "\\s*" + segmentRegs + "\\s*:\\s*" + Identifier.identRegex + "\\s*";
	public static String directAddressingWithPtr = "\\s*" + allowedTypes + "\\s*ptr\\s*" + directAddressingWithOutPtr;
	
	private static Pattern sumBaseIndexAddrWithOutPtrP = Pattern.compile("^" + sumBaseIndexAddressingWithOutPtrR + "$");
	private static Pattern sumBaseIndexAddrWithPtrP = Pattern.compile("^" + sumBaseIndexAddressingWithPtrR + "$");
	private static Pattern doubleParenthesisBaseIndexWithOutP = Pattern.compile("^" + doubleParenthesisBaseIndexWithOutR + "$");
	private static Pattern doubleParenthesisBaseIndexWithP = Pattern.compile("^" + doubleParenthesisBaseIndexWithR + "$");
	private static Pattern directAddrWithOutPtrRegex = Pattern.compile("^" + directAddressingWithOutPtr + "$");
	private static Pattern directAddrWithPtrRegex = Pattern.compile("^" + directAddressingWithPtr + "$");
	
	public MemoryOperand(ArrayList<Atom> atoms) {
		super(atoms);
		String checkOperand = buildStringFromAtoms(operandAtoms);
		Matcher sumBaseIndexWith = sumBaseIndexAddrWithPtrP.matcher(checkOperand);
		Matcher sumBaseIndexWithout = sumBaseIndexAddrWithOutPtrP.matcher(checkOperand);
		Matcher doubleBaseIndexWith =	doubleParenthesisBaseIndexWithP.matcher(checkOperand);
		Matcher doubleBaseIndexWithout = doubleParenthesisBaseIndexWithOutP.matcher(checkOperand);
		Matcher directWith = directAddrWithPtrRegex.matcher(checkOperand);
		Matcher directWithout = directAddrWithOutPtrRegex.matcher(checkOperand);
		
		if ( sumBaseIndexWith.matches() )
			sumBaseIndexWithPtrInit(atoms);
		if ( sumBaseIndexWithout.matches() )
			sumBaseIndexWituotPtrInit(atoms,0);
		if ( directWith.matches() )
			directWithPtrInit(atoms);
		if ( directWithout.matches() ) 
			directWithoutPtrInit(atoms,0);
		if ( doubleBaseIndexWith.matches() ) 
			doubleBaseIndexInitWith(atoms);
		if ( doubleBaseIndexWithout.matches() )
			doubleBaseIndexInitWithout(atoms,0);
	}
	
	private void doubleBaseIndexInitWithout(ArrayList<Atom> atoms, int start) {
		segChanger = (Register) atoms.get(start);
		base = (Register) atoms.get(start + 3);
		index = (Register) atoms.get(start + 6);
	}

	private void doubleBaseIndexInitWith(ArrayList<Atom> atoms) {
		type = (Typename) atoms.get(0);
		doubleBaseIndexInitWithout(atoms,2);
		operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
	}

	private void directWithPtrInit(ArrayList<Atom> atoms) {
		type = (Typename) atoms.get(0);		
		directWithoutPtrInit(atoms,2);
		operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
	}

	private  void directWithoutPtrInit(ArrayList<Atom> atoms, int start) {
		segChanger = (Register) atoms.get(start);
		direct = (Variable) atoms.get(start + 2);
		if ( type == null ) {
			type = Typename.makeTypename(direct.Size());
			operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
		}
	}
	
	private void sumBaseIndexWithPtrInit(ArrayList<Atom> atoms) {
		type = (Typename) atoms.get(0);
		sumBaseIndexWituotPtrInit(atoms,2);		
		operKind = OperandKind.whatKind(OperandKind.MEMORY,type.getSize());
	}
		
	private void sumBaseIndexWituotPtrInit(ArrayList<Atom> atoms, int start) {
		segChanger = (Register) atoms.get(start + 0);
		base = (Register) atoms.get(start + 3);
		index = (Register) atoms.get(start + 5);
	}

	public static boolean isMemoryOperand(ArrayList < Atom > operandAtoms) {
		String checkOperand = buildStringFromAtoms(operandAtoms);
		Matcher sumBaseIndexWith = sumBaseIndexAddrWithOutPtrP.matcher(checkOperand);
		Matcher sumBaseIndexWithout = sumBaseIndexAddrWithPtrP.matcher(checkOperand);
		Matcher doubleBaseIndexWith =	doubleParenthesisBaseIndexWithP.matcher(checkOperand);
		Matcher doubleBaseIndexWithout = doubleParenthesisBaseIndexWithOutP.matcher(checkOperand);
		Matcher directWith = directAddrWithPtrRegex.matcher(checkOperand);
		Matcher directWithout = directAddrWithOutPtrRegex.matcher(checkOperand);
				
		return sumBaseIndexWith.matches() || sumBaseIndexWithout.matches() || 
				 doubleBaseIndexWith.matches() || doubleBaseIndexWithout.matches() ||
				(directWith.matches() && ((Identifier) operandAtoms.get(4)).getType() == AtomType.Variable ) || 
				(directWithout.matches() && ((Identifier) operandAtoms.get(2)).getType() == AtomType.Variable ) ;
	}
		
	public int getDirectOffset() {
		return direct.getOffset();
	}
	
	public int getOffsetInComand() {	
		return (int) offsetInCommand.GetVaue();
	}
	
	public int getSizeOfOffsetInCommand() {
		return offsetInCommand.getSizeInBytes();
	}
	
	public boolean isOffsetPresent() { 	
		return offsetInCommand != null;
	}
	
	public boolean isSibNeeded() {
		if ( base != null && index != null && base.GetBitSize() == 32)
			return true;
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

	public String getReplacementByte() {
		return segChanger.getReplacementByte();
	}

	public Register getReplacementReg() {
		return segChanger;
	}
	
	@Override
	public OperandKind getOperandKind() {
		return operKind;
	}
	
	public void isValidMemory() throws BaseIndexCombinationNotAllowed {
		if ( base != null && !RmContainer.matches(base, index) )
			throw new BaseIndexCombinationNotAllowed();
	}

	@Override
	public AtomType getType() {
		return AtomType.Memory;
	}
	
	public Register getStandardSegReg() {
		if (base != null && (base.equals(SymbolTable.getReserved("bp")) || base.equals(SymbolTable.getReserved("ebp")) 
							|| base.equals(SymbolTable.getReserved("esp")) ))
			return (Register) SymbolTable.getReserved("ss");
		return (Register) SymbolTable.getReserved("ds");
	}

	public boolean isRegReplacement() {
		return !segChanger.equals(getStandardSegReg());
	}

	public boolean isDirect() {
		return direct != null;
	}
	
}
