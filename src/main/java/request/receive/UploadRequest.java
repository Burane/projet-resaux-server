package request.receive;

import BDDconnection.BDDConnection;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Base64;

public class UploadRequest extends GenericRequest implements GenericRequestInterface {
	private String data;
	private String titre;
	private String[] tags;

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond("Error not authentified");
			return;
		}

		Connection connection = BDDConnection.getConnection();

		try {
			int imageId = insertImage(connection); // return -1 if error

			insertUpload(client.getUserId(), connection, imageId);

			for (int i = 0; i < tags.length; i++) {
				int tagId = createTag(connection, tags[i]);
				insertPossede(connection, imageId, tagId);
			}

		} catch (SQLException throwable) {
			throwable.printStackTrace();
		}

	}

	private void insertPossede(Connection connection, int imageId, int tagId) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO Possede (Id_Image, Id_Tag) VALUES (?, ?)");
		preparedStatement.setInt(1, imageId);
		preparedStatement.setInt(2, tagId);
		preparedStatement.execute();
	}

	private void insertUpload(int userId, Connection connection, int imageId) throws SQLException {
		PreparedStatement preparedStatement2 = connection
				.prepareStatement("INSERT INTO Upload (Id_Utilisateur, Id_Image) VALUES (?, ?)");
		preparedStatement2.setInt(1, userId);
		preparedStatement2.setInt(2, imageId);
		preparedStatement2.execute();
	}

	private int insertImage(Connection connection) throws SQLException {

		byte[] binaryImage = Base64.getDecoder().decode(data);
		InputStream inputStream = new ByteArrayInputStream(binaryImage);

		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO Image (Titre, Full_Image, Tiny_Image) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, titre);
		preparedStatement.setBinaryStream(2, inputStream);
		preparedStatement.setBinaryStream(3, inputStream);
		preparedStatement.execute();

		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		if (resultSet.next())
			return resultSet.getInt(1);
		return -1;
	}

	private int createTag(Connection connection, String tag) throws SQLException {
		int tagId = -1;
		PreparedStatement prepareStatement = connection
				.prepareStatement("INSERT INTO Tag (Libelle) VALUES (?) ON DUPLICATE KEY UPDATE Id_Tag=Id_Tag",
						Statement.RETURN_GENERATED_KEYS);
		prepareStatement.setString(1, tag);
		prepareStatement.execute();

		ResultSet resultSet = prepareStatement.getGeneratedKeys();
		if (resultSet.next())
			tagId = resultSet.getInt(1);

		if (tagId == -1) {
			PreparedStatement preparedStatement2 = connection
					.prepareStatement("SELECT Id_Tag FROM Tag WHERE Libelle LIKE ?");
			preparedStatement2.setString(1, tag);
			ResultSet resultSet2 = preparedStatement2.executeQuery();

			if (resultSet2.next())
				tagId = resultSet.getInt(1);

		}
		return tagId;
	}
}
