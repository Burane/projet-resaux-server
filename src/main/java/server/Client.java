package server;

import request.RequestHandler;
import request.send.GenericResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Client implements Runnable {

	private final Socket client;
	private PrintStream writer;
	private BufferedReader reader;
	private RequestHandler requestHandler;
	private final LinkedList<String> queue = new LinkedList<>();
	private boolean isAuthentified = false;
	private int userId = -1;

	public Client(Socket ClientSock) {
		client = ClientSock;
	}

	public void run() {
		requestHandler = new RequestHandler(this);
		while (true) {
			String request = receiveContent();
			assert request != null;
			if (!request.isEmpty()) {
				queue.addFirst(request);
				System.out.println(request);
				String currentRequest = queue.pollLast();
				if (currentRequest != null)
					requestHandler.handle(currentRequest);
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
	public void respond(GenericResponse response) {
		try {
			writer = new PrintStream(client.getOutputStream());
			writer.write(response.toJson().getBytes());
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
		StringBuilder str = new StringBuilder();
		String line;
		reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty())
				break;
			str.append(line).append("\n");
		}

		while (reader.ready())
			str.append((char) reader.read());
		return str.toString();
	}

	public boolean isAuthentified() {
		return isAuthentified;
	}

	public void setAuthentified(boolean authentified) {
		isAuthentified = authentified;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}