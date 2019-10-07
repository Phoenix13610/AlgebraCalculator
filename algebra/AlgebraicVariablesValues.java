package algebra;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import algebra.vars.VarValue;
import algebra.vars.specialVars.customFunction.Func_Custom;
import algebra.vars.specialVars.customFunction.FunctionList;

/**
 * A object to hold all the values of variables and functions
 * 
 * @author AntoineChevalier
 * @since 1.3.0.1
 */
public final class AlgebraicVariablesValues {
	public final List<VarValue> varValues;
	public final FunctionList functionList;

	public AlgebraicVariablesValues(FunctionList functionList) {
		this(new ArrayList<VarValue>(), functionList);
	}

	public AlgebraicVariablesValues() {
		this(new FunctionList());
	}

	public AlgebraicVariablesValues(List<VarValue> values, FunctionList functions) {
		varValues = values;
		this.functionList = functions;
	}

	public static AlgebraicVariablesValues parse(String[] values, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		List<VarValue> varValue = new ArrayList<VarValue>();
		List<Func_Custom> funcs = new ArrayList<Func_Custom>();
		for (String s : values) {
			s = s.trim();
			if (Pattern.matches("^\\S+\\(\\S*\\)=.*$", s)) {
				funcs.add(Func_Custom.parse(s, reference));
			} else if (Pattern.matches("^\\S*=.*$", s)) {
				
				varValue.add(VarValue.parse(s, reference));
			}

		}
		return new AlgebraicVariablesValues(varValue, new FunctionList(funcs));
	}

	public String toString() {
		String result = "";
		for (VarValue varValue : varValues) {
			result += varValue.toString();
			result += "\n";
		}
		result += functionList.toString();
		return result;
	}

	public boolean equals(Object obj) {
		try {
			AlgebraicVariablesValues aVV=(AlgebraicVariablesValues)obj;
			return aVV.varValues.equals(varValues)&&aVV.functionList.equals(functionList);
		}catch (ClassCastException e) {
			return false;
		}
	}
	
	public void add(AlgebraicVariablesValues otherValues) {
		varValues.addAll(otherValues.varValues);
		functionList.functions.addAll(otherValues.functionList.functions);
	}
}
