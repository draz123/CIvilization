package main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import abm.Agent;
import abm.Algorithm;
import abm.Cell;
import concurrent.ErlangSimulation;
import map.MapHandler;
import visual.MapVisualizer;

public class Global {
	
	/*
	 * Parameters described on page 20 of included documentation
	 */
    public static  int TURNS = 500;
	public static  int TURN_TIME = 10;
	public static  int CIVILIZATIONS_NR = 10;
	public static  int MAX_INIT_CIVIL_SIZE = 10;
	public static  int MAX_AGENTS_CELL_LIMIT = 70;
	public static  int MAX_FERTILITY = 7;
	
	public static HashMap<Color, String> civilizations = new HashMap<>();
	
    public static void main(String args[]) throws IOException, RuntimeException {
	    try{
	    	Global.loadParameters();
	        Algorithm.loadParameters();
	    } catch (Exception e) {
	    	System.err.println("Loading parameters from file failed...");
	    	e.printStackTrace();
	    }
	    
    	System.out.println("Program started!\nSetting up simulation parameters...\n");
        System.out.println("Map loading...");
        MapHandler map = new MapHandler();
        System.out.println("Map loaded!\n");
        
        System.out.println("Setting up the visualization...");
        MapVisualizer visual = new MapVisualizer(map.getWidth(), map.getHeight());
        System.out.println("Visualization set!\n");

        ErlangSimulation simulation = new ErlangSimulation(map, visual);
        try {
        	simulation.start();
        } catch (Exception e) {
    		e.printStackTrace();
    	}
        
        /* Erlang/Go
         * 
         * Here the connection to an Erlang/Go program is set 
         * and the map (list of lists of fertility) is sent to it.
         * Agent, Algorithm, Cell classes and MapHandler's simulation-relevant logic
         * all need to be implemented in the program.
         * The program sends a map of colors back to Java thread every turn, 
         * which is then visualized.  
         * 
         */
/*        System.out.println("Setting civilizations' positions...");
        setCivilizations(map, CIVILIZATIONS_NR);
        System.out.println("Societies set!\n");
        
        System.out.println("Starting simulation...");
        Algorithm alg = new Algorithm(map);
        for (int i = 0; i < TURNS; i++) {
            visual.paintMap(map.getMap(), i);
            System.out.println("Simulation: turn " + i);
            if (i % 10 == 0) System.out.println("Population size: " + map.countAgents());
            alg.nextTurn();
        }
        visual.paintMap(map.getMap(), 0);*/
        /* Erlang/Go
         * 
         * Here the connection to Erlang/Go is closed 
         */
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
	
	public static void setCivilizations(MapHandler map, int n) {
		for(int i=0; i<n; i++) {
			int[] cords = setStartPosition(map);
			Cell cell = map.getCell(cords[0], cords[1]);
			Random r = new Random();
			Color color = new Color(r.nextInt(50)+200, r.nextInt(50)+200, r.nextInt(50)+200);
			for(int j=0; j<MAX_INIT_CIVIL_SIZE; j++) {
				cell.addAgent(new Agent(color));
			}
			cell.updateColor();
			civilizations.put(color, "Civ " + i);
		}
	}
	
	private static void loadParameters() throws IOException {
		
		File fileToRead = new File("app.conf");
		if(fileToRead.exists()) {
			byte[] encoded = Files.readAllBytes(Paths.get("app.conf"));
			String str = new String(encoded, StandardCharsets.UTF_8);
			
			str = str.replace("\n", "").replace("\r", "");
			int[] tab = {
					TURNS, 
					TURN_TIME, 
					CIVILIZATIONS_NR, 
					MAX_INIT_CIVIL_SIZE,
					MAX_AGENTS_CELL_LIMIT,
					MAX_FERTILITY
					};
			
			str = str.replaceAll("[^\\d;]", "");
			
			Scanner scanner = new Scanner(str);
			scanner.useDelimiter(";");
			
			int i = 0;
			while(scanner.hasNext() && i <= 5){
				if(scanner.hasNextInt()){
					int tmp = Integer.parseInt(scanner.next());
					tab[i++] = tmp; 
				}
			}
			scanner.close();
			TURNS = tab[0];
			TURN_TIME = tab[1]; 
			CIVILIZATIONS_NR = tab[2];
			MAX_INIT_CIVIL_SIZE = tab[3];
			MAX_AGENTS_CELL_LIMIT = tab[4];
			MAX_FERTILITY = tab[5];
		}
	}
}
