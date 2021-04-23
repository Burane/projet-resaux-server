package request.send;

public class AuthentificationResponse extends GenericResponse {
	private final boolean success;

	public AuthentificationResponse(boolean success) {
		this.success = success;
	}
}
