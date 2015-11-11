import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ConnectDialogue extends JFrame {

	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPort;
	private JLabel lblServerPort;
	private JTextField txtHost;
	private JLabel lblHostName;
	private JButton btnConnectDlgButton;
	private JButton btnCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectDialogue frame = new ConnectDialogue();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectDialogue() {
		setTitle("Connect to Server");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 361, 193);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setForeground(Color.GRAY);
		txtUser.setText("user");
		txtUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtFocusGain(txtUser);
			}
			@Override
			public void focusLost(FocusEvent e) {
				txtFocusLost(txtUser, "user");
			}
		});
		txtUser.setBounds(10, 40, 155, 29);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setForeground(Color.GRAY);
		txtPort.setText("5077");
		txtPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtFocusGain(txtPort);
			}
			@Override
			public void focusLost(FocusEvent e) {
				txtFocusLost(txtPort, "5077");
			}
		});
		
		txtPort.setColumns(10);
		txtPort.setBounds(180, 40, 155, 29);
		contentPane.add(txtPort);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsername.setBackground(new Color(173, 216, 230));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(10, 11, 155, 29);
		lblUsername.setOpaque(true);
		contentPane.add(lblUsername);
		
		lblServerPort = new JLabel("Server Port");
		lblServerPort.setOpaque(true);
		lblServerPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblServerPort.setBackground(new Color(173, 216, 230));
		lblServerPort.setBounds(180, 11, 155, 29);
		contentPane.add(lblServerPort);
		
		txtHost = new JTextField();
		txtHost.setForeground(Color.GRAY);
		txtHost.setText("localhost");
		txtHost.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtFocusGain(txtHost);
			}
			@Override
			public void focusLost(FocusEvent e) {
				txtFocusLost(txtHost, "localhost");
			}
		});
		txtHost.setColumns(10);
		txtHost.setBounds(10, 114, 155, 29);
		contentPane.add(txtHost);
		
		lblHostName = new JLabel("Host Name");
		lblHostName.setOpaque(true);
		lblHostName.setHorizontalAlignment(SwingConstants.CENTER);
		lblHostName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHostName.setBackground(new Color(173, 216, 230));
		lblHostName.setBounds(10, 87, 155, 29);
		contentPane.add(lblHostName);
		
		btnConnectDlgButton = new JButton("Connect");
		btnConnectDlgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				try {
					ClientGUI.connect(txtPort.getText(), txtHost.getText(), txtUser.getText());
					ClientGUI.switchEnabledButtons();
					ClientGUI.frmMainWindow.setTitle(txtUser.getText() + "'s chat");
					ClientGUI.textPane.setText(txtUser.getText());
					dispose();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnConnectDlgButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConnectDlgButton.setBounds(177, 87, 158, 29);
		contentPane.add(btnConnectDlgButton);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.setBounds(177, 112, 158, 29);
		contentPane.add(btnCancel);
	}
	
	private void txtFocusGain(JTextField txt) {
		if(txt.getText().equals("") || txt.getForeground().equals(Color.GRAY)) {
			txt.setText("");
			txt.setForeground(Color.BLACK);
		}
	}
		
	private void txtFocusLost(JTextField txt, String str) {
		if(txt.getText().equals("")) {
			txt.setText(str);
			txt.setForeground(Color.GRAY);
		}
	}
}
