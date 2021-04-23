package request.send;

public class AuthentificationResponse extends GenericResponse {
	private boolean success;

	public AuthentificationResponse(boolean success) {
		this.success = success;
	}
}
