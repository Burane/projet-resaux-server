package request;

public class LoginRequest extends GenericRequest implements GenericRequestInterface {
	public String username;
	public String password;

	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}
}
