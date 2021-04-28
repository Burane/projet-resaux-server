package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.FullImageResponse;
import server.Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class FullImageRequest extends GenericRequest implements GenericRequestInterface {
	private int imageId;

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified"));
			return;
		}

		Connection connection = BDDConnection.getConnection();

		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT Id_Image, Titre, Full_Image, " +
							"(SELECT COUNT(*) FROM Note WHERE Note = 1 AND Id_Image = ?) as nb_like, " +
							"IFNULL((SELECT Note FROM Note WHERE Id_Image = ? AND Id_Utilisateur = ?), 0) as isLikedByUser ," +
							"(SELECT COUNT(*) FROM Upload WHERE Id_Image = ? AND Id_Utilisateur = ?) as isOwnedByUser " +
							"FROM Image WHERE Id_Image = ?");
			preparedStatement.setInt(1, imageId);
			preparedStatement.setInt(2, imageId);
			preparedStatement.setInt(3, client.getUserId());
			preparedStatement.setInt(4, imageId);
			preparedStatement.setInt(5, client.getUserId());
			preparedStatement.setInt(6, imageId);

			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.next();

			String titre = resultSet.getString("Titre");
			int id = resultSet.getInt("Id_Image");
			byte[] binaryData = resultSet.getBinaryStream("Full_Image").readAllBytes();
			String data = new String(Base64.getEncoder().encode(binaryData));
			int nbLike = resultSet.getInt("nb_like");
			boolean isLikedByUser = resultSet.getBoolean("isLikedByUser");
			boolean isOwnedByUser = resultSet.getBoolean("isOwnedByUser");
			FullImageResponse fullImageResponse = new FullImageResponse(titre, data, id, nbLike, isLikedByUser, isOwnedByUser);

			client.respond(fullImageResponse);
		} catch (SQLException | IOException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()));
			throwables.printStackTrace();
		}

	}
}