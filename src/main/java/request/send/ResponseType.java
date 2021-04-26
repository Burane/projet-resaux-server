package request.send;

public enum ResponseType {
	AUTHENTIFICATION("authentification"),
	ERROR("error"),
	SUCCESS("success"),
	IMAGE("image"),
	SEARCH("search");



	private String field;

	ResponseType(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return field;
	}
}
