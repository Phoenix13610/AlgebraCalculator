package algebra.vars.specialVars.customFunction;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;
import algebra.vars.Var_Variable;
import algebra.vars.specialVars.Var_Function;

/**
 * A variable object that represents a fuction in a algebraic equation
 * 
 * @since 1.3
 * @author AntoineChevalier
 *
 */
public class Func_Reference extends Var_Function {
	public final FunctionList functions;

	public Func_Reference() {
		super("", new Var_Variable(0));
		functions = null;
	}

	public Func_Reference(String name, Var_Base number, FunctionList functions) {
		super(name, number);
		this.functions = functions;
	}

	@Override
	public Var_Base simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		for (Func_Custom fC : functions.getFunctions()) {
			if (name.equals(fC.name)) {
				return fC.evaluate(fC.varToReplace(), number.simplify()).simplify();
			}
		}
		throw new AlgebraicSimplifyingException("The function has not been entered : " + name);
	}

	@Override
	public Func_Reference copy() {
		return new Func_Reference(name, number.copy(), functions);
	}

	@Override
	public Var_Base evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		for (Func_Custom fC : functions.getFunctions()) {
			if (this.name.equals(fC.name)) {
				return fC.evaluate(name, newValue);
			}
		}
		throw new AlgebraicSimplifyingException("The function has not been entered : " + this.name);
	}

	@Override
	public Func_Reference parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		String tName = "";
		for (char c : name.toCharArray()) {
			if (c != '(') {
				tName += c;
			} else {
				break;
			}
		}
		return new Func_Reference(tName, reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar(),
				reference.varValues.functionList);
	}

	@Override
	public String getRegex(ComponentReference reference) {
		return getREGEX(reference);
	}

	public static String getREGEX(ComponentReference reference) {
		return "^[^" + reference.generateBlankOpsAndParenthesisRegex() + "]+\\(\\S*\\)$";
	}
}
