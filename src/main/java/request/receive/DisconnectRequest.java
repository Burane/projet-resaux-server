package request.receive;

import request.GenericRequest;
import request.GenericRequestInterface;
import request.send.ErrorResponse;
import request.send.SuccessResponse;
import server.Client;

public class DisconnectRequest extends GenericRequest implements GenericRequestInterface {


	@Override
	public void handle(Client client) {
		if(!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified").toJson());
			return;
		}
		client.respond(new SuccessResponse("Successfully disconnected").toJson());
		client.close();
	}
}
