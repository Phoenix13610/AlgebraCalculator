package algebra.vars.specialVars.functions;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Operation;
import algebra.Var_Base;
import algebra.vars.Var_Variable;

/**
 * A varaible object that represents a sinus object in degrees
 * 
 * @since 1.2
 * @author AntoineChevalier
 *
 */
public class DegSinus extends RadSinus {

	public DegSinus(Var_Base number) {
		super(number);
	}

	public DegSinus() {
		super();
	}

	public DegSinus parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("sin")) {
			if (name.contains(this.name + "(")) {
				return new DegSinus(reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar());
			}
			name = name.substring(3);
			return new DegSinus(reference.generateVarComponent(name, 0).getVar());
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if (Op_Operation.isVar_VariableSubClass(number) && ((Var_Variable) number).undefVars.isEmpty()) {
			return new Var_Variable(Math.sin(Math.toRadians(((Var_Variable) number).mult)));
		} else {
			throw new AlgebraicSimplifyingException("The number must be a number without any undefined variables");
		}

	}

	public DegSinus copy() {
		return new DegSinus(number.copy());
	}

	public boolean equals(Object obj) {
		try {
			DegSinus n = (DegSinus) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

}
