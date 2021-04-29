package Utils;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public abstract class ImageUtils {

	public static byte[] resizeImage(byte[] imageData, double resizePercentage) {

		try (InputStream in = new ByteArrayInputStream(imageData)) {
			BufferedImage fullSize = ImageIO.read(in);

			double newheight_db = fullSize.getHeight() * resizePercentage;
			double newwidth_db = fullSize.getWidth() * resizePercentage;

			int newheight = (int) newheight_db;
			int newwidth = (int) newwidth_db;

			BufferedImage resized = new BufferedImage(newwidth, newheight, BufferedImage.SCALE_REPLICATE);

			Graphics2D g2 = (Graphics2D) resized.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			g2.drawImage(fullSize, 0, 0, newwidth, newheight, null);

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				ImageIO.write(resized, getImageFormat(imageData), baos);
				baos.flush();
				return baos.toByteArray();
			}

		} catch (IOException e) {
			System.out.println("error resizing screenshot" + e.toString());
			return null;
		}
	}

	public static byte[] compressImage(byte[] imageData, float compressQuality) {

		try (InputStream in = new ByteArrayInputStream(imageData)) {
			BufferedImage image = ImageIO.read(in);

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(getImageFormat(imageData));
			ImageWriter writer = writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressQuality);
			writer.write(null, new IIOImage(image, null, null), param);

			byte[] compressedImage = os.toByteArray();

			os.close();
			ios.close();
			writer.dispose();

			return compressedImage;
		} catch (IOException e) {
			System.out.println("error resizing screenshot" + e.toString());
			return null;
		}
	}

	public static String getImageFormat(byte[] image) throws IOException {

		ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(image));
		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

		if (!iter.hasNext())
			throw new RuntimeException("No readers found!");

		ImageReader reader = iter.next();
		iis.close();
		return reader.getFormatName();
	}
}