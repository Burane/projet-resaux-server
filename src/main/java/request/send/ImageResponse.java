package request.send;

import java.util.ArrayList;

public class ImageResponse extends GenericResponse {
	private String titre;
	private String data;
	private int imageId;

	public ImageResponse(String titre, String data, int imageId) {
		this.titre = titre;
		this.data = data;
		this.imageId = imageId;
	}
}
