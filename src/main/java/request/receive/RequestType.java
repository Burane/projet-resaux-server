package request.receive;

public enum RequestType {
	REGISTER("register"),

	LOGIN("login"),

	UPLOAD("upload"),

	SEARCH("search"),

	DISCONNECT("disconnect"),

	DELETE("delete"),

	PREVIEWIMAGE("previewImage"),

	FULLIMAGE("fullImage");

	private final String field;

	@Override
	public String toString() {
		return field;
	}

	RequestType(String field) {
		this.field = field;
	}
}