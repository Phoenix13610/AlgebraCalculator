package algebra;

public class AlgebraicParsingException extends Exception{

	public AlgebraicParsingException(String string) {
		super(string);
	}

	public void printStackTrace() {
		super.printStackTrace();
		System.exit(0);
	}
}
