package com.infinera.internalframe;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.apache.commons.validator.routines.EmailValidator;

import com.infinera.dao.AdminDao;
import com.infinera.model.Admin;;

public class AdminAddPanel extends JPanel {
	private JTextField usernameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField password2Field;

	/**
	 * Create the panel.
	 */
	public AdminAddPanel() {
		super();
		// setBounds(0, 0, 1000, 800);
		// GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(new GridBagLayout());

		// GridBagLayout gridBagLayout = new GridBagLayout();
		// gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		// gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		// 0};
		// gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0,
		// 0.0, 1.0, Double.MIN_VALUE};
		// gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
		// 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(new GridBagLayout());

		JLabel usernameLabel = new JLabel("User Name");
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.gridwidth = 2;
		gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLabel.gridx = 1;
		gbc_usernameLabel.gridy = 1;
		add(usernameLabel, gbc_usernameLabel);

		usernameField = new JTextField();
		usernameField.setToolTipText("User Name, not leng than 30 characters ");
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.gridwidth = 3;
		gbc_usernameField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 3;
		gbc_usernameField.gridy = 1;
		add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);

		JLabel emailLable = new JLabel("Email");
		GridBagConstraints gbc_emailLable = new GridBagConstraints();
		gbc_emailLable.gridwidth = 2;
		gbc_emailLable.insets = new Insets(0, 0, 5, 5);
		gbc_emailLable.gridx = 1;
		gbc_emailLable.gridy = 3;
		add(emailLable, gbc_emailLable);

		emailField = new JTextField();
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.gridwidth = 3;
		gbc_emailField.insets = new Insets(0, 0, 5, 5);
		gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailField.gridx = 3;
		gbc_emailField.gridy = 3;
		add(emailField, gbc_emailField);
		emailField.setColumns(10);

		JLabel passwordLabel = new JLabel("Password");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.gridwidth = 2;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 5;
		add(passwordLabel, gbc_passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("at least 6 characters");
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 3;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 3;
		gbc_passwordField.gridy = 5;
		add(passwordField, gbc_passwordField);

		JLabel password2Label = new JLabel("Confirm Password");
		GridBagConstraints gbc_password2Label = new GridBagConstraints();
		gbc_password2Label.gridwidth = 2;
		gbc_password2Label.insets = new Insets(0, 0, 5, 5);
		gbc_password2Label.gridx = 1;
		gbc_password2Label.gridy = 7;
		add(password2Label, gbc_password2Label);

		password2Field = new JPasswordField();
		password2Field.setToolTipText("At least 6 charactors");
		GridBagConstraints gbc_password2Field = new GridBagConstraints();
		gbc_password2Field.gridwidth = 3;
		gbc_password2Field.insets = new Insets(0, 0, 5, 5);
		gbc_password2Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_password2Field.gridx = 3;
		gbc_password2Field.gridy = 7;
		add(password2Field, gbc_password2Field);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isInputValid()) {
					saveUserInput();
				}
			}
		});
		GridBagConstraints gbc_addButton = new GridBagConstraints();
		gbc_addButton.insets = new Insets(0, 0, 0, 20);
		gbc_addButton.gridx = 3;
		gbc_addButton.gridy = 9;
		add(addButton, gbc_addButton);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetInput();
			}
		});
		GridBagConstraints gbc_resetButton = new GridBagConstraints();
		gbc_resetButton.insets = new Insets(0, 0, 0, 5);
		gbc_resetButton.gridx = 5;
		gbc_resetButton.gridy = 9;
		add(resetButton, gbc_resetButton);
	}

	public boolean isInputValid() {
		String username = usernameField.getText();
		if (username.length() == 0) {
			JOptionPane.showMessageDialog(null, "Username can't be null");
			return false;
		}

		String email = emailField.getText();
		if (!EmailValidator.getInstance().isValid(email)) {
			JOptionPane.showMessageDialog(null, "Invalid Email addresss");
			return false;
		}

		String password1 = passwordField.getText();
		String password2 = password2Field.getText();

		if (password1.length() == 0 || password2.length() == 0 || password1.length() < 6 || password2.length() < 6) {
			JOptionPane.showMessageDialog(null, "Password has at least 6 characters");
			return false;
		}

		return true;
	}
	
	public void saveUserInput() {
		Admin admin = new Admin();
		admin.setName(usernameField.getText());
		admin.setEmail(emailField.getText());
		admin.setPassword(passwordField.getText());
		
		AdminDao.save(admin);
		
		resetInput();
		
		JOptionPane.showMessageDialog(null, "Add administrator successfully");
	}
	
	public void resetInput() {
		usernameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		password2Field.setText("");
	}
}
