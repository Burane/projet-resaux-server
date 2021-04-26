package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.ImageResponse;
import server.Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class ImageRequest extends GenericRequest implements GenericRequestInterface {
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
			preparedStatement = connection
					.prepareStatement("SELECT Id_Image, Titre, Full_Image  FROM Image WHERE Id_Image = ?");
			preparedStatement.setInt(1, imageId);

			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.next();
			String titre = resultSet.getString("Titre");
			int id = resultSet.getInt("Id_Image");
			byte[] binaryData = resultSet.getBinaryStream("Full_Image").readAllBytes();
			String data = new String(Base64.getEncoder().encode(binaryData));
			ImageResponse imageResponse = new ImageResponse(titre, data, id);

			client.respond(imageResponse);
		} catch (SQLException | IOException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()));
			throwables.printStackTrace();
		}

	}
}
