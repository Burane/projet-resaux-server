package request.receive;

import request.send.ErrorResponse;
import request.send.SuccessResponse;
import server.Client;

public class DisconnectRequest extends GenericRequest implements GenericRequestInterface {


	@Override
	public void handle(Client client) {
		if(!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified"));
			return;
		}
		client.respond(new SuccessResponse("Successfully disconnected"));
		client.close();
	}
}
