package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import abm.Cell;

public class MapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	public MapPanel(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, null);
    }
	
    public void paintMap(Cell[][] map, int turn) {    	
		int width = map[0].length;
		int height = map.length;
    	for (int i=0; i<height; i++)
    		for (int j=0; j<width; j++) {
    			Color col = map[i][j].getColor();
    			image.setRGB(j, i, col.getRGB());
    		}
    	repaint();
    }
}
