package translator.table.tablecomponents.reserved;

import translator.table.CommandSuit;
import translator.table.OperandOption;

public class RelativeCmd extends Command {
	private int opcodeMaxBytes;
	
	public RelativeCmd(String _name, int operandNumb, CommandSuit suit) {
		super(_name, operandNumb, suit);
		int maxByteSize = 0;
		for ( OperandOption option : suit.getOptions() )
			if ( maxByteSize < option.opcodeByteSize() ) 
				maxByteSize = option.opcodeByteSize();
		this.opcodeMaxBytes = maxByteSize;
	}
	
	public int getOpcodeMaxBytes() {	
		return opcodeMaxBytes;
	}

}