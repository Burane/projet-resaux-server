import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.RequestDeserializer;
import request.GenericRequest;
import request.GenericRequestInterface;

import java.util.Enumeration;
import java.util.Properties;

public class Test {

	public static void main(String[] args) {
		String jsonRegister = "{\"type\":\"register\", \"username\":\"user1\", \"password\":\"user1\" }";
		String jsonLogin = "{\"type\": \"login\",\"username\": \"user1\",\"password\": \"user1\"}";
		Gson gson = RequestDeserializer.getDeserializer();

		GenericRequestInterface reqLogin = gson.fromJson(jsonLogin, GenericRequest.class);
		System.out.println(reqLogin);
		GenericRequestInterface reqRegister = gson.fromJson(jsonRegister, GenericRequest.class);
		System.out.println(reqRegister);
		System.out.println();
		Properties properties = System.getProperties();
		properties.forEach((k, v) -> System.out.println(k + ":" + v));
		System.out.println();
		System.out.println(System.getProperty("test"));
	}

}

