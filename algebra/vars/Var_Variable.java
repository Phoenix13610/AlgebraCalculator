package algebra.vars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.MathAC;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.operations.Op_Multiplication;
import algebra.operations.Op_Power;

/**
 * A object representing a variable with a number representing the multiplicator and a list of undefined variables like "x"
 * @author AntoineChevalier
 * @since 1.0
 */
public class Var_Variable implements Var_Base {
	public final List<UndefinedVar> undefVars;
	public final double mult;
/**
 * A simple constructor for parsing reference
 */
	public Var_Variable() {
		this(0);
	}
/**
 * A basic constructor 
 * @param mult
 */
	public Var_Variable(double mult) {
		this(mult, new ArrayList<UndefinedVar>());
	}

	public Var_Variable(double mult, List<UndefinedVar> undefVars) {
		this.mult = mult;
		this.undefVars = undefVars;
		Collections.unmodifiableList(undefVars);
	}
/**
 * @see Var_Base#simplify()
 */
	@Override
	public Var_Base simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base mult = new Var_Variable(1);
		Op_Multiplication multiplier = new Op_Multiplication();
		List<UndefinedVar> nUndefVars = copy().undefVars;
		for (UndefinedVar undefinedVar : undefVars) {
			if (!undefinedVar.getClass().equals(UndefinedVar.class)) {
				nUndefVars.remove(undefinedVar);
				mult = multiplier.use(mult, undefinedVar.simplify());
			}
		}
		if (mult.equals(new Var_Variable(1))) {
			return this;
		} else {
			return multiplier.use(new Var_Variable(this.mult, nUndefVars), mult);
		}
	}

	@Override
	public String toString() {
		String result = Double.toString(mult);
		for (UndefinedVar var_UndefinedVar : undefVars) {
			result += var_UndefinedVar.toString();
		}
		return result;
	}
/**
 * @throws AlgebraicRuntimeException 
 * @see Var_Base#parseVar(String, ComponentReference)
 */
	public Var_Variable parseVar(String var, ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException {
		boolean negative = false;
		for (char c : var.toCharArray()) {
			if (c == '-') {
				negative = !negative;
			} else {
				var = var.substring(var.indexOf(c));
				break;
			}
		}
		String mult = "";
		if (negative) {
			mult = "-";
		}
		List<UndefinedVar> vars = new ArrayList<UndefinedVar>();
		for (char c : var.toCharArray()) {
			if (MathAC.isNum(c) || c == '.') {
				mult += c;
			} else {
				vars = parseUndefVars(var.substring(var.indexOf(c)), reference);
				break;
			}
		}
		if (mult.equals("")) {
			mult = "1";
		} else if (mult.equals("-")) {
			mult = "-1";
		}
		return new Var_Variable(Double.parseDouble(mult), vars);
	}

	protected List<UndefinedVar> parseUndefVars(String vars, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		List<UndefinedVar> result = new ArrayList<UndefinedVar>();
		String varName = "";
		String expo = "";
		for (char c : vars.toCharArray()) {
			if (varName.equals("")) {
				varName += c;
			} else {
				if (MathAC.isNum(c) || c == '.' || c == '-') {
					expo += c;
				} else {
					if (expo.equals("")) {
						expo = "1";
					}
					result.add(new UndefinedVar(varName, reference.generateVarComponent(expo, 0).getVar()));
					varName = "";
					expo = "";
					varName += c;
				}
			}
		}
		if (expo.equals("")) {
			expo = "1";
		}
		result.add(new UndefinedVar(varName, reference.generateVarComponent(expo, 0).getVar()));
		return result;
	}

	public static String getREGEX() {
		return "(\\d*\\.?\\d*)(([^\\s\\d])*(\\d*\\.?\\d*))*";
	}
/**
 * @see Var_Base#getRegex(ComponentReference)
 */
	@Override
	public String getRegex(ComponentReference reference) {
		return "^"+getREGEX(reference)+"$";
	}

	public static String getREGEX(ComponentReference reference) {
		return "((-?\\d*)|(-?\\d\\.\\d*))\\s*(([^\\d," + reference.generateBlankOpsAndParenthesisRegex()
				+ "])*\\s*((\\d*)|(-?\\d\\.\\d*)))*";
	}
/**
 * @see Var_Base#copy()
 */
	@Override
	public Var_Variable copy(){
		List<UndefinedVar> undef = new ArrayList<UndefinedVar>();
		for (UndefinedVar undefinedVar : undefVars) {
			undef.add(undefinedVar.copy());
		}
		return new Var_Variable(mult, undef);
	}

	public boolean equals(Object obj) {
		try {
			Var_Variable var = (Var_Variable) obj;
			if (var.mult == mult && undefVars.equals(var.undefVars)) {
				return true;
			}
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}
/**
 * Checks if the undefined variable list contains the name of the var
 * @param varName the name of the var
 * @return
 */
	public boolean containsVarName(String varName) {
		boolean result = false;
		if (undefVars.isEmpty()) {
			return false;
		}
		for (UndefinedVar v : undefVars) {
			if (v.name.equals(varName)) {
				result = true;
			}
		}
		return result;
	}
/**
 * @see Var_Base#evaluate(String, Var_Base)
 */
	@Override
	public Var_Base evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		Var_Base multiplicator = new Var_Variable(1);
		List<UndefinedVar> nUndefVars = new ArrayList<UndefinedVar>();
		for (UndefinedVar undefinedVar : undefVars) {
			if (undefinedVar.name.equals(name)) {
				multiplicator = new Op_Multiplication().use(multiplicator,
						new Op_Power().use(newValue, undefinedVar.expo));
			} else {
				nUndefVars.add(undefinedVar);
			}
		}
		return new Op_Multiplication().use(new Var_Variable(mult, nUndefVars), multiplicator);
	}

	public Var_Variable simplifyVariable() {
		List<UndefinedVar>list=copy().undefVars;
		for (UndefinedVar undefinedVar : undefVars) {
			if(undefinedVar.expo.equals(new Var_Variable(0))) {
				list.remove(undefinedVar);
			}
		}
		return new Var_Variable(mult, list);
	}
	@Override
	public double getDegree() throws AlgebraicRuntimeException {
		double degree=0;
		for (UndefinedVar undefinedVar : undefVars) {
			if(Op_Operation.isVar_VariableSubClass(undefinedVar.expo)) {
				degree+=((Var_Variable)undefinedVar.expo).mult;
			}else {
				throw new AlgebraicRuntimeException("Cannot find the degree of a variable with a exponent other than a variable");
			}
		}
		return degree;
	}
}
