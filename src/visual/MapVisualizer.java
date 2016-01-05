package visual;

import javax.swing.JFrame;

import cells.Cell;

public class MapVisualizer{
	
	private JFrame frame;
	private MapPanel panel;
	
	public MapVisualizer(int width, int height) {
        frame = new JFrame();
        panel = new MapPanel(width, height);
        
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}  

	public void paintMap(Cell[][] map, int turn) {
		frame.setTitle("Turn " + turn);
		panel.paintMap(map, turn);
	}
}
