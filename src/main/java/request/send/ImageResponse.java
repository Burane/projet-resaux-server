package request.send;

public class ImageResponse extends GenericResponse {
	private final String titre;
	private final String data;
	private final int imageId;

	public ImageResponse(String titre, String data, int imageId) {
		super(ResponseType.IMAGE);
		this.titre = titre;
		this.data = data;
		this.imageId = imageId;
	}
}
