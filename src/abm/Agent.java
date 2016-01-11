package abm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import cells.Cell;
import map.MapHandler;

public class Agent {

	private int cellCount;
	private int wealth;
	private Color color;
	private ArrayList<Cell> cells;
	private int maintenance;
	private AgentCellUpdater agentCellUpdater;
	private int lifeTime; 

	public Agent() {
		agentCellUpdater=new AgentCellUpdater(this);
		cells = new ArrayList<>();
		this.cellCount = 0;
		this.wealth = 0;
		this.maintenance = 0;
		Random r = new Random();
		this.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		setStartPosition();
		lifeTime = 0;
	}
	
	public int getLifeTime() {
		return lifeTime;
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
	

	public Color getAgentColor() {
		return this.color;
	}

	public void setAgentColor(Color color) {
		this.color = color;
	}

	private void setStartPosition() {
		int _x = MapHandler.getRandomXMapCoordinate();
		int _y = MapHandler.getRandomYMapCoordinate();
		while (MapHandler.getMap()[_x][_y].getFertility() == 0) {
			_x = MapHandler.getRandomXMapCoordinate();
			_y = MapHandler.getRandomYMapCoordinate();
		}
		System.out.println("Civilization's settlement coordinates are:[" + MapHandler.getMap()[_x][_y].getX() + ","
				+ MapHandler.getMap()[_x][_y].getY() + "]");
		agentCellUpdater.addCellToAgent(MapHandler.getMap()[_x][_y]);
	}

	public void updateAgent() {
		wealth=getAgentWealth();
		maintenance = (int) (wealth * 0.5);   //TODO tu musze powazniejszy algorytm zaimplementowac
		ArrayList<Cell> borderCells=getBorderCells();
		System.out.println("Wealth: " + wealth + " Maintenance: " + maintenance + " Size: " + cellCount);
		if (wealth > maintenance) {
			ExpansionProcessor expansionProcessor = new ExpansionProcessor(wealth, maintenance, borderCells);
			agentCellUpdater.addCellsToAgent(expansionProcessor.getCells());
		} else {
			RegressionProcessor regressionProcessor = new RegressionProcessor(wealth, maintenance, borderCells);
			agentCellUpdater.removeCellsFromAgent(regressionProcessor.getCells());
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
	
	private int getAgentWealth() {
		int result=0;
		for (Cell c : cells) {
			result += c.getFertility();
		}
		return result;
	}

}

// 1. Dodajemy spolecznosci metoda addAgent()
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
