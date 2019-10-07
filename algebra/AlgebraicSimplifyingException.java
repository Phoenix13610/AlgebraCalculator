package algebra;

public class AlgebraicSimplifyingException extends Exception {

	public AlgebraicSimplifyingException() {
	}

	public AlgebraicSimplifyingException(String message) {
		super(message);
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.exit(0);
	}

}
