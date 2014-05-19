package translator.termworks.syntax.operands;

import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import translator.table.SymbolTable;
import translator.table.tablecomponents.reserved.Register;

abstract public class RmContainer {
	private static Stack < Entry < Integer , Entry< Register, Register > > > rmSuit;
	
	static {
		rmSuit = new Stack < Entry < Integer , Entry< Register, Register > > > ();
	// 16
		rmSuit.push(new SimpleEntry < Integer , Entry< Register, Register > > 
					(0,new SimpleEntry< Register,Register >((Register) SymbolTable.getReserved("bx"), (Register) SymbolTable.getReserved("si"))));		
		rmSuit.push(new SimpleEntry < Integer , Entry< Register, Register > >
					(1, new SimpleEntry< Register,Register >((Register) SymbolTable.getReserved("bx"), (Register) SymbolTable.getReserved("di"))));
		rmSuit.push(new SimpleEntry < Integer , Entry< Register, Register > > 
					(2, new SimpleEntry< Register,Register >((Register) SymbolTable.getReserved("bp"), (Register) SymbolTable.getReserved("si"))));
		rmSuit.push(new SimpleEntry < Integer , Entry< Register, Register > > 
					(3, new SimpleEntry< Register,Register >((Register) SymbolTable.getReserved("bp"), (Register) SymbolTable.getReserved("di"))));		
		
	// 32
		
		Stack < Register > regs32 = new Stack < Register > ( );
		regs32.push((Register) SymbolTable.getReserved("eax"));
		regs32.push((Register) SymbolTable.getReserved("ebx"));
		regs32.push((Register) SymbolTable.getReserved("ecx"));
		regs32.push((Register) SymbolTable.getReserved("edx"));
		regs32.push((Register) SymbolTable.getReserved("ebp"));
		regs32.push((Register) SymbolTable.getReserved("esp"));
		regs32.push((Register) SymbolTable.getReserved("esi"));
		regs32.push((Register) SymbolTable.getReserved("edi"));

		for (Register base : regs32) 
			for (Register index : regs32)  {
				if ( index.getName() == "esp") continue;
				rmSuit.push(new SimpleEntry < Integer , Entry< Register, Register > > (4,new SimpleEntry< Register,Register >(base,index)));
			}
	}
	
	public static boolean matches(Register base,Register indx) {
		
		for (  Entry < Integer,Entry <Register,Register> > Case : rmSuit )
			if (base.equals(Case.getValue().getKey()) && indx.equals(Case.getValue().getValue()) ) 
				return true;
		return false;
	}
	
	public static int getRm(Register base,Register indx) {
		for ( Entry < Integer,Entry <Register,Register> > Case : rmSuit ) 
			if ( base.equals(Case.getValue().getKey()) && 
				 indx.equals(Case.getValue().getValue()) )
				return Case.getKey();
		return -1;
	}
	
}