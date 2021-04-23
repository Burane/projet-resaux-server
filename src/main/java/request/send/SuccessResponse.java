package request.send;

public class SuccessResponse extends GenericResponse {
	private String message;

	public SuccessResponse(String message) {
		this.message = message;
	}
}
