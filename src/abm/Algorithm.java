package abm;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import main.Global;
import map.MapHandler;

public class Algorithm {

	private static  int DEATH_TIME = 50 / Global.TURN_TIME;
	public static  int MIGRATION_CAUSE = 5; // if free room < MIGRATION_CAUSE, try to migrate
	public static  int MIGRATION_PERCENT = 20;
	public static  int TRAVEL_PERCENT = 10;

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
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i, j);
				if (currentCell.getAgentsSize() != 0)
					currentCell.updateColor();
			}
		}
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
	
	public static void loadParameters() throws IOException {
		File fileToRead = new File("app.conf");
		if(fileToRead.exists()) {
			byte[] encoded = Files.readAllBytes(Paths.get("app.conf"));
			String str = new String(encoded, StandardCharsets.UTF_8);
			
			String[] tokens = str.split(";");
			int[] tab = {
					DEATH_TIME,
					MIGRATION_CAUSE,
					MIGRATION_PERCENT,
					TRAVEL_PERCENT
					};
			
			for (int i = 6; i < tokens.length; i++) {
				Scanner scanner = new Scanner(tokens[i]);
				if(scanner.hasNext())
					if(scanner.hasNextBigInteger())
						tab[i-7] = scanner.nextInt();
				scanner.close();
			}
		}
}

}
