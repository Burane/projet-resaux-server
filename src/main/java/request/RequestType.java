package request;

import com.google.gson.annotations.SerializedName;

public enum RequestType {
	REGISTER("register"),

	LOGIN("login"),

	UPLOAD("upload"),

	RESEARCH("research");

	private String field;

	@Override
	public String toString() {
		return "RequestType{" + "field='" + field + '\'' + '}';
	}

	RequestType(String field) {
		this.field = field;
	}
	public String getField(){
		return this.field;
	}
}
