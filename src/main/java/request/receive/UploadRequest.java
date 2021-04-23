package request.receive;

import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

public class UploadRequest extends GenericRequest implements GenericRequestInterface {

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond("Error not authentified");
			return;
		}


	}
}
