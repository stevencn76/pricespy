package net.ojava.openkit.drawnumbers.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.ojava.openkit.drawnumbers.core.AwardItem;
import net.ojava.openkit.drawnumbers.core.DrawNumbers;
import net.ojava.openkit.drawnumbers.res.Resource;
import net.ojava.openkit.drawnumbers.util.StrUtil;

public class InitDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JLabel numberLabel = new JLabel(Resource.getInstance().getResourceString(Resource.KEY_LABEL_POOLNUMS));
	private JTextArea numberArea = new JTextArea(10, 100);
	
	private JLabel awardLabel = new JLabel(Resource.getInstance().getResourceString(Resource.KEY_LABEL_AWARDS));
	private JTextField awardField = new JTextField(100);
	
	private JButton okBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_OK));
	private JButton cancelBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_CANCEL));
	
	private boolean ok = false;
	
	private Pattern numberPattern1 = Pattern.compile("\\d+-\\d+");
	private Pattern awardPattern1 = Pattern.compile("[\\w|\u4E00-\u9FA5|\ufe30-\uffa0]+=\\d+");
	
	public InitDialog(JFrame parent) {
		super(parent, Resource.getInstance().getResourceString(Resource.KEY_TITLE_AWARDINIT), true);
		
		initComponents();
		initEvents();
		initData();
	}
	
	private void initComponents() {
		JPanel cp = new JPanel();
		this.setContentPane(cp);
		cp.setLayout(new BorderLayout());
		cp.setBorder(new EmptyBorder(5, 7, 7, 7));
		
		cp.add(numberLabel, BorderLayout.NORTH);
		cp.add(new JScrollPane(numberArea));

		JPanel subPanel = new JPanel();
		cp.add(subPanel, BorderLayout.SOUTH);
		subPanel.setLayout(new GridLayout(3, 1, 5, 5));
		
		subPanel.add(awardLabel);
		subPanel.add(awardField);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		subPanel.add(p3);
		
		p3.add(okBtn);
		p3.add(cancelBtn);
		
		numberArea.setText("1-87");
		awardField.setText(Resource.getInstance().getResourceString(Resource.KEY_SAMPLE_AWARDS));
		
		this.pack();
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void initEvents() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				doCancel();
			}
		});
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doOk();
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doCancel();
			}
		});
	}
	
	private void initData() {
		if(!StrUtil.isEmpty(DrawNumbers.getInstance().getNumberPoolText())) {
			numberArea.setText(DrawNumbers.getInstance().getNumberPoolText());
		}
		
		if(!StrUtil.isEmpty(DrawNumbers.getInstance().getAwardText())) {
			awardField.setText(DrawNumbers.getInstance().getAwardText());
		}
	}
	
	private void doOk() {
		String poolStr = numberArea.getText();
		List<Integer> pool = parseNumberPool(poolStr);
		if(pool == null)
			return;
		
		
		String awardStr = awardField.getText();
		List<AwardItem> items = parseAward(awardStr);
		if(items == null)
			return;
		
		DrawNumbers.getInstance().reset(poolStr, awardStr, pool, items);
		
		ok = true;
		this.dispose();
	}
	
	private List<Integer> parseNumberPool(String src) {
		if(src == null) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTPOOLNUMBS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		src = src.trim();
		String ss[] = src.split("[,|，]");
		if(ss == null || ss.length == 0) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTPOOLNUMBS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		List<Integer> pool = new LinkedList<Integer>();
		for(String ts : ss) {
			if(ts == null)
				continue;
			
			ts = ts.trim();
			if(ts.length() == 0)
				continue;
			
			if(ts.indexOf('-') != -1) {
				Matcher numMatcher = numberPattern1.matcher(ts);
				if(!numMatcher.matches()) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTVALIDPOOLNUMBS), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
				String cs[] = ts.split("-");
				if(cs == null || cs.length < 2) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTVALIDPOOLNUMBS), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				int i1 = -1;
				int i2 = -1;
				
				try {
					i1 = Integer.parseInt(cs[0]);
				} catch (Exception e){}
				
				try {
					i2 = Integer.parseInt(cs[1]);
				} catch (Exception e){}
				
				for(int i=i1; i<=i2; i++) {
					pool.add(i);
				}
			} else {
				try {
					pool.add(Integer.parseInt(ts));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTVALIDPOOLNUMBS), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
		}
		
		if(pool.size() == 0) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTPOOLNUMBS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return pool;
	}

	
	private List<AwardItem> parseAward(String src) {
		if(src == null) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTAWARDITEMS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		src = src.trim();
		String ss[] = src.split("[,|，]");
		if(ss == null || ss.length == 0) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTAWARDITEMS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		List<AwardItem> items = new LinkedList<AwardItem>();
		for(String ts : ss) {
			if(ts == null)
				continue;
			
			ts = ts.trim();
			if(ts.length() == 0)
				continue;
			
			if(ts.indexOf('=') != -1) {
				Matcher awardMatcher = awardPattern1.matcher(ts);
				if(!awardMatcher.matches()) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTVALIDAWARDITEMS), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
				String cs[] = ts.split("=");
				if(cs == null || cs.length < 2) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTVALIDAWARDITEMS), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				String name = cs[0];
				int count = -1;
				
				try {
					count = Integer.parseInt(cs[1]);
				} catch (Exception e){}
				
				if(count <= 0) {
					JOptionPane.showMessageDialog(this, 
							Resource.getInstance().getResourceString(Resource.KEY_TIP_AWARDCOUNTERROR), 
							Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
							JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				AwardItem item = new AwardItem(name, count);
				items.add(item);
			}
		}
		
		if(items.size() == 0) {
			JOptionPane.showMessageDialog(this, 
					Resource.getInstance().getResourceString(Resource.KEY_TIP_INPUTAWARDITEMS), 
					Resource.getInstance().getResourceString(Resource.KEY_TITLE_ERRORTIP), 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return items;
	}
	
	private void doCancel() {
		ok = false;
		this.dispose();
	}
	
	public boolean isOk() {
		return ok;
	}
}
