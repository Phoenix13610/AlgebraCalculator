package algebra.vars;

import java.util.ArrayList;
import java.util.Arrays;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.Var_Base;
/**
 * 
 * @author AntoineChevalier
 *@since 1.0
 */
public class UndefinedVar {
	public final String name;
	public final Var_Base expo;
	/**
	 * Basic contructor method
	 * @param name
	 * @param expo
	 */
	public UndefinedVar(String name,Var_Base expo) {
		this.name=name;
		this.expo=expo;
	}

	public String toString() {
		String result=name;
		if(!expo.equals(new Var_Variable(1))) {
			result+="^("+expo+")";
		}
		return result;
	}
	
	public boolean equals(Object obj) {
		try {
			UndefinedVar v=(UndefinedVar)obj;
			if(v.name.equals(name)&&v.expo.equals(expo)) {
				return true;
			}
		} catch (ClassCastException e) {
		}
		return false;
	}
	/**
	 * Creates a copy of this object
	 * 
	 * @return the copy
	 * @throws AlgebraicSimplifyingException
	 * @throws AlgebraicRuntimeException
	 */
	public UndefinedVar copy(){
		return new UndefinedVar(name, expo.copy());
	}

	public UndefinedVar build(String name,Var_Base expo) throws AlgebraicParsingException {
		return new UndefinedVar(name, expo);
	}
	
	public Var_Variable simplify() {
		return new Var_Variable(1, new ArrayList<UndefinedVar>(Arrays.asList(new UndefinedVar[] {this})));
	}
}
