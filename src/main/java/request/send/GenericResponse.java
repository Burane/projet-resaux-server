package request.send;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.LocalDateAdapter;

import java.time.LocalDate;

public abstract class GenericResponse {
	private final String type;

	public GenericResponse(ResponseType type) {
		this.type = type.toString();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}

}