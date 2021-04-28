package request.send;

public class PreviewImageResponse extends GenericResponse {
	private final String titre;
	private final String data;
	private final int imageId;

	public PreviewImageResponse(String titre, String data, int id) {
		super(ResponseType.PREVIEWIMAGE);
		this.titre = titre;
		this.data = data;
		this.imageId = id;
	}
}