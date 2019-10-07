package algebra.vars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;

/**
 * A variable containing a list of other variables
 * @author AntoineChevalier
 * @since 1.0
 */
public class Var_Set implements Var_Base {
	public final List<Var_Base> set;

	/**
	 * A basic constructor for parsing reference
	 */
	public Var_Set() {
		set = new ArrayList<Var_Base>();
		Collections.unmodifiableList(set);
	}

	/**
	 * A basic constructor
	 * 
	 * @param set
	 */
	public Var_Set(List<Var_Base> set) {
		this.set = set;
		Collections.unmodifiableList(this.set);
	}

	/**
	 * @see Var_Base#simplify()
	 */
	@Override
	public Var_Set simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		List<Var_Base> temp = new ArrayList<Var_Base>();
		for (Var_Base var_Base : set) {
			temp.add(var_Base.simplify());
		}
		return new Var_Set(temp);
	}

	/**
	 * @throws AlgebraicRuntimeException 
	 * @see Var_Base#parseVar(String, ComponentReference)
	 */
	public Var_Base parseVar(String set, ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException {
		set = set.substring(1, set.length() - 1);
		String[] temp = set.split(",");
		List<Var_Base> newSet = new ArrayList<Var_Base>();
		for (String s : temp) {
			newSet.add(reference.generateVarComponent(s, 0).getVar());
		}
		return new Var_Set(newSet);
	}

	public static String getREGEX(ComponentReference reference) {
		return "(^\\{\\S+(,\\s*\\S+)*\\}$)";
	}

	/**
	 * @see Var_Base#getRegex(ComponentReference)
	 */
	@Override
	public String getRegex(ComponentReference reference) {
		return getREGEX(reference);
	}

	public String toString() {
		String result = "{";
		for (int i = 0; i < set.size(); i++) {
			result += set.get(i);
			if (i < set.size() - 1) {
				result += ',';
			}
		}
		result += '}';
		return result;
	}

	/**
	 * @see Var_Base#copy()
	 */
	@Override
	public Var_Base copy(){
		List<Var_Base> newSet = new ArrayList<Var_Base>();
		for (Var_Base var_Base : set) {
			newSet.add(var_Base.copy());
		}
		return new Var_Set(newSet);
	}

	public boolean equals(Object obj) {
		try {
			Var_Set varSet = (Var_Set) obj;
			if (set.equals(varSet.set)) {
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
	public Var_Set evaluate(String name, Var_Base newValue)
			throws AlgebraicRuntimeException, AlgebraicSimplifyingException {
		List<Var_Base> set = new ArrayList<Var_Base>();
		for (Var_Base var_Base : this.set) {
			set.add(var_Base.evaluate(name, newValue));
		}
		return new Var_Set(set);
	}

	@Override
	public double getDegree() throws AlgebraicRuntimeException {
		double degree=0;
		for (Var_Base var_Base : set) {
			if(degree<var_Base.getDegree()) {
				degree=var_Base.getDegree();
			}
		}
		return degree;
	}

}
