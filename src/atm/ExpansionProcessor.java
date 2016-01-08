package atm;

import java.util.ArrayList;
import java.util.Collections;

import cells.Cell;
import main.Simulation;
import map.MapHandler;

public class ExpansionProcessor{

    private ArrayList<Cell> proposedCells;
    private ArrayList<Cell> borderCells;
    private ArrayList<Cell> outputCells;
    private int maintenance;
    private int difference;
    
	public ExpansionProcessor(int wealth,int maintenance,ArrayList<Cell> borderCells) {
        this.proposedCells = new ArrayList<>();
        this.borderCells = borderCells;
        this.outputCells = new ArrayList<>();
        this.maintenance=maintenance;
        this.difference=wealth-maintenance;
	}

	public ArrayList<Cell> getCells(){
		addNewCellsToProposedCellsFromBorderCells();
		addCellsFromProposedCellsToOutputCells();
		return outputCells;
	}	

	private void addCellsFromProposedCellsToOutputCells(){
		while (difference > maintenance * 0.9) {
			addNewCellFromCandidateCells();
			difference -= 2;
		}
	}

	private void addNewCellFromCandidateCells() {
		if (!proposedCells.isEmpty()) {
			outputCells.add(proposedCells.get(0));
			proposedCells.remove(0);
		}
	}
	
	private void addNewCellsToProposedCellsFromBorderCells() {
		for (Cell c : borderCells) {
			addFreeCellsFromBorderCellSurrounding(c);			
		}
		Collections.sort(proposedCells);
	}

	private void addFreeCellsFromBorderCellSurrounding(Cell c) {
		for (int i = c.getX() - 1; i <= c.getX() + 1; i++) {
			for (int j = c.getY() - 1; j <= c.getY() + 1; j++) {
				if (i >= 0 && j >= 0 && i < MapHandler.getHeight() && j < MapHandler.getWidth()
						&& Simulation.map[i][j].hasNoSociety() && Simulation.map[i][j].getFertility() > 0) {
					proposedCells.add(Simulation.map[i][j]);
				}
			}
		}
	}
	
	
}
