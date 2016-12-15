package com.infinera.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JDesktopPane desktopPane;
	private JLabel backLabel;
    private Map<String, JInternalFrame> ifs = new HashMap<String, JInternalFrame>();

	
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Infinera Intelligent Monitor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("camera.png")));
		setBounds(100, 100, 800, 600);
		addComponentListener(new FrameListener());
		
		backLabel = new JLabel();
		backLabel.setVerticalAlignment(SwingConstants.TOP);
		backLabel.setHorizontalAlignment(SwingConstants.CENTER);
		updateBackgroudImage();
		
		desktopPane = new JDesktopPane();
		desktopPane.add(backLabel, new Integer(Integer.MIN_VALUE));
		
		getContentPane().add(desktopPane);
		getContentPane().add(createNavigationPanel(), BorderLayout.NORTH);
		
		setVisible(true);
		
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
	}
	
	private JTabbedPane createNavigationPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JPanel recordsPanel = new JPanel();
		recordsPanel.setLayout(new BoxLayout(recordsPanel,
                BoxLayout.X_AXIS));
		recordsPanel.add(createFrameButton("Record Management", "RecordsMgmt"));
		
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel,
                BoxLayout.X_AXIS));
		userPanel.add(createFrameButton("User Management", "UserMgmt"));
		
		JPanel systemPanel = new JPanel();
		systemPanel.setLayout(new BoxLayout(systemPanel,
                BoxLayout.X_AXIS));
		systemPanel.add(createFrameButton("Administrator Management", "AdminMgmt"));
		
//		JPanel userPanel1 = new UserMgmtPanel();
		
		tabbedPane.addTab("    Access Record Management", null, recordsPanel, "Access Record Management");
		tabbedPane.addTab("    User Management", null, userPanel, "User Management");
		tabbedPane.addTab("    System Management", null, systemPanel, "System information management");
		
		return tabbedPane;
	}
	
    // 窗体监听器
    private final class FrameListener extends ComponentAdapter {
        public void componentResized(final ComponentEvent e) {
        	updateBackgroudImage();
        }
    }
	
	private void updateBackgroudImage() {
		int width = this.getWidth();
		int height = this.getHeight();
		
		backLabel.setSize(this.getWidth(), this.getHeight());
        backLabel.setText("<html><body><image width='" + width
                + "' height='" + (height -100) + "' src="
                + getResourceUrl("background.jpg")
                + "'></img></body></html>");
	}
	
	public URL getResourceUrl(String name) {
		return this.getClass().getClassLoader().getResource(name);
	}
	
    /** *********************辅助方法************************* */
    // 为内部窗体添加Action的方法
    private JButton createFrameButton(String fName, String cname) {
    	Icon icon = new ImageIcon(getResourceUrl("Button-Blue.png"));
    	Icon iconRoll = new ImageIcon(getResourceUrl("Button-Green.png"));
    	Icon iconDown = new ImageIcon(getResourceUrl("Button-Grey.png"));
        Action action = new openFrameAction(fName, cname, icon);
        JButton button = new JButton(action);
        button.setText(fName);
        button.setMargin(new Insets(1, 1, 1, 1));
//        button.setHideActionText(true);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setContentAreaFilled(false);
        button.setRolloverIcon(iconRoll);
        button.setPressedIcon(iconDown);
        
        return button;
    }

    // 获取内部窗体的唯一实例对象
    private JInternalFrame getIFrame(String frameName) {
        JInternalFrame jf = null;
        if (!ifs.containsKey(frameName)) {
            try {
                Class fClass = Class.forName("com.infinera.internalframe." + frameName);
                Constructor constructor = fClass.getConstructor(null);
                jf = (JInternalFrame) constructor.newInstance(null);
                ifs.put(frameName, jf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            jf = ifs.get(frameName);
        return jf;
    }

    // 主窗体菜单项的单击事件监听器
    protected final class openFrameAction extends AbstractAction {
        private String frameName = null;

        private openFrameAction() {
        }

        public openFrameAction(String cname, String frameName, Icon icon) {
            this.frameName = frameName;
            putValue(Action.NAME, cname);
            putValue(Action.SHORT_DESCRIPTION, cname);
            putValue(Action.SMALL_ICON, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            JInternalFrame jf = getIFrame(frameName);
            // 在内部窗体闭关时，从内部窗体容器ifs对象中清除该窗体。
            jf.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent e) {
                    ifs.remove(frameName);
                }
            });
            if (jf.getDesktopPane() == null) {
                desktopPane.add(jf);
                jf.setVisible(true);
            }
            try {
                jf.setSelected(true);
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
        }
    }

}
