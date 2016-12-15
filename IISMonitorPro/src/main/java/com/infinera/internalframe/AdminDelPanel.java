package com.infinera.internalframe;

import java.awt.EventQueue;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.infinera.dao.AdminDao;
import com.infinera.model.Admin;

import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminDelPanel extends JPanel {
	private JTable table;
	private JTextField nameField;
	private JTextField passwordField;
	private JTextField emailField;
	private JTextField idField;
	private JButton delButton;
	private JButton saveButton;
	private DefaultTableModel dftm;
	private String[] columnNames;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminDelPanel frame = new AdminDelPanel();
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
	public AdminDelPanel() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, new GBC(0, 0, 13, 8).setWeight(1300, 800).setInsets(1).setFill(GridBagConstraints.BOTH));

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		dftm = (DefaultTableModel) table.getModel();
		columnNames = new String[] { "ID", "Name", "Password", "Email" };
		dftm.setColumnIdentifiers(columnNames);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowNum = table.getSelectedRow();
				int id = Integer.parseInt((String) table.getValueAt(rowNum, 0));
				if (id == 1) {
					delButton.setEnabled(false);
				} else {
					delButton.setEnabled(true);
				}

				idField.setText((String) table.getValueAt(rowNum, 0));
				nameField.setText((String) table.getValueAt(rowNum, 1));
				passwordField.setText((String) table.getValueAt(rowNum, 2));
				emailField.setText((String) table.getValueAt(rowNum, 3));
			}
		});
		scrollPane.setViewportView(table);

		JLabel idLabel = new JLabel("ID");
		add(idLabel, new GBC(0, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		idField = new JTextField();
		idField.setEditable(false);
		add(idField, new GBC(1, 9, 2, 1).setWeight(200, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel nameLabel = new JLabel("Name");
		add(nameLabel, new GBC(3, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		nameField = new JTextField();
		add(nameField, new GBC(4, 9, 2, 1).setWeight(200, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel passwordLabel = new JLabel("password");
		add(passwordLabel, new GBC(6, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		passwordField = new JTextField();
		add(passwordField, new GBC(7, 9, 2, 1).setWeight(200, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel emailLabel = new JLabel("EMail");
		add(emailLabel, new GBC(9, 9, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.CENTER));

		emailField = new JTextField();
		add(emailField, new GBC(10, 9, 2, 1).setWeight(200, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL)
				.setAnchor(GridBagConstraints.EAST));

		delButton = new JButton("Del");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idField.getText().length() != 0) {
					int id = Integer.parseInt(idField.getText());
					if (id != 1) {
						AdminDao.delete(Integer.parseInt(idField.getText()));
						initTable();
					}
				}
			}
		});
		add(delButton, new GBC(3, 10, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		saveButton = new JButton("Save");
		add(saveButton, new GBC(9, 10, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));
		saveButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void initTable() {
		String[] rowData = new String[4];
		dftm.setDataVector(null, columnNames);

		List<Admin> list = new AdminDao().findAll();

		for (Admin admin : list) {
			rowData[0] = new Integer(admin.getId()).toString();
			rowData[1] = admin.getName();
			rowData[2] = admin.getPassword();
			rowData[3] = admin.getEmail();
			dftm.addRow(rowData);
		}
		setVisible(true);
	}

}
