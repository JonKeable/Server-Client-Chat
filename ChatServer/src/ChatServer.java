import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

	private static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
    public static ArrayList<Socket> getConnectedSockets() {
		return connectedSockets;
	}
	//private static ArrayList<User> connectedUsers = new ArrayList<User>();
	private static ArrayList<String> connectedUsers = new ArrayList<String>();
	
	//public static ArrayList<User> getConnectedUsers() {
		//return connectedUsers;
	//}
	
	public static void main(String[] args) throws IOException {
		try {
			int port = 5077;
			ServerSocket serverSoc = new ServerSocket(port);
			System.out.println("Waiting for clients to connect ...");
		
			while(true) {
	            Socket soc = serverSoc.accept();
	            connectedSockets.add(soc);
	            
	            System.out.println("Client connected from: " + soc.getLocalAddress().getHostName());
	            addUsername(soc);
	            
	            ServerOutput chatOut = new ServerOutput(soc);
	            Thread t = new Thread(chatOut);
	            t.start();    
			}
		}
		catch(Exception e) {
			System.err.println("An error occured:	" + e);
			e.printStackTrace();
		}
	}
	private static void addUsername(Socket s) throws IOException{
		try {
			boolean nameSet = false;
			String username = "";
			Scanner in = new Scanner(s.getInputStream());
			while(!nameSet) {
				if(in.hasNext()) {
					username = in.nextLine();
					nameSet = true;
				}
			}
			//System.out.println(username);
			connectedUsers.add(username);
			sendUserList();
		}
		catch(Exception e) {
			System.err.println("An error occured trying to add a username!");
			e.printStackTrace();
		}
	}
	
	private static void sendUserList() {
		String usersStr = "";
		for(String u: connectedUsers) {
			usersStr = usersStr.concat("&");
			usersStr = usersStr.concat(u);
		}
			
		//System.out.println("Connected users: " + usersStr);
		String outMsg = "$$$usrs".concat(usersStr);
		System.out.println("Sending UserList:  " + outMsg);
			
		outputMsgToSockets(outMsg);
	}	
	
	private static void removeUser(String user) throws IOException {
		connectedUsers.remove(user);
		sendUserList();
	}
	
	public static void cmd(String cmd) throws IOException {
		String id = cmd.substring(0, 4);
		String content = cmd.substring(4);
		switch(id) {
		case "rmUs" : removeUser(content);
		break;
		}
		
	}
	
	public static void outputMsgToSockets(String msg) {
		try{
			for(Socket s : ChatServer.getConnectedSockets()) {
				PrintWriter sout = new PrintWriter(s.getOutputStream());
				sout.println(msg);
				sout.flush();
				System.out.println("Message sent to: " + s.getLocalAddress().getHostName());
		
			}
		}
		catch(IOException ioE) {
			System.out.println("An error occured trying to seng a message to a client");
			ioE.printStackTrace();
		}
	}	
}
