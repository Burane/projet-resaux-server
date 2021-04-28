package request.send;

public class FullImageResponse extends GenericResponse {
	private final String titre;
	private final String data;
	private final int imageId;
	private final int nbLike;
	private final boolean isLikedByUser;
	private final boolean isOwnedByUser;

	public FullImageResponse(String titre, String data, int imageId, int nbLike, boolean isLikedByUser, boolean isOwnedByUser) {
		super(ResponseType.FULLIMAGE);
		this.titre = titre;
		this.data = data;
		this.imageId = imageId;
		this.nbLike = nbLike;
		this.isLikedByUser = isLikedByUser;
		this.isOwnedByUser = isOwnedByUser;
	}

}