package request.send;

public class ErrorResponse extends GenericResponse {

	private final String message;

	public ErrorResponse(String errorMessage) {
		super(ResponseType.ERROR);
		this.message = errorMessage;
	}
}
