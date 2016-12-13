package com.infinera.internalframe;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class AdminAddPanelTest {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				JFrame frame = new JFrame("Test");
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				frame.add(new AdminAddPanel());
			}
		});
	}
}
