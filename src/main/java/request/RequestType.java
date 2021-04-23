package request;

public enum RequestType {
	REGISTER("register"),

	LOGIN("login"),

	UPLOAD("upload"),

	SEARCH("search"),

	DISCONNECT("disconnect"),

	DELETE("delete"),

	IMAGE("image");

	private final String field;

	@Override
	public String toString() {
		return field;
	}

	RequestType(String field) {
		this.field = field;
	}
}
