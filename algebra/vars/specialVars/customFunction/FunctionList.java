package algebra.vars.specialVars.customFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * A object that lists all of the custom functions
 * 
 * @since 1.3
 * @author AntoineChevalier
 *
 */
public class FunctionList {
	public List<Func_Custom> functions = new ArrayList<Func_Custom>();

	public FunctionList(List<Func_Custom> list) {
		functions = list;
	}

	public FunctionList() {
		functions = new ArrayList<Func_Custom>();
	}

	public List<Func_Custom> getFunctions() {
		return functions;
	}

	public String toString() {
		String result="";
		for (Func_Custom func_Custom : functions) {
			result+=func_Custom.toString();
			result+="\n";
		}
		return result;
	}
	
	public boolean equals(Object obj) {
		try {
			FunctionList fl=(FunctionList)obj;
			return functions.equals(fl.functions);
		} catch (ClassCastException e) {
			return false;
		}
	}
}
