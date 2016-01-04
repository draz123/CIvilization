package main;
import java.io.IOException;
import atm.Society;
import map.MapHandler;

public class Global {

	private static final int TURNS = 100;

	public static void main(String args[]) throws IOException, RuntimeException {
		//Reading map
		System.out.println("Program started,\nSetting up simulation parameters:\n");
		Simulation s=new Simulation();
		System.out.println("Map loading...");
		MapHandler map=new MapHandler();
		s.setMap(map.getMap(),map.getHeight(),map.getWidth());
		System.out.println("Map loaded");
		System.out.println("Setting civilization's positions...");
		s.addSociety(new Society());
		s.addSociety(new Society());
		s.addSociety(new Society());
		System.out.print("Societies settled\n");
		System.out.println("Starting simulation");
		for(int i=0; i< TURNS;i++){
		//Civilization grown process
			if(i%10==0)
				System.out.println("Doing simulation, turn: "+i);
			s.doTurn();
		}
		System.out.println("End of simulation");
		
		
		//Getting results and printing them
	}

}
