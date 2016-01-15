package abm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import main.Global;
import map.MapHandler;

public class Algorithm {

	private static final int DEATH_TIME = 50 / Global.TURN_TIME;
	public static final int MIGRATION_CAUSE = 5; // if free room <
													// MIGRATION_CAUSE, try to
													// migrate
	public static final int MIGRATION_PERCENT = 20;
	public static final int TRAVEL_PERCENT = 10;

	private MapHandler map;
	int rows;
	int cols;

	public Algorithm(MapHandler map) {
		this.map = map;
		rows = map.getHeight();
		cols = map.getWidth();
	}

	public void nextTurn() {
		deathsAndBirths();
		migrations();
		elections();
	}

	private void deathsAndBirths() {
		Random r = new Random();

		// Tu moglby sie rozpoczynac proces dla mapy, w ktorym Elang/Go
		// przebiegalby po niej i odpalal procesy dla kazdej komorki, problem:
		// jak przekazac mape, jako strukture zawierajaca Cell, ktora zawiera w
		// sobie Agent, HashMap, Color oraz siega do globalnych stalych?
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i, j);
				if (currentCell.getAgentsSize() == 0)
					continue;
				ArrayList<Agent> aliveAgents = new ArrayList<Agent>();
				for (Agent agent : currentCell.agents) {
					if (agent.getLifeTime() < DEATH_TIME)
						aliveAgents.add(agent);
					agent.incLifeTime();
				}
				Collections.reverse(aliveAgents);
				currentCell.agents = aliveAgents;

				int availableSpace = currentCell.getAvailableSpace();
				ArrayList<Agent> newborns = new ArrayList<Agent>();
				for (Agent agent : currentCell.agents) {
					if (availableSpace == 0)
						break;
					boolean bornNewBaby = r.nextInt(100) < 100 - 20 * agent.getLifeTime();
					// TODO: make a parameter of it
					if (bornNewBaby) {
						newborns.add(new Agent(agent.getColor()));
						availableSpace--;
					}
				}
				for (Agent newborn : newborns)
					currentCell.addAgent(newborn);
			}
		}
		// Tu konczylby sie proces dla mapy,
		// jak odebrac te mape?
	}

	private void migrations() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i, j);
				if (currentCell.getAgentsSize() == 0)
					continue;
				ArrayList<Cell> neighbours = map.getNeighbours(i, j);
				if (currentCell.getAvailableSpace() < MIGRATION_CAUSE)
					migrate(currentCell, neighbours, MIGRATION_PERCENT * currentCell.getAgentsSize() / 100);
				migrate(currentCell, neighbours, TRAVEL_PERCENT * currentCell.getAgentsSize() / 100);
			}
		}
	}

	private void elections() {
		// Tu moglby sie rozpoczynac proces dla mapy, w ktorym Elang/Go
		// przebiegalby po niej i odpalal procesy dla kazdej komorki
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i, j);
				if (currentCell.getAgentsSize() != 0)
					currentCell.updateColor();
			}
		}
		// Tu konczylby sie proces dla mapy
	}

	private void migrate(Cell currentCell, ArrayList<Cell> neighbours, int migrantsNumber) {
		Random r = new Random();
		if (neighbours.size() == 0)
			return;
		for (int i = 0; i < migrantsNumber; i++) { 
			int index = r.nextInt(currentCell.getAgentsSize()); //TODO: better migrants' choice
			Agent agent = currentCell.agents.get(index);
			Cell destination = neighbours.get(r.nextInt(neighbours.size()));
			if (destination.hasAvailableSpace()) {
				destination.addAgent(agent);
				currentCell.removeAgent(agent);
			}
		}
	}

}
