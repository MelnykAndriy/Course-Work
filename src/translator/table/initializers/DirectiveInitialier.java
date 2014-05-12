package translator.table.initializers;

import java.util.StringTokenizer;
import java.util.TreeMap;

import translator.table.tablecomponents.Atom;
import translator.table.tablecomponents.Directive;

public class DirectiveInitialier {

	public static void initialize(TreeMap<String, Atom> table) {
        StringTokenizer directives = new StringTokenizer("end;segment;ends;db;dw;dd",";");
        while ( directives.hasMoreTokens() ) {
        	String directive = directives.nextToken();
        	table.put(directive,new Directive(directive) );
        }
	}

}
