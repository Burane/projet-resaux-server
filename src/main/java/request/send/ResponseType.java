package request.send;

public enum ResponseType {
	AUTHENTIFICATION("authentification"),
	ERROR("error"),
	SUCCESS("success"),
	PREVIEWIMAGE("previewImage"),
	FULLIMAGE("fullImage"),
	LIKE("like"),
	SEARCH("search"),
	SEARCHPERDAY("searchPerDay"),
	ONESEARCHDAY("oneSearchDay");



	private String field;

	ResponseType(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return field;
	}
}