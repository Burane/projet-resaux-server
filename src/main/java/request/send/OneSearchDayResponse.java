package request.send;

import java.time.Instant;
import java.time.LocalDate;

public class OneSearchDayResponse extends GenericResponse {
	private final LocalDate date;
	private final int nbRequest;

	public OneSearchDayResponse(LocalDate date, int nbRequest) {
		super(ResponseType.ONESEARCHDAY);
		this.date = date;
		this.nbRequest = nbRequest;
	}

	@Override
	public String toString() {
		return "OneSearchDayResponse{" + "date=" + date + ", nbRequest=" + nbRequest + '}';
	}
}