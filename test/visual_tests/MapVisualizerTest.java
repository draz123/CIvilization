package visual_tests;

import java.awt.Color;
import java.util.Random;

import org.junit.Test;

import abm.Cell;
import visual.MapVisualizer;

public class MapVisualizerTest {
	
	public static void main(String args[]) {
		
		int width = 600;
		int height = 600;
		
		Cell[][] map = new Cell[width][height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                map[i][j] = new Cell(5, i, j);
                map[i][j].setColor(Color.BLUE);
            }
		
		MapVisualizer visual = new MapVisualizer(width, height);
		visual.paintMap(map, 0);
		
		Random r = new Random();
		for (int i=25; i<100; i++) {
			map[i][r.nextInt(width)].setColor(Color.BLACK);
		}
		visual.paintMap(map, 1);
	}
}
