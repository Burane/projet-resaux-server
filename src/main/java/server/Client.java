package server;

import request.RequestHandler;
import request.send.GenericResponse;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
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
	private boolean isRunning = true;

	public Client(Socket ClientSock) {
		client = ClientSock;
		try {
			writer = new PrintStream(client.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {
			requestHandler = new RequestHandler(this);
			while (isRunning) {
				String request = receiveContent();
				if (!request.isEmpty()) {
					queue.addFirst(request);
					System.out.println(request);
					String currentRequest = queue.pollLast();
					if (currentRequest != null) {
						requestHandler.handle(currentRequest);
					}
				}
			}
		} catch (Exception e) {
			close();
		}
	}

	public void respond(byte[] content) {
		try {
			writer.write(content);
			writeEndLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}

	public void respond(String content) {
		try {
			writer.write(content.getBytes(StandardCharsets.UTF_8));
			writeEndLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}

	public void respond(GenericResponse response) {
		try {
			writer.write(response.toJson().getBytes());
			writeEndLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}

	private void writeEndLine() {
		try {
			writer.write("\n\r".getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String receiveContent() throws Exception {
		return readBuffer();
	}

	private String readBuffer() throws Exception {
		StringBuilder str = new StringBuilder();
		String line;
		
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty())
					break;
				str.append(line);
			}

			while (reader.ready()) {
				int ch = reader.read();
				str.append((char) ch);
			}
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
		System.out.println("Closing connection with :" + client.getInetAddress() + ":" + client.getLocalPort());
		isRunning = false;
		try {
			reader.close();
			writer.close();
			client.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}