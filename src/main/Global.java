package main;

import java.io.IOException;
import java.util.ArrayList;

import abm.Agent;
import map.MapHandler;
import visual.MapVisualizer;

public class Global {

    private static final int TURNS = 500;
	private static final int CIVILIZATIONS_COUNT = 10;

    public static void main(String args[]) throws IOException, RuntimeException {
        //Reading map
        System.out.println("Program started,\nSetting up simulation parameters:\n");
        
        System.out.println("Map loading...");
        MapHandler map = new MapHandler();
        System.out.println("Map loaded");
        
        ArrayList<Agent> s = new ArrayList<Agent>();
        
        System.out.println("Setting civilization's positions...");
        for(int i=0;i<CIVILIZATIONS_COUNT;i++){
        	s.add(new Agent());
        }
        System.out.print("Societies settled\n");
        
        System.out.println("Setting up the visualization...");
        MapVisualizer visual = new MapVisualizer(map.getWidth(), map.getHeight());        
        
        System.out.println("Starting simulation");
        for (int i = 0; i < TURNS; i++) {
            visual.paintMap(map.getMap(), i);
            //Civilization grown process
            //if (i % 10 == 0)
                System.out.println("Doing simulation, turn: " + i);
            doTurn(s);
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
