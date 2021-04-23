import java.io.*;
import java.util.Base64;

public class Test {



	public static void main(String[] args) {
		File file = new File("C:\\Users\\antoi\\Pictures\\15e31c4b27e4501a8abd64182acbaad5.jpg");
		try {
			FileInputStream in = new FileInputStream(file);

			System.out.println( new String(Base64.getEncoder().encode(in.readAllBytes()), "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

