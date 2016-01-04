package atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import main.Simulation;
import cells.Cell;
import map.MapHandler;

public class Society {
	
	private int size;
	private int wealth;
	private String color;
	private ArrayList<Cell> cells;	
	private ArrayList<Cell> candidateCells;
	private ArrayList<Cell> borderCells;
	private int maintenance;

	
	public Society(){
		candidateCells=new ArrayList<>();
		borderCells=new ArrayList<>();
		cells=new ArrayList<>();
		this.size=0;
		this.wealth=0;
		this.maintenance=0;
		setColor();
		setStartPosition();
	}
	
	private void setStartPosition() {
		int _x=getXCoordinate();
		int _y=getYCoordinate();
		while(Simulation.map[_x][_y].getFertility()==0){
			_x=getXCoordinate();
			_y=getYCoordinate();
		}
		System.out.println("Civilization's settlement coordinates are:["+Simulation.map[_x][_y].getX()+","+Simulation.map[_x][_y].getY()+"]");
		addCell(Simulation.map[_x][_y]);	
		updateSociety();
		size++;

	}
	
	private int getXCoordinate(){
		Random rx=new Random();
		return rx.nextInt(MapHandler.getHeight());
	}
	
	private int getYCoordinate(){
		Random ry=new Random();
		return ry.nextInt(MapHandler.getWidth());
	}
	
	public void addCell(Cell c){
		Simulation.map[c.getX()][c.getY()].setHasNoSociety(false);
		c.setHasNoSociety(false);
		size++;
		cells.add(c);
	}
	
	public void removeCell(Cell c){
		Simulation.map[c.getX()][c.getY()].setHasNoSociety(true);
		c.setHasNoSociety(true);
		size--;
		cells.remove(c);
	}
	
	private void setColor(){
		Random r=new Random();
		//here we will set random color to society as a specific formatted code
	}
	
	public void updateSociety(){
		wealth=0;
		candidateCells.clear();		
		borderCells.clear();
		for(Cell c:cells){
			c.checkBorderCondition();
			if(c.isBorderCell()){
				borderCells.add(c);
			}
			wealth+=c.getFertility();
		}
		maintenance=(int) (wealth*0.3);
		System.out.println("Wealth: " +wealth+" Maintenance: "+maintenance + " Size: "+ size);
		if(wealth>maintenance){
			getNewCells();
		}
		else{
			loseCells();
		}
	}

	private void loseCells() {
		ArrayList<Cell> lowestValueCells=new ArrayList();
		for(Cell c:cells){
			if(c.isBorderCell()){
				lowestValueCells.add(c);
			}
		}
		Collections.sort(lowestValueCells);
		int difference=maintenance-wealth;
		while(difference>maintenance*0.2){
			removeCell(lowestValueCells.get(lowestValueCells.size()-1));
			lowestValueCells.remove(lowestValueCells.size()-1);
			difference=difference-2;
		}
	}

	private void getNewCells() {
		int difference=wealth-maintenance;
		for(Cell c:borderCells){
			if(c.isBorderCell()){
				checkFreeBorderCells(c);
			}
		}
		Collections.sort(candidateCells);
		while(difference>0){
			addNewCellFromCandidateCells();
			difference=difference-2;
		}
	}

	private void addNewCellFromCandidateCells() {
		if(!candidateCells.isEmpty()){
			this.addCell(candidateCells.get(0));
			candidateCells.remove(0);
		}
	}

	private void checkFreeBorderCells(Cell c) {
		for(int i=c.getX()-1;i<=c.getX()+1;i++){
			for(int j=c.getY()-1;j<=c.getY()+1;j++){
				if(i<=MapHandler.getHeight() && j<=MapHandler.getWidth() && Simulation.map[i][j].hasNoSociety() && Simulation.map[i][j].getFertility()!=0 && i>=0 && j>=0){
					candidateCells.add(Simulation.map[i][j]);
				}
			}
		}
	}
	
}

