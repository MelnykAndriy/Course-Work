
package translator.table.tablecomponents;

import java.util.ArrayList;

public class Segment extends Identifier {
	public enum SegmentType { codeSeg, dataSeg }; 
	private SegmentType SegType;	
	private int byteSize;
	private ArrayList < Variable > vars;
	private ArrayList < Label > labels;
		
	public Segment(String _name,SegmentType type) {
		super(_name);
		SegType = type;
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
		lab.setOffset(0); // TODO 
		labels.add(lab);
	}
	
	public ArrayList < Identifier > getDefSymbols() {
		ArrayList < Identifier > defSymbols = new ArrayList < Identifier >();
		defSymbols.addAll(vars);
		defSymbols.addAll(labels);
		return defSymbols;
	}
	
	public SegmentType getSegmentType() {
		return SegType;
	}

	public synchronized int getByteSize() {
		return byteSize;
	}
	
    public String toString() {
    	return "Segment";
    }
    
}
