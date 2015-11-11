import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ChatClient implements Runnable {

	private Socket soc;
	private String username;
	private Scanner in;
	private PrintWriter out;

	public ChatClient(Socket s, String usr) {
		soc = s;
		username = usr;
	}
	
	public void run() {

		try {
			in = new Scanner(soc.getInputStream());
			out = new PrintWriter(soc.getOutputStream());
			out.flush();
			
			String concUname = "$$$uname".concat(username);
			//ClientGUI.displayMsg("Uname sent: " + concUname);
			out.println(concUname);
			out.flush();
			
			out.println("user connected: " + username);
			out.flush();
			
			while(true) {
				receive();
			}
		}
		catch(Exception e) {
			System.err.println("An error occured: " + e);
		}
		finally {
			try {
				soc.close();
			}
			catch(IOException e) {
				System.err.print("IOException thrown: " + e.getMessage());
			}
		}
	}
	
	private void receive() {
		
		String msg;
		if(in.hasNext()) {
			msg = in.nextLine();
			if(msg.contains("$$$")) {
				String cmd = msg.substring(3);
				chatCmd(cmd);
				
			}
			else {
				ClientGUI.displayMsg(msg);
			}
		}
	}
	

	
	public void disconnect() {
		try {
	        out.println(username + " has disconnected.");
	        out.flush();
	        
	        String outMsg = "$$$rmUs".concat(username);
	        out.println(outMsg);
	        out.flush();
	        
	        soc.close();
	        JOptionPane.showMessageDialog(null, "You disconnected");
	        
	        ClientGUI.switchEnabledButtons();
	        ClientGUI.clearUsers();
	        ClientGUI.textPane.setText("");
		}
		catch(Exception e) {
			System.err.println("An error occurred: " + e);
		}
		
	}
	

	
	public void send(String msg) {
		out.println(username + ":   " + msg);
		out.flush();
	}
	
	private void chatCmd(String cmd) {
		String id = cmd.substring(0, 4);
		//ClientGUI.displayMsg(id);
		String content = cmd.substring(4);
		//ClientGUI.displayMsg(content);
		switch(id) {
		case "usrs" : sendUsrs(content);
		break;
		}
	}
	
	private void sendUsrs(String usrs) {
				ClientGUI.updateUsers(usrs.split("&"));
	}
}
	
