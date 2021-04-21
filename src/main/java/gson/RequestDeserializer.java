package gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.*;

public abstract class RequestDeserializer {

	public static Gson getDeserializer() {
		RuntimeTypeAdapterFactory<GenericRequest> typeAdapterFactory = RuntimeTypeAdapterFactory
				.of(GenericRequest.class, "type")
				.registerSubtype(RegisterRequest.class, RequestType.REGISTER.getField())
				.registerSubtype(LoginRequest.class, RequestType.LOGIN.getField())
				.registerSubtype(DisconnectRequest.class,RequestType.DISCONNECT.getField());

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
		return gson;
	}
}