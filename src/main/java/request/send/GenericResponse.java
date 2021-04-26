package request.send;

import com.google.gson.Gson;

public abstract class GenericResponse {
	private final String type;

	public GenericResponse(ResponseType type) {
		this.type = type.toString();
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}

}
