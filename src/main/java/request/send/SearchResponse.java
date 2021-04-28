package request.send;

import java.util.ArrayList;

public class SearchResponse extends GenericResponse {
	final ArrayList<PreviewImageResponse> images;

	public SearchResponse(ArrayList<PreviewImageResponse> images) {
		super(ResponseType.SEARCH);
		this.images = images;
	}
}