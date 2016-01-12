package main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import abm.Agent;
import abm.Algorithm;
import abm.Cell;
import map.MapHandler;
import visual.MapVisualizer;

public class Global {

    public static final int TURNS = 5000;
	public static final int TURN_TIME = 10;
	public static final int CIVILIZATIONS_NR = 10;
	public static final int INIT_CIVIL_SIZE = 1000;
	public static final int MAX_AGENTS_CELL_LIMIT = 7000;
	public static final int MAX_FERTILITY = 7;
	
	public static HashMap<Color, String> civilizations = new HashMap<>();
	
    public static void main(String args[]) throws IOException, RuntimeException {
        System.out.println("Program started,\nSetting up simulation parameters:\n");
       
        System.out.println("Map loading...");
        MapHandler map = new MapHandler();
        System.out.println("Map loaded");
        
        System.out.println("Setting civilizations' positions...");
        setCivilizations(map, CIVILIZATIONS_NR);
        System.out.print("Societies set\n");
        
        System.out.println("Setting up the visualization...");
        MapVisualizer visual = new MapVisualizer(map.getWidth(), map.getHeight());
        System.out.println("Visualization set");
        
        System.out.println("Starting simulation");
        Algorithm alg = new Algorithm(map);
        for (int i = 0; i < TURNS; i++) {
            visual.paintMap(map.getMap(), i);
            System.out.println("Simulation: turn " + i);
            alg.nextTurn();
        }
        visual.paintMap(map.getMap(), 0);
        System.out.println("End of simulation");
    }
    
	private static int[] setStartPosition(MapHandler map) {
		int row, col;
		do {
			row = map.getRandomRowCoordinate();
			col = map.getRandomColCoordinate();
		} while (map.getCell(row, col).getFertility() == 0);
		int[] cords = {row, col};
		
		return cords;
	}
	
	private static void setCivilizations(MapHandler map, int n) {
		for(int i=0; i<n; i++) {
			int[] cords = setStartPosition(map);
			Cell cell = map.getCell(cords[0], cords[1]);
			// TODO: draw one from basic colors, not completely random!
			Random r = new Random();
			Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
			for(int j=0; j<INIT_CIVIL_SIZE; j++) {
				cell.addAgent(new Agent(color));
			}
			cell.updateColor();
			civilizations.put(color, "Civ " + i);
		}
	}    
}
