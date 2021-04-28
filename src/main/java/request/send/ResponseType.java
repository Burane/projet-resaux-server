package request.send;

public enum ResponseType {
	AUTHENTIFICATION("authentification"),
	ERROR("error"),
	SUCCESS("success"),
	PREVIEWIMAGE("previewImage"),
	FULLIMAGE("fullImage"),
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