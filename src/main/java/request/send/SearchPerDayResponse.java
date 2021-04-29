package request.send;

import java.util.ArrayList;

public class SearchPerDayResponse extends GenericResponse {
	final ArrayList<OneSearchDayResponse> searchDayResponses;

	public SearchPerDayResponse(ArrayList<OneSearchDayResponse> searchDayResponses) {
		super(ResponseType.SEARCHPERDAY);
		this.searchDayResponses = searchDayResponses;
	}
}