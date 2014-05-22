package translator.table.tablecomponents.reserved;

import translator.table.CommandSuit;

public class RelativeCmd extends Command {
	private int opcodeMaxBytes;
	
	public RelativeCmd(String _name, int operandNumb,int opcodeMaxBytes, CommandSuit suit) {
		super(_name, operandNumb, suit);
		this.opcodeMaxBytes = opcodeMaxBytes;
	}
	
	public int getOpcodeMaxBytes() {	
		return opcodeMaxBytes;
	}

}
