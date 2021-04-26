package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.SuccessResponse;
import server.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterRequest extends GenericRequest implements GenericRequestInterface {
	public final String username;
	public final String password;

	public RegisterRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterRequest{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}

	@Override
	public void handle(Client client) {
		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn
					.prepareStatement("INSERT INTO `Utilisateur` (`username`, `password`) VALUES (?, ?) ");
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, sha256Hash(password));
			prepareStatement.execute();
			client.respond(new SuccessResponse("Successfully registered"));
		} catch (SQLException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()));
			throwables.printStackTrace();
		}
	}



}
