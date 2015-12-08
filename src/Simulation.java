import java.util.ArrayList;

import atm.Society;
import map.MapHandler;

public class Simulation{

	private ArrayList<Society> societies;
	
	public Simulation(){		
	}
	
	public void addSociety(Society society){
		societies.add(society);
	}
	
	public void doTurn() {
		for (Society s : societies){
			s.updateSociety();
			s.doAction();
			MapHandler.updateMap();
		}
		
	}

	public void setMap() {
		//There is the setting and processing map values to cells
		
	}

}
