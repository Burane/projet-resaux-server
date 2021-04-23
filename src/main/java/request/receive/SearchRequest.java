package request.receive;

import BDDconnection.BDDConnection;
import KeywordsExctractor.KeywordsExtractor;
import KeywordsExctractor.Langage;
import request.GenericRequest;
import request.GenericRequestInterface;
import request.send.ErrorResponse;
import request.send.ImageResponse;
import request.send.SearchResponse;
import server.Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class SearchRequest extends GenericRequest implements GenericRequestInterface {
	private String query;
	private int limitFrom;
	private int limitTo;
	private ArrayList<String> keywords;

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified").toJson());
			return;
		}

		KeywordsExtractor keywordsExtractor = new KeywordsExtractor(Langage.FRENCH);
		keywords = keywordsExtractor.getKeywords(query);
		System.out.println(Arrays.toString(keywords.toArray()));

		getImages(client);
		insertSearch(client.getUserId());

	}

	private void insertSearch(int userId) {
		Connection conn = BDDConnection.getConnection();

		for (String keyword : keywords) {
			try {
				PreparedStatement prepareStatement = conn.prepareStatement(
						"INSERT INTO Champ (Champ) VALUES (?) ON DUPLICATE KEY UPDATE Id_Champ=Id_Champ");
				prepareStatement.setString(1, keyword);
				prepareStatement.execute();

				PreparedStatement prepareStatement2 = conn.prepareStatement(
						"INSERT INTO Recherche (`Id_Utilisateur`, `Id_Champ`) VALUES (?, (SELECT Id_Champ FROM Champ WHERE Champ LIKE ?))");
				prepareStatement2.setInt(1, userId);
				prepareStatement2.setString(2, keyword);
				prepareStatement2.execute();

			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}

		}
	}

	public void getImages(Client client) {
		StringBuilder queryBuilder = new StringBuilder();

		boolean isFirstWhere = true;

		for (int i = 0; i < keywords.size(); i++) {
			queryBuilder.append(isFirstWhere ? "WHERE" : "OR");
			queryBuilder.append(" Titre LIKE ? ");
			isFirstWhere = false;
			queryBuilder.append("OR");
			queryBuilder.append(" Libelle LIKE ? ");
		}

		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn.prepareStatement(
					"SELECT Id_Image, Titre, Tiny_Image  FROM Image WHERE Id_Image IN ( SELECT DISTINCT Image.Id_Image FROM Image LEFT JOIN Possede ON Image.Id_Image = Possede.Id_Image LEFT JOIN Tag ON Tag.Id_Tag = Possede.Id_Tag " + queryBuilder
							.toString() + " ) LIMIT ?, ?");

			for (int i = 0; i < keywords.size(); i++) {
				prepareStatement.setString(i * 2 + 1, "%" + keywords.get(i) + "%");
				prepareStatement.setString(i * 2 + 2, "%" + keywords.get(i) + "%");
			}

			prepareStatement.setInt(keywords.size() * 2 + 1, limitFrom);
			prepareStatement.setInt(keywords.size() * 2 + 2, limitTo);

			ResultSet resultSet = prepareStatement.executeQuery();

			ArrayList<ImageResponse> imageResponses = new ArrayList<>();
			while (resultSet.next()) {
				String titre = resultSet.getString("Titre");
				int id = resultSet.getInt("Id_Image");
				byte[] binaryData = resultSet.getBinaryStream("Tiny_Image").readAllBytes();
				String data = new String(Base64.getEncoder().encode(binaryData));
				imageResponses.add(new ImageResponse(titre, data, id));
			}
			SearchResponse response = new SearchResponse(imageResponses);
			client.respond(response.toJson());

		} catch (SQLException | IOException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()).toJson());
			throwables.printStackTrace();
		}
	}
}
