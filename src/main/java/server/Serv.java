package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Serv implements Runnable {

	private ServerSocket server = null;
	private boolean isRunning = true;

	public Serv(int port, String adresse) {

		try {
			server = new ServerSocket(port, 100,
					InetAddress.getByName(adresse)); // 100 nombre de connections en attente max
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("server started on " + server.getInetAddress() + ":" + server.getLocalPort());

	}

	public void close() {
		isRunning = false;
	}

	@Override
	public void run() {
		while (isRunning) {

			try {
				Socket client = server.accept();

				System.out
						.println("Connexion avec : " + client.getInetAddress() + " sur le port : " + client.getPort());
				Thread t = new Thread(new Client(client));
				t.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
			server = null;
		}
	}
}