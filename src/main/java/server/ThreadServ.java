package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ThreadServ implements Runnable {

	private Socket client;
	private PrintStream writer = null;
	private BufferedReader reader = null;

	public ThreadServ(Socket ClientSock) {
		client = ClientSock;
	}

	public void run() {
		while (true) {
			String msg = recevoirMsg();
			if (!msg.isEmpty()) {
				System.out.println(msg);
				sendContent("got you !".getBytes(StandardCharsets.UTF_8));
			}
		}
	}

	private void sendContent(byte[] content) {
		try {
			writer = new PrintStream(client.getOutputStream());
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
	}

	private String recevoirMsg() {
		try {
			return lireBuffer(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String lireBuffer(InputStream stream) throws IOException {
		String msg = "", ligne;
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while ((ligne = reader.readLine()) != null) {
			if (ligne.isEmpty())
				break;
			msg += ligne + "\n";
		}

		while (reader.ready())
			msg += (char) reader.read();
		return msg;
	}

}