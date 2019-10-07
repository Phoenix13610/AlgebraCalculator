package algebra;

import algebra.vars.Var_Variable;

public class Source {
	public static ComponentReference reference = new ComponentReference() {
		@Override
		public void setReference() {
			addBasics();
			setToDegrees();
		}
	};

	public static void main(String[] args) {
		long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		double start = System.currentTimeMillis();
		try {
			reference.varValues = AlgebraicVariablesValues.parse(new String[] {"x=a","a=s","s=1"}, reference);
		} catch (AlgebraicParsingException | AlgebraicRuntimeException e) {
			e.printStackTrace();
		}
		AlgebraicExpression a = null;
		a = new AlgebraicExpression(new Var_Variable(0.0));
		try {
			a = AlgebraicExpression.parse("x", reference);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(a);
//		System.out.println((System.currentTimeMillis() - start) / 1000);
//		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - beforeUsedMem);
		try {
			System.out.println(a.simplify().evaluate(reference.varValues).simplify());
		} catch (AlgebraicRuntimeException | AlgebraicSimplifyingException e) {
			e.printStackTrace();
		}
//		System.out.println((System.currentTimeMillis() - start) / 1000);
//		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - beforeUsedMem);
	}
}
