import java.net.Socket;

public class User {
	private Socket socket;
	private String username;
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket s) {
		this.socket = s;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String u) {
		this.username = u;
	}
	
}
