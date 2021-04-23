package request.send;

public class ErrorResponse extends GenericResponse {

	private final String message;

	public ErrorResponse(String errorMessage) {
		super();
		this.message = errorMessage;
	}
}
