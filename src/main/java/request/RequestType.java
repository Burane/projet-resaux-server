package request;

public enum RequestType {
	REGISTER("register"),

	LOGIN("login"),

	UPLOAD("upload"),

	SEARCH("research"),

	DISCONNECT("disconnect"),

	DELETE("delete");

	private String field;

	@Override
	public String toString() {
		return field;
	}

	RequestType(String field) {
		this.field = field;
	}
}
