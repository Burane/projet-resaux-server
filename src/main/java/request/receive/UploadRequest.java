package request.receive;

import BDDconnection.BDDConnection;
import Utils.SQLUtils;
import net.coobird.thumbnailator.Thumbnails;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;

public class UploadRequest extends GenericRequest implements GenericRequestInterface {
	private String data;
	private String titre;
	private String[] tags;

	public UploadRequest(String data, String titre, String[] tags) {
		this.data = data;
		this.titre = titre;
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "UploadRequest{" + "data='" + data + '\'' + ", \ntitre='" + titre + '\'' + ", tags=" + Arrays
				.toString(tags) + '}';
	}

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond("Error not authentified");
			return;
		}

		System.out.println(this);

		Connection connection = BDDConnection.getConnection();
		Savepoint save = null;
		try {
			connection.setAutoCommit(false);
			connection.setSavepoint();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		try {
			int imageId = insertImage(connection); // return -1 if error

			insertUpload(client.getUserId(), connection, imageId);

			for (String tag : tags) {
				int tagId = createTag(connection, tag);
				insertPossede(connection, imageId, tagId);
			}

		} catch (SQLException throwable) {
			try {
				connection.rollback(save);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			throwable.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}

		System.out.println("request handled");

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
		InputStream fullSizeImage = new ByteArrayInputStream(binaryImage);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.out.println("before resize");
		try {
			Thumbnails.of(fullSizeImage).scale(0.25).outputQuality(0.25).toOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream tinyImage = new ByteArrayInputStream(out.toByteArray());
		System.out.println("after resize");

		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO Image (Titre, Full_Image, Tiny_Image) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, titre);
		preparedStatement.setBinaryStream(2, fullSizeImage);
		preparedStatement.setBinaryStream(3, tinyImage);
		preparedStatement.execute();

		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		if (resultSet.next())
			return resultSet.getInt(1);
		return -1;
	}

	private int createTag(Connection connection, String tag) throws SQLException {
		int tagId = -1;

		PreparedStatement preparedStatement2 = connection
				.prepareStatement("SELECT Id_Tag FROM Tag WHERE Libelle LIKE ?");
		preparedStatement2.setString(1, tag);
		ResultSet resultSet2 = preparedStatement2.executeQuery();

		if (resultSet2.next())
			tagId = resultSet2.getInt(1);

		System.out.println("TAG ID = " + tagId);

		if (tagId == -1) {

			PreparedStatement prepareStatement = connection
					.prepareStatement("INSERT INTO Tag (Libelle) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1, tag);
			prepareStatement.execute();

			ResultSet resultSet = prepareStatement.getGeneratedKeys();
			if (resultSet.next())
				tagId = resultSet.getInt(1);

		}
		return tagId;
	}
}
