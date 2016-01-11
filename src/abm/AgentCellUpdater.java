package abm;

import java.awt.Color;
import java.util.ArrayList;

import cells.Cell;
import map.MapHandler;

public class AgentCellUpdater{
	
	private Agent agent;
	
	public AgentCellUpdater(Agent agent){
		this.agent=agent;
	}
	
	public void removeCellsFromAgent(ArrayList<Cell> cellsToRemove) {
		for (Cell c : cellsToRemove) {
			removeCell(c);
		}
	}

	public void removeCell(Cell c) {
		MapHandler.getMap()[c.getX()][c.getY()].setHasNoAgent(true);
		c.setHasNoAgent(true);
		c.setColor(new Color(0, 0, 0));
		agent.setCellCount(agent.getCellCount()-1);
		agent.getCells().remove(c);
	}
	
	public void addCellsToAgent(ArrayList<Cell> newCells) {
		for (Cell c : newCells) {
			addCellToAgent(c);
		}
	}

	public void addCellToAgent(Cell c) {
		if (c.hasNoAgent()) {
			MapHandler.getMap()[c.getX()][c.getY()].setHasNoAgent(false);
			c.setHasNoAgent(false);
			c.setColor(agent.getColor());
			agent.setCellCount(agent.getCellCount()+1);
			agent.getCells().add(c);
		}
	}

}
