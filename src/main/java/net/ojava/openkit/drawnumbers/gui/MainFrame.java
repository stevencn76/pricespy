package net.ojava.openkit.drawnumbers.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.ojava.openkit.drawnumbers.core.AwardItem;
import net.ojava.openkit.drawnumbers.core.DrawNumbers;
import net.ojava.openkit.drawnumbers.res.Resource;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenuItem initMenuItem = new JMenuItem(Resource.getInstance().getResourceString(Resource.KEY_LABEL_AWARDSETTING));
	private JMenuItem viewMenuItem = new JMenuItem(Resource.getInstance().getResourceString(Resource.KEY_LABEL_VIEWPOOLNUMS));
	private JTextArea resultArea = new JTextArea();
	private JTextField curAwardField = new JTextField(16);
	private JButton drawBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_DRAW));
	
	public MainFrame() {
		super(Resource.getInstance().getResourceString(Resource.KEY_APPNAME));
		
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu systemMenu = new JMenu(Resource.getInstance().getResourceString(Resource.KEY_LABEL_SYSTEM));
		menuBar.add(systemMenu);
		systemMenu.add(initMenuItem);
		systemMenu.addSeparator();
		systemMenu.add(viewMenuItem);
		
		this.setJMenuBar(menuBar);
		
		resultArea.setEditable(false);
		resultArea.setBackground(Color.LIGHT_GRAY);
		resultArea.setForeground(Color.BLUE);
		resultArea.setFont(new Font("宋 体", Font.BOLD, 45));
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		resultArea.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_SETNUMSANDAWARDS));
		this.getContentPane().add(new JScrollPane(resultArea));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.getContentPane().add(p1, BorderLayout.NORTH);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout(5, 5));
		p2.add(curAwardField);
		p2.add(drawBtn, BorderLayout.EAST);

		drawBtn.setEnabled(false);
		
		p1.add(p2);
		Color bc = new Color(229, 164, 8);
		p1.setBackground(bc);
		p2.setBackground(bc);
		curAwardField.setBackground(bc);
		curAwardField.setForeground(Color.WHITE);
		curAwardField.setFont(new Font("宋 体", Font.BOLD, 32));
		curAwardField.setBorder(new EmptyBorder(0, 0, 0, 0));
		curAwardField.setEditable(false);
		
		this.setSize(new Dimension(1204, 768));
		this.setExtendedState( this.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		this.setVisible(true);
	}
	
	private void initEvents() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
		initMenuItem.addActionListener(this);
		viewMenuItem.addActionListener(this);
		drawBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == initMenuItem) {
			doInitNumberPool();
		} else if(e.getSource() == viewMenuItem) {
			doViewNumberPool();
		} else if(e.getSource() == drawBtn) {
			doDraw();
		}
	}
	
	private void doInitNumberPool() {
		InitDialog dlg = new InitDialog(this);
		dlg.setVisible(true);
		
		if(dlg.isOk()) {
			resultArea.setText(DrawNumbers.getInstance().getAwardsDesc());
			
			AwardItem item = DrawNumbers.getInstance().nextAwardItem();
			if(item != null) {
				curAwardField.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_READYDRAW) + ": " + item.getName());
				drawBtn.setEnabled(true);
			}
		}
	}
	
	private void doViewNumberPool() {
		ViewDialog dlg = new ViewDialog(this);
		dlg.setVisible(true);
	}
	
	private DrawDialog getDrawDlg() {
		return new DrawDialog(this);
	}
	
	private void doDraw() {
		getDrawDlg().open();
		
		resultArea.setText(DrawNumbers.getInstance().getAwardsDesc());
		
		AwardItem item = DrawNumbers.getInstance().nextAwardItem();
		if(item != null) {
			curAwardField.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_READYDRAW) + ": " + item.getName());
			drawBtn.setEnabled(true);
		} else {
			curAwardField.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_DRAWEND));
			drawBtn.setEnabled(false);
		}
	}
}
