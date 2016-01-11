package abm;

import java.util.ArrayList;
import java.util.Collections;

import cells.Cell;

public class RegressionProcessor {
	private ArrayList<Cell> borderCells;
	private ArrayList<Cell> outputCells;
	private int maintenance;
	private int wealth;

	public RegressionProcessor(int wealth, int maintenance, ArrayList<Cell> borderCells) {
        this.borderCells = borderCells;
        outputCells = new ArrayList<>();
        this.maintenance=maintenance;
        this.wealth=wealth;
	}

	public ArrayList<Cell> getCells(){
		addCellsToRemove();
		return outputCells;
	}
	
	private void addCellsToRemove() {
		Collections.sort(borderCells);
		int difference = maintenance - wealth;
		while (difference > maintenance * 0.8) {
			outputCells.add(borderCells.get(borderCells.size() - 1));
			borderCells.remove(borderCells.size() - 1);
			difference -= 2;
		}
	}
}