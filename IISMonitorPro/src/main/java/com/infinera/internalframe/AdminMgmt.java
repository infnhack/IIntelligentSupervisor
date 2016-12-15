package com.infinera.internalframe;

import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdminMgmt extends JInternalFrame {

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
					AdminMgmt internalFrame = new AdminMgmt();
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
	public AdminMgmt() {
		setTitle("Admin Management");
		setIconifiable(true);
		setClosable(true);
		setBounds(0, 0, 600, 400);
//		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 424, 260);
		getContentPane().add(tabbedPane);
		
		final AdminAddPanel addPanel = new AdminAddPanel();
		final AdminDelPanel delPanel = new AdminDelPanel();
		tabbedPane.addTab("Add administrator", null, addPanel, "Add administrator");
		tabbedPane.addTab("Delete adminstrator", null, delPanel, "Delete administrator");
		tabbedPane.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				delPanel.initTable();
			}
		});
		
//		pack();
		setVisible(true);

	}

}
