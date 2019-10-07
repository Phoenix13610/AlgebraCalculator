package algebra.vars;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.ComponentReference;
import algebra.Var_Base;

public abstract class Var_Special implements Var_Base {
	public final String name;

	public Var_Special(String name) {
		this.name = name;
	}

	public abstract Var_Special parseVar(String name, ComponentReference reference)
			throws AlgebraicParsingException, AlgebraicRuntimeException;

	public String toString() {
		return name;
	}

	public boolean equals(Object obj) {
		try {
			Var_Special n = (Var_Special) obj;
			return n.name.equals(name);
		} catch (ClassCastException e) {
			return false;
		}

	}
	
	public double getDegree() {
		return 0;
	}
}
