package com.infinera.internalframe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.validator.routines.EmailValidator;

import com.infinera.dao.UserDao;
import com.infinera.model.User;

public class UserDelPanel extends JPanel {
	private JTable table;
	private JTextField nameField;
	private JTextField emailField;
	private JTextField idField;
	private JButton delButton;
	private JButton saveButton;
	private DefaultTableModel dftm;
	private String[] columnNames = new String[] { "ID", "Name", "Email" };;

	/**
	 * Create the panel.
	 */
	public UserDelPanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, new GBC(0, 0, 13, 8).setWeight(1300, 800).setInsets(1).setFill(GridBagConstraints.BOTH));

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		dftm = (DefaultTableModel) table.getModel();
		// columnNames = new String[] { "ID", "Name", "Password", "Email" };
		dftm.setColumnIdentifiers(columnNames);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowNum = table.getSelectedRow();
				// int id = Integer.parseInt((String) table.getValueAt(rowNum,
				// 0));
				// if (id == 1) {
				// delButton.setEnabled(false);
				// } else {
				// delButton.setEnabled(true);
				// }

				idField.setText((String) table.getValueAt(rowNum, 0));
				nameField.setText((String) table.getValueAt(rowNum, 1));
				emailField.setText((String) table.getValueAt(rowNum, 2));
			}
		});
		scrollPane.setViewportView(table);

		JLabel idLabel = new JLabel("ID");
		add(idLabel, new GBC(0, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		idField = new JTextField();
		idField.setEditable(false);
		add(idField, new GBC(1, 9, 3, 1).setWeight(300, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel nameLabel = new JLabel("Name");
		add(nameLabel, new GBC(4, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		nameField = new JTextField();
		add(nameField, new GBC(5, 9, 2, 1).setWeight(300, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel emailLabel = new JLabel("EMail");
		add(emailLabel, new GBC(8, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		emailField = new JTextField();
		add(emailField, new GBC(9, 9, 2, 1).setWeight(300, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL)
				.setAnchor(GridBagConstraints.EAST));

		delButton = new JButton("Del");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idField.getText().length() != 0) {

					int id = Integer.parseInt(idField.getText());
					UserDao userDao = new UserDao();
					userDao.delete(userDao.getById(id));
					initTable();
				}
			}
		});
		add(delButton, new GBC(4, 10, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		saveButton = new JButton("Save");
		add(saveButton, new GBC(8, 10, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));
		saveButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isInputValid()) {
					User user = new User(Integer.parseInt(idField.getText()), nameField.getText(),
							emailField.getText());
					new UserDao().update(user);
					
					initTable();
				}
			}
		});
	}

	public boolean isInputValid() {
		String username = nameField.getText();
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

	public void initTable() {
		String[] rowData = new String[3];
		dftm.setDataVector(null, columnNames);

		UserDao userDao = new UserDao();

		List<User> list = userDao.findAll();

		for (User user : list) {
			rowData[0] = new Integer(user.getId()).toString();
			rowData[1] = user.getName();
			rowData[2] = user.getEmail();
			dftm.addRow(rowData);
		}
		setVisible(true);
	}

}
