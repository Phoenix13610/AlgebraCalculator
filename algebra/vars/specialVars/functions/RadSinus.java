package algebra.vars.specialVars.functions;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;
import algebra.vars.specialVars.Var_Function;

/**
 * A varaible object that represents a sinus object
 * 
 * @since 1.2
 * @author AntoineChevalier
 *
 */
public class RadSinus extends Var_Function {

	public RadSinus(Var_Base number) {
		super("sin", number);
	}

	public RadSinus() {
		this(new Var_Variable(0));
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if (Op_Operation.isVar_VariableSubClass(number) && ((Var_Variable) number).undefVars.isEmpty()) {
			return new Var_Variable(Math.sin(((Var_Variable) number).mult));
		} else {
			throw new AlgebraicSimplifyingException("The number must be a number without any undefined variables");
		}
	}

	public RadSinus parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("sin")) {
			if (name.contains(this.name + "(")) {
				return new RadSinus(reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar());
			}
			name = name.substring(3);
			return new RadSinus(reference.generateVarComponent(name, 0).getVar());
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	@Override
	public RadSinus copy() {
		return new RadSinus(number.copy());
	}

	@Override
	public RadSinus evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return new RadSinus(newValue.evaluate(name, newValue));
	}

	public boolean equals(Object obj) {
		try {
			RadSinus n = (RadSinus) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

}
