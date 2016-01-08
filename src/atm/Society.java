package atm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import main.Simulation;
import cells.Cell;
import map.MapHandler;

public class Society {

	private int cellCount;
	private int wealth;
	private Color color;
	private ArrayList<Cell> cells;
	private int maintenance;
	private SocietyCellUpdater societyCellUpdater;
	
	public Society() {
		societyCellUpdater=new SocietyCellUpdater(this);
		cells = new ArrayList<>();
		this.cellCount = 0;
		this.wealth = 0;
		this.maintenance = 0;
		Random r = new Random();
		this.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		setStartPosition();
	}
	
	public int getCellCount() {
		return cellCount;
	}

	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	public int getWealth() {
		return wealth;
	}

	public void setWealth(int wealth) {
		this.wealth = wealth;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(int maintenance) {
		this.maintenance = maintenance;
	}
	
	public ArrayList<Cell> getCells(){
		return cells;
	}
	
	public void setCells(ArrayList<Cell> cells){
		this.cells=cells;
	}	
	

	public Color getSocietyColor() {
		return this.color;
	}

	public void setSocietyColor(Color color) {
		this.color = color;
	}

	private void setStartPosition() {
		int _x = MapHandler.getRandomXMapCoordinate();
		int _y = MapHandler.getRandomYMapCoordinate();
		while (Simulation.map[_x][_y].getFertility() == 0) {
			_x = MapHandler.getRandomXMapCoordinate();
			_y = MapHandler.getRandomYMapCoordinate();
		}
		System.out.println("Civilization's settlement coordinates are:[" + Simulation.map[_x][_y].getX() + ","
				+ Simulation.map[_x][_y].getY() + "]");
		societyCellUpdater.addCellToSociety(Simulation.map[_x][_y]);
	}

	public void updateSociety() {
		wealth=getSocietyWealth();
		maintenance = (int) (wealth * 0.5);   //TODO tu musze powazniejszy algorytm zaimplementowac
		ArrayList<Cell> borderCells=getBorderCells();
		System.out.println("Wealth: " + wealth + " Maintenance: " + maintenance + " Size: " + cellCount);
		if (wealth > maintenance) {
			ExpansionProcessor expansionProcessor = new ExpansionProcessor(wealth, maintenance, borderCells);
			societyCellUpdater.addCellsToSociety(expansionProcessor.getCells());
		} else {
			RegressionProcessor regressionProcessor = new RegressionProcessor(wealth, maintenance, borderCells);
			societyCellUpdater.removeCellsFromSociety(regressionProcessor.getCells());
		}
	}
	
	private ArrayList<Cell> getBorderCells(){
		ArrayList<Cell> borderCells=new ArrayList<Cell>();
		for (Cell c : cells) {
			c.updateIsBorderCell();
			if (c.isBorderCell()) {
				borderCells.add(c);
			}
		}
		return borderCells;
	}
	
	private int getSocietyWealth() {
		int result=0;
		for (Cell c : cells) {
			result += c.getFertility();
		}
		return result;
	}

}

// 1. Dodajemy spolecznosci metoda addSociety()
// a) Dla kazdej spolecznosci obliczane sa losowe miejsca startowe
// 2.Rozpoczynamy symulacje
// 3.Tura to:
// a)aktualizacja spolecznosci
// -zliczamy bogactwo cywilizacji
// -znajdujemy komorki brzegowe cywilizacji
// -obliczamy koszt utrzymania
// -na podstawie bogactwa i kosztu utrzymania okreslamy czy spoleczenstwo sie
// rozrasta czy maleje
// *Rozrasta:
// +Dodajemy nowe komorki, na podstawie komorek brzegowych szukamy wolnych
// komorek w otoczeniui dodajemy je do candidateCells
// +sortujemy candidateCells wg. fertility, im wiecej, tym lepiej
// +w zaleznosci od przyjetego warunku dodajemy mozliwa ilosc komorek(na razje
// jest to diff=-2)
// *Maleje:
// +sortujemy borderCells, na koncu listy sa komorki o najnizszej wartosci
// fertility
// +usuwamy komorki o najmniejszej wartosci ze spolecznosci
// b)aktualizacja mapy
// -kolorujemy mape
