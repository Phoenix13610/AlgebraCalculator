package algebra;

public class AlgebraicRuntimeException extends Exception{

	public AlgebraicRuntimeException() {
		super();
	}

	public AlgebraicRuntimeException(String message){
		super(message);
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.exit(0);
	}
}
