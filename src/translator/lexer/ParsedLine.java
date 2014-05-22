package translator.lexer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.AtomType;

public class ParsedLine {
	private int lineNumb;
	private ArrayList<Atom> atoms;
	private String fullLineWithoutComments;
	private String errorMsg = null;
	private int lineByteSize;
	
	public ParsedLine(int lineNumb, ArrayList<Atom> atoms ,String fullLineWithoutComments) {
		this.lineNumb = lineNumb;
		this.atoms = atoms;
		this.fullLineWithoutComments = fullLineWithoutComments;
	}
	
	public ParsedLine(ParsedLine line, ArrayList< Atom > atoms) {
		this.lineNumb = line.lineNumb;
		this.fullLineWithoutComments = line.fullLineWithoutComments;
		this.atoms = atoms;
	}
	
	public synchronized int getLineNumb() {
		return lineNumb;
	}
	
	public synchronized ArrayList<Atom> getAtoms() {
		return atoms;
	}

	public synchronized String getLine() {
		return fullLineWithoutComments;
	}
	
	public boolean isInvalid() {
		return errorMsg != null;
	}
	
	public void errFound(String msg) {
		errorMsg = msg;
	}
	
	public String getErr() {
		return errorMsg;
	}
	
	public String getIndexName(int indx) {
		return atoms.get(indx).getName();
	}
	
	public Atom getAtomAt(int indx) {
		return atoms.get(indx);
	}
	
	public ArrayList< Atom > subArray(int start,int end) {
		return new ArrayList< Atom >(atoms.subList(start, end));
	}
	
	public ArrayList< Atom > subArray(int start) {
		return new ArrayList< Atom >(atoms.subList(start, atoms.size()));
	}
	
	public int atomsSize() {
		return atoms.size();
	}
	
	public synchronized int firstIndexOf(AtomType tp) {
		for (int i = 0; i < atoms.size() ; i++ ) {
			if (atoms.get(i).getType() == tp) return  i;
		}
		return -1;
	}
	
	public int findPos(Atom atom) {
		return fullLineWithoutComments.toLowerCase().indexOf( atom.searchedPosAtom().getName().toLowerCase() ) + 1;
	}
	
	public int findPos(String name) { 
		return fullLineWithoutComments.toLowerCase().indexOf( name.toLowerCase() ) + 1;
	}
	
	public int findPos(String name,int numberInLine) {
		int i = 1;
		Matcher matcher = Pattern.compile("(" + name +")")
							     .matcher(fullLineWithoutComments);
		while ( matcher.find() ) {
			if (i == numberInLine) return matcher.start();
			i++;
		}
		return -1;
	}
	
	public int findPos(Atom atom,int numberInLine) { 
		return findPos(atom.searchedPosAtom().getName(),numberInLine);
	}
	
	public boolean strMatches(String regex) {
		return fullLineWithoutComments.matches(regex);
	}

	public synchronized boolean startsWith( ArrayList < AtomType > pattern) {
		if ( pattern.size() > atoms.size() ) return false;
		return this.checkRegion(0, pattern.size() ,pattern);
	}
	
	public synchronized boolean endsWith( ArrayList < AtomType > pattern) {
		return this.matches(atoms.size() - pattern.size(),pattern);
	}
	
	public synchronized boolean matches(ArrayList < AtomType > pattern) {
		return this.matches(0,pattern);
	}
	
	public synchronized boolean matches(int start, ArrayList < AtomType > pattern) {
		return this.matches(start, pattern.size() , pattern);
	}
	
	private boolean matches(int start,int end, ArrayList < AtomType > pattern ) {
		if ( end != atoms.size() - start ) return false; 
		return checkRegion(start, end, pattern);
	}
	
	private boolean checkRegion(int start,int end, ArrayList < AtomType > pattern) {
		for (int i = start; i < end ; i++ ) {
			if ( pattern.get(i) != atoms.get(i + start).getType() )	return false;		
		}
		return true;
	}
	
	public String toString() {
		return fullLineWithoutComments;
	}

	public int getLineByteSize() {
		return lineByteSize;
	}

	public void setLineByteSize(int lineByteSize) {
		this.lineByteSize = lineByteSize;
	}

	public int findPos(AtomType tp) {
		for (Atom atom : atoms) 
			if ( atom.getType() == tp)
				return fullLineWithoutComments.toLowerCase().indexOf( atom.searchedPosAtom().getName().toLowerCase() ) + 1 ;
		return -1;

	}


}
