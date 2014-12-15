/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class IconPanel extends JPanel {

	public static final long serialVersionUID = 0;

	private Icon _icon;
	public IconPanel(LayoutManager ly) {
		super(ly);
	}

	public IconPanel() {

	}

	public void setIcon(Icon icon) {
		_icon = icon;
		setSize(new Dimension(_icon.getIconWidth(), _icon.getIconHeight()));
		setPreferredSize(new Dimension(_icon.getIconWidth(), _icon.getIconHeight()));
	}

	@Override
	public void paintComponent(Graphics g) {
		if (_icon == null) {
			return;
		}
		final int width = getWidth();
		final int height = getHeight();
		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);
		_icon.paintIcon(this, g, 0, 0);
       //Graphics2D gr = ((Graphics2D)(g));
		//gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
}
