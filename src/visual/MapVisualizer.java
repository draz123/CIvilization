package visual;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import abm.Cell;

public class MapVisualizer{
	
	private JFrame frame;
	private MapPanel panel;
	
	public MapVisualizer(int width, int height) {
        frame = new JFrame();
        panel = new MapPanel(width, height);
        
        frame.add(panel);
        frame.pack();
        setCenter();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}  

	public void paintMap(Cell[][] map, int turn) {
		frame.setTitle("Turn " + turn);
		panel.paintMap(map, turn);
	}
	
	private void setCenter(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x, y);
	}
}
