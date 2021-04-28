package request.send;

public class FullImageResponse extends GenericResponse {
	private final String titre;
	private final String data;
	private final int imageId;
	private final boolean isLikedByUser;
	private final int nbLike;

	public FullImageResponse(String titre, String data, int imageId, int nbLike, boolean isLikedByUser) {
		super(ResponseType.FULLIMAGE);
		this.titre = titre;
		this.data = data;
		this.imageId = imageId;
		this.nbLike = nbLike;
		this.isLikedByUser = isLikedByUser;
	}

}