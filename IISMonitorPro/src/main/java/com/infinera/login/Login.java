package com.infinera.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.infinera.dao.AdminDao;
import com.infinera.frames.MainFrame;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame loginFrame;
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		loginFrame.setTitle("Infinera Intelligent Monitor");
		loginFrame.setResizable(false);
		loginFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("camera.png")));
		loginFrame.setBounds(100, 100, 474, 285);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.getContentPane().setLayout(null);

		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(205, 67, 85, 21);
		loginFrame.getContentPane().add(lblUserName);

		usernameField = new JTextField();
		usernameField.setToolTipText("Input user name");
		usernameField.setBounds(300, 67, 112, 20);
		loginFrame.getContentPane().add(usernameField);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(205, 118, 78, 17);
		loginFrame.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Input password");
		passwordField.setBounds(300, 116, 112, 20);
		loginFrame.getContentPane().add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminDao dao = new AdminDao();
				if (dao.isValidUser(usernameField.getText(), passwordField.getText())) {
					loginFrame.dispose();
					
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else {
					JOptionPane.showMessageDialog(null, "Wrong username or password, input and try again!");
				}
			}
		});
		btnLogin.setToolTipText("Login with user name and password");
		btnLogin.setBounds(205, 165, 89, 23);
		loginFrame.getContentPane().add(btnLogin);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
			}
		});
		btnReset.setToolTipText("Reset user name and password input");
		btnReset.setBounds(323, 165, 89, 23);
		loginFrame.getContentPane().add(btnReset);

		JLabel loginLabel = new JLabel("");
		loginLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("login.png")));
		loginLabel.setBounds(28, 40, 149, 161);
		loginFrame.getContentPane().add(loginLabel);
	}
}
