package main;

import java.io.IOException;

import atm.Society;
import map.MapHandler;
import visual.MapVisualizer;

public class Global {

    private static final int TURNS = 500;
	private static final int CIVILIZATIONS_COUNT = 10;

    public static void main(String args[]) throws IOException, RuntimeException {
        //Reading map
        System.out.println("Program started,\nSetting up simulation parameters:\n");
        Simulation s = new Simulation();
        
        System.out.println("Map loading...");
        MapHandler map = new MapHandler();
        s.setMap(map.getMap(), map.getHeight(), map.getWidth());
        System.out.println("Map loaded");
        
        System.out.println("Setting civilization's positions...");
        for(int i=0;i<CIVILIZATIONS_COUNT;i++){
        	s.addSociety(new Society());
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
            s.doTurn();
        }
        System.out.println("End of simulation");
        visual.paintMap(map.getMap(), 0);

        //Getting results and printing them
    }

}
