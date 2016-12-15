package com.infinera.internalframe;

import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UserMgmt extends JInternalFrame {

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
					UserMgmt internalFrame = new UserMgmt();
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
	public UserMgmt() {
		setTitle("Admin Management");
		setIconifiable(true);
		setClosable(true);
		setBounds(0, 0, 600, 400);
//		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 424, 260);
		getContentPane().add(tabbedPane);
		
		final UserAddPanel addPanel = new UserAddPanel();
		final UserDelPanel delPanel = new UserDelPanel();
		tabbedPane.addTab("Add user", null, addPanel, "Add user");
		tabbedPane.addTab("Modify/Delete user", null, delPanel, "Modify/Delete user");
		tabbedPane.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				delPanel.initTable();
			}
		});
		
//		pack();
		setVisible(true);

	}

}
