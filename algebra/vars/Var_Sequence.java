package algebra.vars;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Component;
import algebra.Var_Base;
import algebra.Var_Component;

/**
 * A object representing a algebraic expression containned in a variable object
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public abstract class Var_Sequence extends AlgebraicExpression implements Var_Base {
	/**
	 * a basic constructor for parsing reference
	 */
	public Var_Sequence() {
		super(new ArrayList<Var_Component>(), new ArrayList<Op_Component>());
	}

	public Var_Sequence(List<Var_Component> vars, List<Op_Component> ops) {
		super(vars, ops);
	}

	public Var_Sequence(Var_Base base) {
		super(base);
	}

	/**
	 * @throws AlgebraicRuntimeException
	 * @see Var_Base#parseVar(String, ComponentReference)
	 */
	public Var_Sequence parseVar(String sequence, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		AlgebraicExpression temp = AlgebraicExpression.parse(sequence, reference);
		return new Var_Sequence(temp.vars, temp.ops) {
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

	/**
	 * A method to get the regex
	 */
	public String getRegex(ComponentReference reference) {
		return getREGEX();
	}

	public static String getREGEX() {
		return "^.+\\s*([" + reference.generateOpsRegex() + "]\\s*.+\\s*)*$";
	}

	/**
	 * @see Var_Base#simplify()
	 */
	public Var_Sequence simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		AlgebraicExpression temp = super.simplify();
		return new Var_Sequence(temp.vars, temp.ops) {
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

	/**
	 * The opening character of the sequence
	 * 
	 * @return the character
	 */
	public abstract char openParenthesis();

	/**
	 * The closing character of the sequence
	 * 
	 * @return the character
	 */
	public abstract char closingParenthesis();

	/**
	 * @see Var_Base#copy()
	 */
	public Var_Sequence copy() {
		List<Var_Component> newVars = new ArrayList<Var_Component>();
		for (Var_Component var : vars) {
			newVars.add(new Var_Component(var.getVar().copy(), var.index()));
		}
		return new Var_Sequence(newVars, ops) {
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

	public boolean equals(Object obj) {
		try {
			Var_Sequence seq = (Var_Sequence) obj;
			if (vars.equals(seq.vars) && ops.equals(seq.ops)) {
				return true;
			}
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * @see Var_Base#evaluate(String, Var_Base)
	 */
	@Override
	public Var_Sequence evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		AlgebraicExpression temp = super.evaluate(name, newValue);
		return new Var_Sequence(temp.vars, temp.ops) {
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

	public double getDegree() throws AlgebraicRuntimeException {
		double degree = 0;
		for (Var_Component var : vars) {
			if (degree < var.getVar().getDegree()) {
				degree = var.getVar().getDegree();
			}
		}
		return degree;
	}

	public Var_Sequence sort() throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		AlgebraicExpression temp = super.sort();
		return new Var_Sequence(temp.vars, temp.ops) {
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
}
