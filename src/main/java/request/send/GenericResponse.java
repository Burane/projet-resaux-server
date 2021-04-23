package request.send;

import com.google.gson.Gson;

import java.util.Locale;

public class GenericResponse {
	private final String type;

	public GenericResponse() {
		String name = getClass().getSimpleName().toLowerCase(Locale.ROOT);
		this.type = name.substring(0, name.length() - "response".length());
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
