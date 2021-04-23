package request.receive;

import BDDconnection.BDDConnection;
import request.GenericRequest;
import request.GenericRequestInterface;
import request.send.AuthentificationResponse;
import request.send.ErrorResponse;
import server.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRequest extends GenericRequest implements GenericRequestInterface {
	public final String username;
	public final String password;

	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}

	@Override
	public void handle(Client client) {
		if (client.isAuthentified()) {
			client.respond(new ErrorResponse("User already authentified").toJson());
			return;
		}
		int userId = authenticate(client); // return -1 if not authentified
		boolean isAuthenticateSuccess = userId > 0;
		client.setUserId(userId);
		client.setAuthentified(isAuthenticateSuccess);
		client.respond(new AuthentificationResponse(isAuthenticateSuccess).toJson());
	}

	public int authenticate(Client client) {
		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn.prepareStatement(
					"SELECT Id_Utilisateur FROM `Utilisateur` WHERE `username` LIKE ? AND `password` LIKE ?");
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, sha256Hash(password));
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}

		} catch (SQLException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()).toJson());
			throwables.printStackTrace();
		}
		return -1;
	}
}
