package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cells.Cell;

public class MapPanel extends JPanel {

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
    	//TODO: zwaliduj rozmiary mapy - muszą być takie same, jak obrazka!
    	
		int width = map[0].length;
		int height = map.length;
    	for (int i=0; i<height; i++)
    		for (int j=0; j<width; j++) {
    			Cell c = map[i][j];
    			Color col = c.getColor();
    			int rgb = col.getRGB();
    			image.setRGB(j, i, rgb);
    		}
    	repaint();
    }
}
