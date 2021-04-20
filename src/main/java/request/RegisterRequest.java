package request;

public class RegisterRequest extends GenericRequest implements GenericRequestInterface{
	public String username;
	public String password;

	public RegisterRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterRequest{" +  "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}
}
