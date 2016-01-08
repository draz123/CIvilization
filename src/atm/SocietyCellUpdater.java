package atm;

import java.util.ArrayList;

import cells.Cell;
import main.Simulation;

public class SocietyCellUpdater{
	
	private Society society;
	
	public SocietyCellUpdater(Society society){
		this.society=society;
	}
	
	public void removeCellsFromSociety(ArrayList<Cell> cellsToRemove) {
		for (Cell c : cellsToRemove) {
			removeCell(c);
		}
	}

	public void removeCell(Cell c) {
		Simulation.map[c.getX()][c.getY()].setHasNoSociety(true);
		c.setHasNoSociety(true);
		c.setColor(null); //TODO null to bedzie domyslny kolor pola mapy?
		society.setCellCount(society.getCellCount()-1);
		society.getCells().remove(c);
	}
	
	public void addCellsToSociety(ArrayList<Cell> newCells) {
		for (Cell c : newCells) {
			addCellToSociety(c);
		}
	}

	public void addCellToSociety(Cell c) {
		if (c.hasNoSociety()) {
			Simulation.map[c.getX()][c.getY()].setHasNoSociety(false);
			c.setHasNoSociety(false);
			c.setColor(society.getColor());
			society.setCellCount(society.getCellCount()+1);
			society.getCells().add(c);
		}
	}

}
