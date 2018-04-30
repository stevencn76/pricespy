package net.ojava.openkit.drawnumbers.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Color fromColor = new Color(20, 63,134);
	private Color toColor = new Color(164, 197, 252);
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isOpaque()) {
            return;
        }
        
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gradientPaint =new GradientPaint(0, 0, fromColor, 0, height, toColor,false);
        
        
        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, width, height);
        
    }
}
