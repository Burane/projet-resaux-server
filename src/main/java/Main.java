import server.Serv;

public class Main {

	public static void main(String[] args) {
		Thread server = new Thread(new Serv(42345, "127.0.0.1"));
		server.start();
	}

}