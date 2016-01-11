package main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import abm.Agent;
import map.MapHandler;
import visual.MapVisualizer;

public class Global {

    public static final int TURNS = 50;
	public static final int TURN_TIME = 10;
	public static final int CIVILIZATIONS_NR = 10;
	public static final int MAX_AGENTS_NR_LIMIT = 7000;
	public static HashMap<Color, String> civilizations = new HashMap<>();
	
    public static void main(String args[]) throws IOException, RuntimeException {
        //Reading map
        System.out.println("Program started,\nSetting up simulation parameters:\n");
        
        System.out.println("Map loading...");
        MapHandler map = new MapHandler();
        System.out.println("Map loaded");
        
        ArrayList<Agent> agents = new ArrayList<Agent>();
        
        System.out.println("Setting civilization's positions...");
        for(int i=0;i<CIVILIZATIONS_NR;i++){
        	agents.add(new Agent());
        }
        System.out.print("Societies settled\n");
        
        System.out.println("Setting up the visualization...");
        MapVisualizer visual = new MapVisualizer(map.getWidth(), map.getHeight());        
        
        System.out.println("Starting simulation");
        for (int i = 0; i < TURNS; i++) {
            visual.paintMap(map.getMap(), i);
            //Civilization grown process
            System.out.println("Doing simulation, turn: " + i);
            doTurn(agents);
        }
        System.out.println("End of simulation");
        visual.paintMap(map.getMap(), 0);

        //Getting results and printing them
    }
    
    public static void doTurn(ArrayList<Agent> s) {
        for (Agent agent : s) {
            agent.updateAgent();
        }
    }

}
