package algebra.vars.sequence;

import java.util.ArrayList;
import java.util.List;

import algebra.AlgebraicExpression;
import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Op_Component;
import algebra.Var_Component;
import algebra.vars.Var_Sequence;

/**
 * A sequence for a simple expression inside parenthesis
 * 
 * @author AntoineChevalier
 * @since 1.0
 */
public class Seq_Parenthesis extends Var_Sequence {
	/**
	 * A basic constructor for parsing reference
	 */
	public Seq_Parenthesis() {
		super();
	}

	/**
	 * A basic constructor
	 * 
	 * @param vars
	 * @param ops
	 */
	public Seq_Parenthesis(List<Var_Component> vars, List<Op_Component> ops) {
		super(vars, ops);
	}

	/**
	 * @see Var_Sequence#simplify()
	 */
	@Override
	public Seq_Parenthesis simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		Var_Sequence temp = super.simplify();
		return new Seq_Parenthesis(temp.vars, temp.ops);
	}

	@Override
	public char openParenthesis() {
		return '(';
	}

	@Override
	public char closingParenthesis() {
		return ')';
	}

	public String getRegex(ComponentReference reference) {
		return getREGEX();
	}

	public static String getREGEX() {
		return "^\\(\\S*\\)$";
	}

	public String toString() {
		return "(" + super.toString() + ")";
	}

	public Seq_Parenthesis parseVar(String sequence, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException {
		AlgebraicExpression temp = AlgebraicExpression.parse(sequence.substring(1, sequence.length() - 1));
		return new Seq_Parenthesis(temp.vars, temp.ops);
	}

	public Seq_Parenthesis copy() {
		List<Var_Component> newVars = new ArrayList<Var_Component>();
		for (Var_Component var : vars) {
			newVars.add(new Var_Component(var.getVar().copy(), var.index()));
		}
		return new Seq_Parenthesis(newVars, ops);
	}

}
