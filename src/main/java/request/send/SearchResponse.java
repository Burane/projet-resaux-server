package request.send;

import java.util.ArrayList;

public class SearchResponse extends GenericResponse {
	final ArrayList<ImageResponse> images;

	public SearchResponse(ArrayList<ImageResponse> images) {
		super(ResponseType.SEARCH);
		this.images = images;
	}
}
