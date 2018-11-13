package net.ojava.openkit.pricespy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import net.ojava.openkit.pricespy.gui.compare.ComparePricePanel;
import net.ojava.openkit.pricespy.gui.query.QueryPricePanel;
import net.ojava.openkit.pricespy.gui.sync.SyncProductPanel;
import net.ojava.openkit.pricespy.res.Resource;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabPane = new JTabbedPane();
	private QueryPricePanel queryPricePanel = new QueryPricePanel();
	private ComparePricePanel comparePricePanel = new ComparePricePanel();
	private SyncProductPanel syncProductPanel = new SyncProductPanel();
	
	public MainFrame() {
		super(Resource.getInstance().getResourceString(Resource.KEY_APPNAME));
		
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		tabPane.add(Resource.getInstance().getResourceString(Resource.KEY_LABEL_QUERYPRICE), queryPricePanel);
		tabPane.add(Resource.getInstance().getResourceString(Resource.KEY_LABEL_COMPAREPRICE), comparePricePanel);
		tabPane.add(Resource.getInstance().getResourceString(Resource.KEY_LABEL_SYNCPRODUCT), syncProductPanel);
		
		JPanel cp = new JPanel();
		cp.setBorder(new EmptyBorder(5, 7, 7, 7));
		cp.setLayout(new BorderLayout());
		cp.add(tabPane);
		
		this.getContentPane().add(cp);
		
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
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == initBtn) {
//		} else if(e.getSource() == viewResultBtn) {
//		} else if(e.getSource() == drawBtn) {
//		}
	}
}
