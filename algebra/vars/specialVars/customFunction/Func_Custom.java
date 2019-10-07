package algebra.vars.specialVars.customFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algebra.AlgebraicExpression;
import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;
import algebra.vars.Var_Sequence;

/**
 * Reprents a custom made function
 * 
 * @since 1.3
 * @author AntoineChevalier
 *
 */
public class Func_Custom {
	public final String name;
	private String varToReplace = "x";
	public final Var_Base equation;

	public Func_Custom(String name, Var_Base equation) {
		this.name = name;
		this.equation = equation;
	}

	public Func_Custom(String name, AlgebraicExpression equation) {
		this.name = name;
		this.equation = new Var_Sequence(equation.vars, equation.ops) {
			@Override
			public char openParenthesis() {
				return 0;
			}

			@Override
			public char closingParenthesis() {
				return 0;
			}
		};
	}

	public Func_Custom(String name, String varToReplace, Var_Base equation) {
		this(name, equation);
		this.varToReplace = varToReplace;
	}

	public Func_Custom(String name, String varToReplace, AlgebraicExpression equation) {
		this(name, equation);
		this.varToReplace = varToReplace;
	}

	public Func_Custom copy() {
		return new Func_Custom(name, equation.copy());
	}

	public String varToReplace() {
		return varToReplace;
	}

	public Var_Base evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return equation.evaluate(name, newValue);
	}

	public static Func_Custom parse(String function, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		function = function.trim();
		if (function.contains("=")) {
			String tName = "";
			String varToReplace = "";
			String name = function.split("=")[0];
			Var_Base equation = reference.generateVarComponent(function.split("=")[1], 0).getVar();
			Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(name);
			while (m.find()) {
				varToReplace = m.group(1);
			}
			tName = name.substring(0, name.indexOf('('));
			return new Func_Custom(tName, varToReplace, equation);
		} else {
			throw new AlgebraicParsingException("The function is incomplete");
		}
	}
	
	public String toString() {
		return name+"("+varToReplace+")="+equation;
	}
}
