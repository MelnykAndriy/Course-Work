package translator.table.initializers;

import java.util.TreeMap;

import translator.table.CommandSuit;
import translator.table.OperandOption;
import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.reserved.Command;
import translator.table.OperandKind;

public class CommandInitializer  {

	public static void initialize(TreeMap<String, Atom> table ) {
        CommandSuit addedCommandSuit = new CommandSuit();
      
    // MOV
        addedCommandSuit.addOption(new OperandOption("88","/r",OperandKind.m8,OperandKind.r8));
        addedCommandSuit.addOption(new OperandOption("89","/r",OperandKind.m16,OperandKind.r16));
        addedCommandSuit.addOption(new OperandOption("89","/r",OperandKind.m32,OperandKind.r32));
        table.put("mov",new Command("mov",2,addedCommandSuit));

    // STI
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("FB"));
        table.put("sti",new Command("sti",0,addedCommandSuit));
    
        
    // DIV
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("F6","/6",OperandKind.m8));
        addedCommandSuit.addOption(new OperandOption("F7","/6",OperandKind.m16));
        addedCommandSuit.addOption(new OperandOption("F7","/6",OperandKind.m32));
        table.put("div", new Command("div",1,addedCommandSuit));
        
    // MUL
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("F6","/4",OperandKind.m8));
        addedCommandSuit.addOption(new OperandOption("F7","/4",OperandKind.m16));
        addedCommandSuit.addOption(new OperandOption("F7","/4",OperandKind.m32));
        table.put("mul",new Command("mul",1,addedCommandSuit));
        
    // ADC
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("12","/r",OperandKind.r8,OperandKind.m8 ) );
        addedCommandSuit.addOption(new OperandOption("13","/r",OperandKind.r16,OperandKind.m16 ) );
        addedCommandSuit.addOption(new OperandOption("13","/r",OperandKind.r32,OperandKind.m32 ) );
        table.put("adc",new Command("adc",2,addedCommandSuit));
        
    // AND
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("22","/r",OperandKind.r8,OperandKind.m8 ) );
        addedCommandSuit.addOption(new OperandOption("23","/r",OperandKind.r16,OperandKind.m16 ) );
        addedCommandSuit.addOption(new OperandOption("23","/r",OperandKind.r32,OperandKind.m32 ) );
        table.put("and",new Command("and",2,addedCommandSuit));
        
    // TEST
        addedCommandSuit = new CommandSuit();
        addedCommandSuit.addOption(new OperandOption("84","/r",OperandKind.m8,OperandKind.r8 ) );
        addedCommandSuit.addOption(new OperandOption("85","/r",OperandKind.m16,OperandKind.r16 ) );
        addedCommandSuit.addOption(new OperandOption("85","/r",OperandKind.m32,OperandKind.r32 ) );
        table.put("test",new Command("test",2,addedCommandSuit));
        
    // OR
        addedCommandSuit = new CommandSuit();
        // TODO
        addedCommandSuit.addOption(new OperandOption("0C","/r",OperandKind.r8,"AL",OperandKind.imm8 ) );
        addedCommandSuit.addOption(new OperandOption("0D","/r",OperandKind.r16,"AX",OperandKind.imm16 ) );
        addedCommandSuit.addOption(new OperandOption("0D","/r",OperandKind.r32,"EAX",OperandKind.imm32 ) );
        addedCommandSuit.addOption(new OperandOption("80","/1",OperandKind.r8,OperandKind.imm8 ) );
        addedCommandSuit.addOption(new OperandOption("81","/1",OperandKind.r16,OperandKind.imm16 ) );
        addedCommandSuit.addOption(new OperandOption("81","/1",OperandKind.r32,OperandKind.imm32 ) );
        addedCommandSuit.addOption(new OperandOption("83","/1",OperandKind.r16,OperandKind.imm8 ));
        addedCommandSuit.addOption(new OperandOption("83","/1",OperandKind.r32,OperandKind.imm8 ));
        table.put("or",new Command("or",2,addedCommandSuit));
        
//    // JMP
//        addedCommandSuit = new CommandSuit();
//        table.put("jmp",new Command("jmp",1,addedCommandSuit));
//        
//    // JAE
//        addedCommandSuit = new CommandSuit();
//        table.put("jae",new Command("jae",1,addedCommandSuit));
		
	}

}
