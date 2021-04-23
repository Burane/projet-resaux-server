package server;

import request.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Client implements Runnable {

	private Socket client;
	private PrintStream writer;
	private BufferedReader reader;
	private RequestHandler requestHandler;
	private LinkedList<String> queue = new LinkedList<String>;
	private boolean isAuthentified = false;
	private int userId = -1;

	public Client(Socket ClientSock) {
		client = ClientSock;
	}

	public void run() {
		requestHandler = new RequestHandler(this);
		while (true) {
			String request = receiveContent();
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
		reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty())
				break;
			str += line + "\n";
		}

		while (reader.ready())
			str += (char) reader.read();
		return str;
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