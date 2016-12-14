package com.infinera.internalframe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.validator.routines.EmailValidator;

import com.infinera.dao.UserDao;
import com.infinera.model.User;

public class UserAddPanel extends JPanel {
	private JTextField usernameField;
	private JTextField emailField;

	/**
	 * Create the panel.
	 */
	public UserAddPanel() {
		super();

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
		gbc_addButton.gridy = 5;
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
		gbc_resetButton.gridy = 5;
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

		return true;
	}

	public void saveUserInput() {
		User user = new User(usernameField.getText(), emailField.getText());
		// user.setName(usernameField.getText());
		// user.setEmail(emailField.getText());
		//
		UserDao userDao = new UserDao();
		userDao.create(user);

		resetInput();

		JOptionPane.showMessageDialog(null, "Add user successfully");
	}

	public void resetInput() {
		usernameField.setText("");
		emailField.setText("");
	}

}
