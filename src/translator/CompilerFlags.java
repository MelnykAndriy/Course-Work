/* 
 * File : CompilerFlags.java
 * Autor : Andriy Melnyk
 * Purpose : definition of class that contain compiler flags 
 * created : 10.03.14
 */


package translator;

public class CompilerFlags {
	enum ArgType { iFile,oFile,flagExpect}
	
	private boolean prnLexer = false;
	private boolean prnTable = false;
	private boolean prnSyntaxer = false;
	private String iFile = null;
	private String oFile = null;
	private final static String HelpMsg = "Follow keys can be used: \n" +
								  "\t-h 		 	Print this help messege.\n" +
								  "\t-f [file]		File which will be compiled.\n" +
								  "\t-o [name] 		File which will be created as result of compiling.\n" + 
								  "\t-prnlexer		Print lexer result.\n" +
								  "\t-prnsyn 	 	Print syntaxer result.\n" +		
								  "\t-prntable		Print symbol table.\n" +
								  "Example : java Translator -f source.asm -o listing.lst\n";
	
	public void ParseArgs(String[] args) {
		PrnIfAndExit(args.length == 0,"Arguments list is empty.");
		try {
			ArgType curParseArg = ArgType.flagExpect;
			
			for (String arg : args)
				switch (arg) {
	        		case "-h":
	        			PrnIfAndExit(true,HelpMsg);
	        		case "-f":
	        			PrnIfAndExit(curParseArg != ArgType.flagExpect,"Missing operand.");
	        			if ( iFile != null ) throw new ParameterDuplicate("-f");
	        			curParseArg = ArgType.iFile;
	        			break;
	        		case "-o":
	        			PrnIfAndExit(curParseArg != ArgType.flagExpect,"Missing operand.");
	        			if ( oFile != null ) throw new ParameterDuplicate("-o");
	        			curParseArg = ArgType.oFile;
	        			break;
	        		case "-prnlexer": 
	        			PrnIfAndExit(curParseArg != ArgType.flagExpect,"Missing operand.");
	        			if (prnLexer) throw new ParameterDuplicate("-prnlexer");
	        			prnLexer = true;
	        			curParseArg = ArgType.flagExpect;
	        			break;
	        		case "-prntable":
	        			PrnIfAndExit(curParseArg != ArgType.flagExpect,"Missing operand.");
	        			if (prnTable) throw new ParameterDuplicate("-prntable");
	        			prnTable = true;
	        			curParseArg = ArgType.flagExpect;
	        			break;
	        		case "-prnsyn":
	        			PrnIfAndExit(curParseArg != ArgType.flagExpect,"Missing operand.");
	        			if (prnSyntaxer) throw new ParameterDuplicate("-prntable");
	        			prnSyntaxer = true;
	        			curParseArg = ArgType.flagExpect;
	        			break;
	        		default :
	        			switch(curParseArg) {
	        				case flagExpect:
	        					PrnIfAndExit(true,"Unexpected flag: " + arg +  "\nPlease, read help.");
	        				case iFile: 
	        					PrnIfAndExit(iFile != null,"Flag -f expect only one parameter.");
	        					iFile = new String(arg);
	        					curParseArg = ArgType.flagExpect;
	        					break;
	        				case oFile:
	        					PrnIfAndExit(oFile != null,"Flag -o expect only one parameter.");
	        					oFile = new String(arg);
	        					curParseArg = ArgType.flagExpect;
	        			}
	        	}
		} catch (ParameterDuplicate exc) {
			PrnIfAndExit(true,"Parameter '" + exc.getpName() + "' is specified two or more times.");
		}
        PrnIfAndExit(iFile == null,"Value of key '-f' is obligatory.");
        if (oFile == null) {
            if ( iFile.endsWith(".asm") ) 
    			oFile = iFile.substring(0,iFile.lastIndexOf('.')); 
    		else 
    			oFile = iFile;
        }
        oFile = oFile + ".lst";
	}
	
	private void PrnIfAndExit(boolean flag,String msg) {
        if ( flag ) {
        	System.err.println(msg); 
			System.exit(1);
        }
	}

	public boolean isPrnLexer() {
		return prnLexer;
	}

	public boolean isPrnTable() {
		return prnTable;
	}

	public boolean isPrnSyntaxer() {
		return prnSyntaxer;
	}
	
	public String getIFile() {
		return iFile;
	}

	public String getOFile() {
		return oFile;
	}

}

class ParameterDuplicate extends Exception {
	private static final long serialVersionUID = 6939194010252412835L;
	
	private String pName;
	
	ParameterDuplicate(String pName) {
		super();
		this.pName = new String(pName);
	}
	
	public String getpName() {
		return pName;
	} 
}
