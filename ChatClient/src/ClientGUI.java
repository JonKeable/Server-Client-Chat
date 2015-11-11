import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class ClientGUI {

	public static JFrame frmMainWindow;
	private static JTextField chatField;
	private static JScrollPane scrollPane;
	public static JTextPane textPane;
	private static JLabel lblConnectedUsers;
	private static JScrollPane scrollPane_1;
	private static JLabel lblYourName;
	private static JEditorPane editorPane;
	private static JButton btnConnectButton;
	private static JButton btnDisconnectButton;
	private static JList<String> list;
	
	private static Scanner in;
	private static PrintWriter out;
	private static Socket soc;
	private static String username = "unknown user";
	private static ChatClient client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frmMainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void connect(String portStr, String host, String userStr) throws IOException {
		try{
			int port = Integer.parseInt(portStr);
			if(portStr.equals("")) {
				port = 5077;
			}
			else {
				port = Integer.parseInt(portStr);
			}
			
			if(host.equals("")) host = "localhost"; 
			username = userStr;
			
			Socket soc = new Socket(host, port);
			displayMsg("you Connected to: " + host);
			
			client = new ChatClient(soc, username);
			Thread t = new Thread(client);
			t.start();
			
			out = new PrintWriter(soc.getOutputStream());
			out.println(username);
			out.flush();
		}
		catch(Exception e) {
			displayMsg("Could not connect to server!");
			try{
				for(int i = 0; i<5; i++) {
					displayMsg("Exiting in " + (5-i) + " seconds");
					TimeUnit.SECONDS.sleep(1);
				}
			}
			catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	public static void initialize() {
		frmMainWindow = new JFrame();
		frmMainWindow.setTitle("chat window");
		frmMainWindow.setResizable(false);
		frmMainWindow.setBounds(100, 100, 456, 462);
		frmMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainWindow.getContentPane().setLayout(null);
		
		chatField = new JTextField();
		chatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.send(chatField.getText());
				chatField.setText("");
				chatField.requestFocusInWindow();
			}
		});
		chatField.setBounds(10, 349, 231, 26);
		frmMainWindow.getContentPane().add(chatField);
		chatField.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(292, 266, 127, 146);
		frmMainWindow.getContentPane().add(scrollPane);
		
		list = new JList<String>();
		scrollPane.setViewportView(list);
		
		btnConnectButton = new JButton("Connect");
		btnConnectButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectDialogue conDlg = new ConnectDialogue();
				conDlg.setVisible(true);
			}
		});
		btnConnectButton.setBounds(292, 11, 127, 38);
		frmMainWindow.getContentPane().add(btnConnectButton);
		
		JButton btnSendButton = new JButton("Send Message");
		btnSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.send(chatField.getText());
				chatField.setText("");
				chatField.requestFocusInWindow();
			}
		});
		btnSendButton.setBounds(10, 386, 231, 26);
		frmMainWindow.getContentPane().add(btnSendButton);
		
		btnDisconnectButton = new JButton("Disconnect");
		btnDisconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.disconnect();
			}
		});
		btnDisconnectButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDisconnectButton.setBounds(292, 60, 127, 38);
		btnDisconnectButton.setEnabled(false);
		frmMainWindow.getContentPane().add(btnDisconnectButton);
		
		textPane = new JTextPane();
		textPane.setToolTipText("will be appended to your messages");
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textPane.setEditable(false);
		textPane.setBounds(292, 180, 127, 38);
		frmMainWindow.getContentPane().add(textPane);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 231, 327);
		frmMainWindow.getContentPane().add(scrollPane_1);
		
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane_1.setViewportView(editorPane);
		
		lblYourName = new JLabel("Your Name");
		lblYourName.setOpaque(true);
		lblYourName.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblYourName.setBackground(new Color(173, 216, 230));
		lblYourName.setBounds(292, 155, 127, 26);
		frmMainWindow.getContentPane().add(lblYourName);
		
		lblConnectedUsers = new JLabel("Connected Users");
		lblConnectedUsers.setBounds(292, 240, 127, 25);
		frmMainWindow.getContentPane().add(lblConnectedUsers);
		lblConnectedUsers.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnectedUsers.setOpaque(true);
		lblConnectedUsers.setBackground(new Color(173, 216, 230));
		lblConnectedUsers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		displayMsg("Welcome");
		
	}
	
	public static void displayMsg(String msg) {
		try {
			Document doc = editorPane.getDocument();
			doc.insertString(doc.getLength(), (msg + "\n"), null);
			editorPane.validate();
			editorPane.repaint();
		}
		catch(BadLocationException ble) {
			ble.printStackTrace();
		}
	}
	
	public static void switchEnabledButtons() {
		if(btnConnectButton.isEnabled()) {
			btnConnectButton.setEnabled(false);
			btnDisconnectButton.setEnabled(true);
		}
		else {
			btnConnectButton.setEnabled(true);
			btnDisconnectButton.setEnabled(false);
		}
			
	}
	
	public static void updateUsers(String[] userStrArray) {
		list.setListData(userStrArray);
	}
	
	public static void clearUsers() {
		String[] blank = new String[0];
		list.setListData(blank);
	}
}
