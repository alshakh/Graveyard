/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JPanel;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
class IconPanel extends JPanel
{
    public static final long serialVersionUID = 0;
    
    public final Icon _icon;
    
    
    public IconPanel(Icon icon)
    {
	    _icon = icon;
	    setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        final int width = getWidth();
        final int height = getHeight();
                g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        _icon.paintIcon(this, g, 0,0);
       //Graphics2D gr = ((Graphics2D)(g));
       //gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}