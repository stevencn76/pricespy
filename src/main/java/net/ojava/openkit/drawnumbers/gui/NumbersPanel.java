package net.ojava.openkit.drawnumbers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

public class NumbersPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color BORDER_COLOR = Color.BLACK;
	private static final Color FONT_COLOR = Color.BLACK;
	
	private List<Integer> numberList = new ArrayList<Integer>();
	private int colCount = 10;
	private Font textFont = new Font("Monaco", Font.BOLD, 16);
	private int maxNum = 0;
	private String maxNumString = "0";
	
	public NumbersPanel(int colCount) {
		this.colCount = colCount;
	}
	
	private int getRowCount() {
		return (int)(Math.ceil(numberList.size() / (double)colCount));
	}
	
	public void updateNumbers(Collection<Integer> numbers) {
		numberList.clear();
		numberList.addAll(numbers);
		
		for(int i : numbers) {
			if(maxNum < i) {
				maxNum = i;
			}
		}
		maxNumString = "" + maxNum;
		
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension dim = this.getSize();
		int width = dim.width;
		int height = dim.height;
		
		int rowCount = getRowCount();
		if(rowCount <= 0)
			return;
		
		int rowHeight = height / rowCount;
		int colWidth = width / colCount;
		if (rowHeight > colWidth) {
			rowHeight = colWidth;
			height = rowCount * rowHeight;
		}
		
		int index = 0;
		for(int row=0; row<rowCount; row++) {
			g.setColor(BORDER_COLOR);
			int rowY = 0 + row * rowHeight;
			g.drawLine(0, rowY, width, rowY);
			for(int col=0; col<colCount; col++) {
				int colX = 0 + col * colWidth;
				g.drawLine(colX, rowY, colX, rowY + rowHeight - 1);
				
				if(index < this.numberList.size()) {
					Rectangle r = new Rectangle(colX + 2, rowY + 2, colWidth - 4, rowHeight - 4);
					int num = numberList.get(index);
					String numStr = this.getNumberString(num);
					this.drawTextFont(g, numStr, r);
					
					index++;
				}
			}
		}
		
		int bottomY = rowCount * rowHeight -1;
		g.drawLine(width - 1, 0, width - 1, bottomY);
		g.drawLine(0, bottomY, width, bottomY);
	}
	
	private String getNumberString(int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(num);
		for(int i=0; i<maxNumString.length() - sb.length(); i++) {
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	private void drawTextFont(Graphics g, String s, Rectangle r) {
		g.setFont(textFont);
		Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        Font font = g2.getFont().deriveFont(16f);
        g2.setFont(font);
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        LineMetrics lm = font.getLineMetrics(s, frc);
        float sh = lm.getAscent() + lm.getDescent();
		
     // scale text to fit and center in r
        double xScale = r.width/sw;
        double yScale = r.height/sh;
        double x = r.x + xScale*(r.width - xScale*sw)/2;
        double y = r.getMaxY() - yScale*lm.getDescent();
        AffineTransform at =
            AffineTransform.getTranslateInstance(x, y);
        at.scale(xScale, yScale);
        g2.setFont(font.deriveFont(at));
        g2.drawString(s, 0, 0);
	}
}
