
package translator.table.csvtocomponents;
//import translator.table.tablecomponents.*;


public class CsvParser {
    public static void main(String[] args) {
        DirectiveParser DP = new DirectiveParser("testFile3");
        RegisterParser RP = new RegisterParser("testFile2");
        CommandParser CP = new CommandParser("testFile1");
        // switch (instructions)
        
        try {	
	        DP.join();
	        RP.join();
	        CP.join();
        } catch (InterruptedException exc) {
        	System.err.println( exc );
        }
    }
    
    // static private void 
}

class DirectiveParser extends Parser {
    DirectiveParser(String CsvFile) {
        super(CsvFile);
    }
    
    protected void Parse() {
        System.out.println("File : " + parseFile);
        System.out.println("Parsing started " + this);
        System.out.println(Thread.currentThread());
    }
}

class RegisterParser extends Parser {    
    RegisterParser(String CsvFile) {
        super(CsvFile);
    }
    
    protected void Parse() {
        System.out.println("File : " + parseFile);
        System.out.println("Parsing started " + this);
        System.out.println(Thread.currentThread());
    }
}

class CommandParser extends Parser { 
    CommandParser(String CsvFile) {
        super(CsvFile);
    }
    
    protected void Parse() {
        System.out.println("File : " + parseFile);
        System.out.println("Parsing started " + this);
        System.out.println(Thread.currentThread());
    }
}

abstract class Parser implements Runnable {
    protected Thread t;
    protected String parseFile;
    
    Parser(String CsvFile) {
        parseFile = CsvFile;
        t = new Thread(this,this.getClass().getName());
        t.start();
    }
    
    public void join() throws InterruptedException {
    	t.join();
    }
    
    public void run() {
        // try {
            Parse();
        // } catch (InterruptedException exc) {
    //        System.out.println("Parsing in " + this.getClass().getName() + "interrupted.");
        // } finally {
            // // file closing
        // }
    }
        
    protected abstract void Parse();    
}