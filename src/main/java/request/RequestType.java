package request;

import com.google.gson.annotations.SerializedName;

public enum RequestType {
	REGISTER("register"),

	LOGIN("login"),

	UPLOAD("upload"),

	SEARCH("research"),

	DISCONNECT("disconnect");

	private String field;

	@Override
	public String toString() {
		return field;
	}

	RequestType(String field) {
		this.field = field;
	}
}
