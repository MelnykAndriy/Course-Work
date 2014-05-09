package translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class Printable {
	
	public abstract void genOutput(PrintWriter writer);
	
	public void genOutput(OutputStream stream) {
		PrintWriter printer = new PrintWriter(stream);
		try {
			genOutput( printer );
		} finally {
			printer.close();
		}
	}
	
	public void genOutput(String fname) throws FileNotFoundException { 
		PrintWriter printer = new PrintWriter(fname); 
		try {
			genOutput( printer );
		} finally {
			printer.close();
		}
	}
	
	public void genOutput(File file) throws FileNotFoundException { 
		PrintWriter printer = new PrintWriter(file);
		try {
			genOutput( printer );
		} finally {
			printer.close();
		}
	}
}
