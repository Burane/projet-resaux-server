package request.send;

public class LikeResponse extends GenericResponse {
	private final int imageId;
	private final boolean isLikedByUser;

	public LikeResponse(int imageId, boolean isLikedByUser) {
		super(ResponseType.LIKE);
		this.imageId = imageId;
		this.isLikedByUser = isLikedByUser;
	}
}