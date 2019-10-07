package algebra.vars.addable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algebra.AlgebraicParsingException;
import algebra.AlgebraicRuntimeException;
import algebra.AlgebraicSimplifyingException;
import algebra.ComponentReference;
import algebra.Var_Base;
import algebra.vars.Var_Set;

/**
 * A set for all the values inside a difined start and end
 * @author AntoineChevalier
 * @since 1.0
 */
public class Set_Continuous extends Var_Set {
	public final Var_Base start;
	public final Var_Base end;
	public final boolean inclusiveStart;
	public final boolean inclusiveEnd;

	public Set_Continuous() {
		super();
		start = null;
		end = null;
		inclusiveStart = false;
		inclusiveEnd = false;
	}

	public Set_Continuous(Var_Base start, boolean inclusiveStart, Var_Base end, boolean inclusiveEnd) {
		super(Arrays.asList(new Var_Base[] { start, end }));
		this.start = start;
		this.end = end;
		this.inclusiveStart = inclusiveStart;
		this.inclusiveEnd = inclusiveEnd;
	}

	public Set_Continuous(List<Var_Base>set,boolean inclusiveStart,boolean inclusiveEnd) throws AlgebraicRuntimeException{
		this(set.get(0),inclusiveStart,set.get(1),inclusiveEnd);
		if(set.size()!=2) {
			throw new AlgebraicRuntimeException("the set length isn't equal to 2");
		}
	}
	
	public static String getREGEX(ComponentReference reference) {
		return "(^[\\[\\]]\\S+,\\S+[\\[\\]]$)";
	}

	public String getRegex(ComponentReference reference) {
		return getREGEX(reference);
	}

	public Set_Continuous parseVar(String set, ComponentReference reference) throws AlgebraicParsingException, AlgebraicRuntimeException {
		boolean inclusiveStart;
		boolean inclusiveEnd;
		if (set.charAt(0) == '[') {
			inclusiveStart = true;
		} else {
			inclusiveStart = false;
		}
		if (set.charAt(set.length() - 1) == ']') {
			inclusiveEnd = true;
		} else {
			inclusiveEnd = false;
		}
		set = set.substring(1, set.length() - 1);
		String[] temp = set.split(",");
		List<Var_Base> newSet = new ArrayList<Var_Base>();
		for (String s : temp) {
			newSet.add(reference.generateVarComponent(s, 0).getVar());
		}
		if (newSet.size() > 2) {
			throw new AlgebraicParsingException("There cannot be more than two objects in a continuous set");
		} else {
			return new Set_Continuous(newSet.get(0), inclusiveStart, newSet.get(1), inclusiveEnd);
		}
	}

	public Set_Continuous simplify() throws AlgebraicSimplifyingException, AlgebraicRuntimeException {
		return new Set_Continuous(start.simplify(), inclusiveStart, end.simplify(), inclusiveEnd);
	}

	public String toString() {
		String result = "";
		if (inclusiveStart) {
			result += '[';
		} else {
			result += ']';
		}
		for (int i = 0; i < set.size(); i++) {
			result += set.get(i);
			if (i < set.size() - 1) {
				result += ',';
			}
		}
		if (inclusiveEnd) {
			result += ']';
		} else {
			result += '[';
		}
		return result;
	}

	public Set_Continuous copy(){
		return new Set_Continuous(start.copy(), inclusiveStart, end.copy(), inclusiveEnd);
	}

	public boolean equals(Object obj) {
		try {
			Set_Continuous newSet = (Set_Continuous) obj;
			return inclusiveStart == newSet.inclusiveStart && inclusiveEnd == newSet.inclusiveEnd
					&& start.equals(newSet.start) && end.equals(newSet.end) && set.equals(newSet.set);
		} catch (ClassCastException e) {
			return false;
		}

	}
}
