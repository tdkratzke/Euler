package com.euler.tdk.euler0566.tmk;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class OvalPanelCanvas extends JPanel {
	private static final long serialVersionUID = 1L;

	public OvalPanelCanvas() {
	}

	@Override
	public void paintComponent(final Graphics g) {
		final int width = getWidth();
		final int height = getHeight();
		g.setColor(Color.black);
		g.drawOval(0, 0, width, height);
	}

	public static void main(final String args[]) {
		final JFrame jFrame = new JFrame("Oval Sample");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(new OvalPanelCanvas());
		jFrame.setSize(300, 200);
		jFrame.setVisible(true);
	}
}