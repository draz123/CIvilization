import java.util.ArrayList;

import atm.Society;
import cells.Cell;
import map.MapHandler;

public class Simulation{

	private Cell map[][];
	private int width;
	private int height;
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
			MapHandler.updateMap(map);
		}
		
	}

	public void setMap(Cell[][] map,int height,int width) {
		setMapDimensions(height,width);
		this.map=map;
		//There is the setting and processing map values to cells
		
	}
	
	private void setMapDimensions(int height,int width){
		this.height=height;
		this.width=width;
	}

}
