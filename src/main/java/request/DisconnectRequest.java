package request;

import server.Client;

public class DisconnectRequest extends GenericRequest implements GenericRequestInterface {


	@Override
	public void handle(Client client) {
		if(!client.isAuthentified())
			return;
		client.respond("Disconnected");
		client.close();
	}
}
