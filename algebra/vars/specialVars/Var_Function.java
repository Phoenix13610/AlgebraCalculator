package algebra.vars.specialVars;

import algebra.AlgebraicParsingException;
import algebra.ComponentReference;
import algebra.Var_Base;
import algebra.vars.Var_Special;

/**
 * A variable consisting of a name and a variable
 * 
 * @since 1.3
 * @author AntoineChevalier
 */
public abstract class Var_Function extends Var_Special {
	public final Var_Base number;

	/**
	 * A basic constructor
	 * 
	 * @param name
	 * @param number
	 */
	public Var_Function(String name, Var_Base number) {
		super(name);
		this.number = number;
	}

	/**
	 * A method to get the regex
	 */
	@Override
	public String getRegex(ComponentReference reference) {
		return "(^" + name + "\\S*$)|(^" + name + "\\(\\S*\\))$";
	}

	public String toString() {
		return name + "(" + number + ")";
	}

	public static String getBetweenParenthesis(String str) throws AlgebraicParsingException {
		String result="";
		int parenthesis=0;
		for (char c : str.toCharArray()) {
			if(c==')') {
				parenthesis--;
			}
			if(parenthesis>=1) {
				result+=c;
			}
			if(c=='(') {
				parenthesis++;
			}
		}
		if(parenthesis!=0) {
			throw new AlgebraicParsingException("There is no number inputed between the parenthesis");
		}
		return result;
	}
}
