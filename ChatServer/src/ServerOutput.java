import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerOutput implements Runnable {
	
	private Socket soc;
	
	public ServerOutput(Socket s) {
		soc = s;
	}
	
	private boolean isSocketConnected() throws IOException{
		try {
			if(!soc.isConnected()) {
				
				ChatServer.getConnectedSockets().remove(soc);
				
				for(Socket s : ChatServer.getConnectedSockets()) {
					PrintWriter out =new PrintWriter(s.getOutputStream());
					out.println(soc.getLocalAddress().getHostName() + " disconneted!");
					out.flush();
					System.out.println(soc.getLocalAddress().getHostName() + " disconnected!");
				}
				
				return false;
			}
			else return true;
		}	
		catch(Exception e) {
			System.err.println("An error occured whilst trying to remove a socket from the array");
			e.printStackTrace();
			return false;
		}
	}
	
	public void run() {
		String msg;
		Scanner in;
		PrintWriter out;
		try {
			in = new Scanner(soc.getInputStream());
			out = new PrintWriter(soc.getOutputStream());
				
			while(true) {
				if(isSocketConnected()) {
					if(in.hasNext()) {
						msg = in.nextLine();
						System.out.println("incomming message: " + msg);
						
						if(!msg.contains("$$$")) {
							ChatServer.outputMsgToSockets(msg);
						}
						else {
							String cmd = msg.substring(3);
							ChatServer.cmd(cmd);
						}
					}
				}
			}
		}
		catch(Exception e) {
			System.err.println("An error occured: " + e);
			e.printStackTrace();
		}
		finally {
				try {
					soc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}		
		}
	}
	

}
