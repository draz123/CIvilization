package abm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import main.Global;
import map.MapHandler;

public class Algorithm {

	private static final int DEATH_TIME = 50 / Global.TURN_TIME;
//	private static final int DEATH_TIME = 100 / Global.TURN_TIME;
//	public static final int MIGRATION_CAUSE = 500; // if free room < MIGRATION_CAUSE, try to migrate
	public static final int MIGRATION_CAUSE = 5; // if free room < MIGRATION_CAUSE, try to migrate
	public static final int MIGRATION_PERCENT = 20;
	public static final int TRAVEL_PERCENT = 10;

	private MapHandler map;

	public Algorithm(MapHandler map) {
		this.map = map;
	}
	
	public void nextTurn() {
		int popSize1 = map.countAgents();
		
		deathsAndBirths();
		migrations();
		nationPride();
		
		int popSize2 = map.countAgents();
		boolean a = true;
	}
	
	private void deathsAndBirths() {
		int rows = map.getHeight();
		int cols = map.getWidth();
		Random r = new Random();
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				Cell c = map.getCell(i, j);
				if (c.getAgentsNumber() == 0) continue;
				ArrayList<Agent> youngAgents = new ArrayList<Agent>();
				for (Agent agent: c.agents) {
					if (agent.getLifeTime() < DEATH_TIME) youngAgents.add(agent);
					agent.incLifeTime();
				}
				Collections.reverse(youngAgents);
				c.agents = youngAgents;
				
				int room = c.getFreeRoom();
				ArrayList<Agent> newborns = new ArrayList<Agent>();
				for (Agent agent: c.agents) {
					if (room == 0) 
						break;
					boolean succeedMakingABaby = r.nextInt(100) < (10-2*agent.getLifeTime())*10;
					// TODO: make a parameter of it
					if (succeedMakingABaby) { 
						newborns.add(new Agent(agent.getColor()));
						room--;
					}
				}
				for (Agent newborn: newborns)
					c.addAgent(newborn);
			}
		}
	}
	
	private void migrations() {
		int rows = map.getHeight();
		int cols = map.getWidth();
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				Cell c = map.getCell(i, j);
				if (c.getAgentsNumber() == 0) continue;
				ArrayList<Cell> neighbours = map.getNeighbours(i, j);
				if (c.getFreeRoom() < MIGRATION_CAUSE) 
					migrate(c, neighbours, MIGRATION_PERCENT * c.getAgentsNumber() / 100);
				migrate(c, neighbours, TRAVEL_PERCENT/100 * c.getAgentsNumber());
			}
		}
	}
	
	private void nationPride() {
		int rows = map.getHeight();
		int cols = map.getWidth();
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				Cell c = map.getCell(i, j);
				if (c.getAgentsNumber() != 0) c.updateColor();
			}
		}
	}
	
	private void migrate(Cell c, ArrayList<Cell> neighbours, int migrantsNumber) {
		Random r = new Random();
		if (neighbours.size() == 0) return;
		
		for (int i=0; i<migrantsNumber; i++) {
			int index = c.getAgentsNumber()-1; //TODO: better migrants' choice
			Agent agent = c.agents.get(index);
			Cell destination = neighbours.get(r.nextInt(neighbours.size()));
			if (destination.hasRoomForAgent()) {
				destination.addAgent(agent);
				c.removeAgent(index);
			}
		}		
	}
}
