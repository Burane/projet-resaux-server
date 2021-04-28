package gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.receive.GenericRequest;
import request.receive.RequestType;
import request.receive.*;

public abstract class RequestDeserializer {

	public static Gson getDeserializer() {
		RuntimeTypeAdapterFactory<GenericRequest> typeAdapterFactory = RuntimeTypeAdapterFactory
				.of(GenericRequest.class, "type")
				.registerSubtype(RegisterRequest.class, RequestType.REGISTER.toString())
				.registerSubtype(LoginRequest.class, RequestType.LOGIN.toString())
				.registerSubtype(DisconnectRequest.class, RequestType.DISCONNECT.toString())
				.registerSubtype(SearchRequest.class, RequestType.SEARCH.toString())
				.registerSubtype(UploadRequest.class, RequestType.UPLOAD.toString())
				.registerSubtype(DeleteRequest.class, RequestType.DELETE.toString())
				.registerSubtype(FullImageRequest.class, RequestType.FULLIMAGE.toString());

		return new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
	}
}