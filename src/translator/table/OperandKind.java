package translator.table;

public enum OperandKind {
	rel8,rel16,rel32,
	r8,r16,r32,
	m8,m16,m32,
	imm8,imm16,imm32;
		
	final public static byte MEMORY = 0;  
	final public static byte RELATIVE = 1;
	final public static byte REGISTER = 2;
	final public static byte ABSOLUTE = 3;
	public static OperandKind whatKind(byte operType , int size ) {
		switch ( operType ) {
			case MEMORY:
				return whatKindOfMemory(size);
			case RELATIVE:
				return whatKindOfRelative(size);
			case REGISTER:
				return whatKindOfRegister(size);
			case ABSOLUTE:
				return whatKindOfAbsolute(size);
		}
		
		return null;
	}
	
	public static int sizeForOperandKind(OperandKind kind) {
		switch (kind) {
		case rel8:
		case r8:
		case imm8:
		case m8:
			return 1;
		case rel16:
		case r16:
		case imm16:
		case m16:
			return 2;
		case rel32:
		case r32:
		case imm32:
		case m32:
			return 4;
		}
		return 0;
	}
	
	private static OperandKind whatKindOfMemory(int byteSize) {
		switch (byteSize) {
			case 1:	return m8;
			case 2:	return m16;
			case 4: return m32;
		}
		return null;
	}
	
	private static OperandKind whatKindOfRelative(int byteSize) {
		switch (byteSize) {
			case 1:	return rel8;
			case 2: return rel16;
			case 4: return rel32;
		}
		return null;
	}
	
	private static OperandKind whatKindOfRegister(int byteSize) {
		switch (byteSize) {
			case 1:	return r8;
			case 2:	return r16;
			case 4: return r32;
		}
		return null;
	}
	
	private static OperandKind whatKindOfAbsolute(int byteSize) {
		switch (byteSize) {
			case 1:	return imm8;
			case 2:	return imm16;
			case 4: return imm32;
		}
		return null;
	}
	
}
