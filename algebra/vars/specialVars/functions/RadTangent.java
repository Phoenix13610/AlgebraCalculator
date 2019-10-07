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
 * A varaible object that represents a tangent object in degrees
 * 
 * @since 1.2
 * @author AntoineChevalier
 *
 */
public class RadTangent extends Var_Function {

	public RadTangent(Var_Base number) {
		super("tan", number);
	}

	public RadTangent() {
		this(new Var_Variable(0));
	}

	public Var_Variable simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Base number = this.number.simplify();
		if (Op_Operation.isVar_VariableSubClass(number) && ((Var_Variable) number).undefVars.isEmpty()) {
			return new Var_Variable(Math.tan(((Var_Variable) number).mult));
		} else {
			throw new AlgebraicSimplifyingException("The number must be a number without any undefined variables");
		}
	}

	public RadTangent parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		if (name.contains("tan")) {
			if (name.contains(this.name + "(")) {
				return new RadTangent(reference.generateVarComponent(getBetweenParenthesis(name), 0).getVar());
			}
			name = name.substring(3);
			return new RadTangent((Var_Variable) reference.generateVarComponent(name, 0).getVar());
		} else {
			throw new AlgebraicParsingException(
					"The entered name is not the corresponding name for the variable : " + this.name + " -> " + name);
		}
	}

	@Override
	public RadTangent copy() {
		return new RadTangent(number.copy());
	}

	@Override
	public RadTangent evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		return new RadTangent(number.evaluate(name, newValue));
	}

	public boolean equals(Object obj) {
		try {
			RadTangent n = (RadTangent) obj;
			return n.number.equals(number) && n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

}
