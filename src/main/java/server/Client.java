package server;

import request.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {

	private Socket client;
	private PrintStream writer;
	private BufferedReader reader;
	private RequestHandler requestHandler;


	public Client(Socket ClientSock) {
		client = ClientSock;
	}

	public void run() {
		requestHandler = new RequestHandler(this);
		while (true) {
			String request = receiveContent();
			if (!request.isEmpty()) {
				System.out.println(request);
				respond("got you !".getBytes(StandardCharsets.UTF_8));
				requestHandler.handle(request);

			}
		}
	}

	public void respond(byte[] content) {
		try {
			writer = new PrintStream(client.getOutputStream());
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}
	public void respond(String content) {
		try {
			writer = new PrintStream(client.getOutputStream());
			writer.write(content.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}

	private String receiveContent() {
		try {
			return readBuffer(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String readBuffer(InputStream stream) throws IOException {
		String str = "", line;
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty())
				break;
			str += line + "\n";
		}

		while (reader.ready())
			str += (char) reader.read();
		return str;
	}

}