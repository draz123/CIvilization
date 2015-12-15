package atm;

import java.util.ArrayList;
import java.util.Random;

import cells.Cell;
import map.MapHandler;

public class Society {
	
	private int size;
	private int wealth;
	private String color;
	private ArrayList<Cell> cells;
	private int maintenance;
	
	public Society(){
		this.size=0;
		this.wealth=0;
		this.maintenance=0;
		setColor();
		setStartPosition();
	}
	
	private void setStartPosition() {
		Cell c=new Cell();
		int _x=getXCoordinate();
		int _y=getYCoordinate();
		while(MapHandler.getMap()[_x][_y].getWealth()==0){
			_x=getXCoordinate();
			_y=getYCoordinate();
		}
		c.setX(_x);
		c.setY(_y);
		c.setHasNoSociety(false);
		addCell(c);	
		updateSociety();
		size++;
	}
	
	private int getXCoordinate(){
		Random rx=new Random(MapHandler.getHeight());
		return rx.nextInt();
	}
	
	private int getYCoordinate(){
		Random ry=new Random(MapHandler.getWidth());
		return ry.nextInt();
	}
	
	public void addCell(Cell c){
		c.setHasNoSociety(false);
		size++;
		cells.add(c);
	}
	
	private void setColor(){
		Random r=new Random();
		//here we will set random color to society as a specific formatted code
	}
	
	public void updateSociety(){
		for(Cell c:cells){
			wealth+=c.getWealth();
		}
		
		if(wealth==maintenance){
			
		}
		else{ 
			if(wealth<maintenance){
				
			}
			else{
				
			}
		}
	}
	
	public void doAction(){
		//Algorithm where we will count the next population's move
	}
}

