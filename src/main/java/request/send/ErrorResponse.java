package request.send;

public class ErrorResponse extends GenericResponse {

	private String message;

	public ErrorResponse(String errorMessage) {
		super();
		this.message = errorMessage;
	}
}
