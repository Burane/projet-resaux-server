package request;

import com.google.gson.Gson;
import gson.RequestDeserializer;
import request.receive.GenericRequest;
import request.receive.GenericRequestInterface;
import server.Client;

public class RequestHandler {
	private final Client client;

	public RequestHandler(Client client) {
		this.client = client;
	}

	public void handle(String rawRequest) {

		Gson gson = RequestDeserializer.getDeserializer();
		GenericRequestInterface request;
		try {
			request = gson.fromJson(rawRequest, GenericRequest.class);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			client.respond("Error syntax in request : \r" + rawRequest);
			return;
		}

		request.handle(client);

	}
}
