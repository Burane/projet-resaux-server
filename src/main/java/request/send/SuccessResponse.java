package request.send;

public class SuccessResponse extends GenericResponse {
	private final String message;

	public SuccessResponse(String message) {
		this.message = message;
	}
}
