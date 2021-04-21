package request;

import com.google.gson.Gson;
import gson.RequestDeserializer;
import server.Client;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.util.Base64;

public class RequestHandler {
	private final Client client;
	private boolean isAuthentified = false;

	public RequestHandler(Client client) {
		this.client = client;
	}

	public void handle(String rawRequest) {

		Gson gson = RequestDeserializer.getDeserializer();
		GenericRequestInterface request = null;
		try {
			request = gson.fromJson(rawRequest, GenericRequest.class);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			client.respond("Error syntax in request : \r" + rawRequest);
			return;
		}

		if (!(request instanceof LoginRequest) && isAuthentified == false) {
			if (!(request instanceof RegisterRequest)) {
				client.respond("Error not authentified");
				return;
			}
		}

		if (request instanceof LoginRequest) {
			isAuthentified = ((LoginRequest) request).authenticate();
			String response = isAuthentified ? "Authentification success" : "Authentification error";
			client.respond(response);
			return;
		}

		request.handle();

	}
}
