
package translator.table.tablecomponents.userdefined;

import java.util.ArrayList;

import translator.table.tablecomponents.AtomType;

public class Segment extends Identifier {
	public enum SegmentType { bit16 , bit32 }; 
	private SegmentType segType;	
	private int byteSize;
	private ArrayList < Variable > vars;
	private ArrayList < Label > labels;
		
	public Segment(String _name,SegmentType type) {
		super(_name);
		segType = type;
		byteSize= 0;
		vars = new ArrayList < Variable > ();
		labels = new ArrayList < Label > ();
	}

	@Override
	public AtomType getType() {
		return AtomType.Segment;
	}
	
	public int byteSize() {
		return byteSize;
	}
		
	public void defVariable(Variable var) {
		vars.add(var);
		var.setOffset(byteSize);
		byteSize += var.Size();
	}
	
	public void defLabel(Label lab) {
		lab.setOffset(byteSize); 
		labels.add(lab);
	}
	
	public ArrayList < Identifier > getDefSymbols() {
		ArrayList < Identifier > defSymbols = new ArrayList < Identifier >();
		defSymbols.addAll(vars);
		defSymbols.addAll(labels);
		return defSymbols;
	}
	
	public SegmentType getSegmentType() {
		return segType;
	}

	public synchronized int getByteSize() {
		return byteSize;
	}
	
    public String toString() {
    	return "Segment";
    }

	@Override
	public String identTypeToString() {
		return (segType == SegmentType.bit32)?("32 bit"):("16 bit");
	}
    
    
}
