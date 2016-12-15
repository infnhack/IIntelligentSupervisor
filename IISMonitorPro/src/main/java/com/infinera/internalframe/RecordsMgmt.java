package com.infinera.internalframe;

import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.GridBagConstraints;
import javax.swing.JComboBox;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.infinera.dao.AccessRecordDao;
import com.infinera.dao.UserDao;
import com.infinera.model.AccessRecord;
import com.infinera.model.User;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class RecordsMgmt extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					JFrame frame = new JFrame("Test AdminMgmt");

					desktopPane = new JDesktopPane();
					frame.getContentPane().add(desktopPane);
					RecordsMgmt internalFrame = new RecordsMgmt();
					internalFrame.setVisible(true);
					desktopPane.add(internalFrame);

					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private JDesktopPane desktopPane;
		});
	}

	/**
	 * Create the frame.
	 */
	public RecordsMgmt() {
		setBounds(100, 100, 900, 400);
		GridBagLayout gridBagLayout = new GridBagLayout();
		// gridBagLayout.rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0,
		// 1.0, 1.0, 1.0, 1.0, 1.0};
		getContentPane().setLayout(gridBagLayout);

		JLabel userLabel = new JLabel("User Name");
		getContentPane().add(userLabel,
				new GBC(0, 0, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		userComboBox = new JComboBox<String>();
		initUserComboBox();
		getContentPane().add(userComboBox,
				new GBC(1, 0, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));


		JLabel legalLabel = new JLabel("Legal Access");
		getContentPane().add(legalLabel,
				new GBC(0, 2, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		String[] legalItems = new String[] { "Both", "Yes", "No" };
		legaComboBox = new JComboBox<String>(legalItems);
		// legaComboBox.add
		// legaComboBox.setSelectedIndex(-1);

		getContentPane().add(legaComboBox,
				new GBC(1, 2, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel dateBeginLabel = new JLabel("Date Begin");
		getContentPane().add(dateBeginLabel,
				new GBC(0, 4, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		UtilDateModel beginModel = new UtilDateModel();
		JDatePanelImpl beginDatePanel = new JDatePanelImpl(beginModel);
		beginDatePicker = new JDatePickerImpl(beginDatePanel);
		getContentPane().add(beginDatePicker,
				new GBC(1, 4, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JLabel dateEndLabel = new JLabel("Date End");
		getContentPane().add(dateEndLabel,
				new GBC(0, 6, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		UtilDateModel endModel = new UtilDateModel();
		JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel);
		endDatePicker = new JDatePickerImpl(endDatePanel);
//		endDatePicker.
		getContentPane().add(endDatePicker,
				new GBC(1, 6, 1, 1).setWeight(100, 100).setInsets(1).setFill(GridBagConstraints.HORIZONTAL));

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane,
				new GBC(2, 0, 7, 8).setWeight(700, 800).setInsets(1).setFill(GridBagConstraints.BOTH));

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm = (DefaultTableModel) table.getModel();
		dtm.setColumnIdentifiers(columnNames);
		scrollPane.setViewportView(table);
		initTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowNum = table.getSelectedRow();
				
				photoLabel.setVerticalAlignment(SwingConstants.CENTER);
				photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
				photoLabel.setSize(photoWidth, photoHeight);
//				// ImageIcon("D:\\Debug\\2016-11-25-12-30-59-20.jpg"));
				photoLabel.setText("<html><body><image width='" + photoWidth + "' height='" + photoHeight
						+ "' src=file:/" + (String)table.getValueAt(rowNum, 4) + "'></img></body></html>");
			}
		});

		photoLabel = new JLabel("New label");
		photoLabel.setVerticalAlignment(SwingConstants.CENTER);
		photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(photoLabel,
				new GBC(9, 0, 1, 8).setInsets(20).setWeight(100, 800).setFill(GridBagConstraints.BOTH));

		queryButton = new JButton("Query");
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryTable();
			}
		});
		getContentPane().add(queryButton,
				new GBC(3, 9, 1, 1).setWeight(100, 100).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));

		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		getContentPane().add(refreshButton,
				new GBC(5, 9, 1, 1).setWeight(100, 100).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowNum = table.getSelectedRow();
				if (rowNum == -1) {
					JOptionPane.showMessageDialog(null, "No row is selected");
					return;
				}

				int recordId = Integer.parseInt((String) table.getValueAt(rowNum, 0));
				
				AccessRecordDao dao = new AccessRecordDao();
				dao.delete(dao.getById(recordId));
				
				queryTable();

			}
		});
		getContentPane().add(deleteButton,
				new GBC(7, 9, 1, 1).setWeight(100, 100).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.HORIZONTAL));

	}

	public void initTable() {
		String[] rowData = new String[5];
		UserDao userDao = new UserDao();
		AccessRecordDao accessRecordDao = new AccessRecordDao();

		dtm.setDataVector(null, columnNames);

		List<AccessRecord> list = accessRecordDao.findAll();

		for (AccessRecord ar : list) {
			rowData[0] = new Integer(ar.getId()).toString();
			rowData[1] = userDao.getById(ar.getUserId()).getName();
			rowData[2] = ar.getIsLegal() == 0 ? "No" : "Yes";
			rowData[3] = ar.getTimestamp().toString();
			rowData[4] = ar.getPhotoPath();
			dtm.addRow(rowData);
		}
		setVisible(true);
	}
	
	public void queryTable() {
		Date beginDate = (Date) beginDatePicker.getModel().getValue();
		Date endDate = (Date) endDatePicker.getModel().getValue();
		String name = (String)userComboBox.getSelectedItem();
		String legal = (String)legaComboBox.getSelectedItem();
		String[] rowData = new String[5];
		UserDao userDao = new UserDao();
		AccessRecordDao accessRecordDao = new AccessRecordDao();

		dtm.setDataVector(null, columnNames);

		List<AccessRecord> list = accessRecordDao.findByCriteria(userDao.getIdByName(name), legal, beginDate, endDate);

		for (AccessRecord ar : list) {
			rowData[0] = new Integer(ar.getId()).toString();
			rowData[1] = userDao.getById(ar.getUserId()).getName();
			rowData[2] = ar.getIsLegal() == 0 ? "No" : "Yes";
			rowData[3] = ar.getTimestamp().toString();
			rowData[4] = ar.getPhotoPath();
			dtm.addRow(rowData);
		}
		setVisible(true);
	}
	
	public void initUserComboBox() {
//		userComboBox.removeAll();
		userComboBox.removeAllItems();
		
		
		userComboBox.addItem("All Users");
		List<User> userList = new UserDao().findAll();
		for (User user : userList) {
			userComboBox.addItem(user.getName());
		}
		
		userComboBox.setSelectedIndex(0);
	}

	public void refresh() {
		legaComboBox.setSelectedIndex(0);
		
		initUserComboBox();
		
		//reload table
		initTable();
		
		beginDatePicker.getModel().setSelected(false);
		endDatePicker.getModel().setSelected(false);
		

	}

	private JButton deleteButton;
	private JButton refreshButton;
	private JButton queryButton;
	
	private JDatePickerImpl beginDatePicker;
	private JDatePickerImpl endDatePicker;

	private final int photoWidth = 200;
	private final int photoHeight = 300;
	private JLabel photoLabel;
	private JTable table;
	private JComboBox<String> legaComboBox;
	private JComboBox<String> userComboBox;
	private DefaultTableModel dtm;
	private String[] columnNames = new String[] { "ID", "User Name", "Is Legal", "Access Date", "Photo Path" };
}
