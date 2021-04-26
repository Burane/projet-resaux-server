package request.send;

public class SuccessResponse extends GenericResponse {
	private final String message;

	public SuccessResponse(String message) {
		super(ResponseType.SUCCESS);
		this.message = message;
	}
}
