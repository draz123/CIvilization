package atm;

import java.io.IOException;
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
	MapHandler m;
	
	public Society(){
		try {
			m=new MapHandler();
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		}
		cells=new ArrayList<>();
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
		while(m.getMap()[_x][_y].getWealth()==0){
			_x=getXCoordinate();
			_y=getYCoordinate();
		}
		c.setX(_x);
		c.setY(_y);
		System.out.println("Civilization's settlement coordinates are:["+c.getX()+","+c.getY()+"]");
		c.setHasNoSociety(false);
		c.toString();
		addCell(c);	
		updateSociety();
		size++;

	}
	
	private int getXCoordinate(){
		Random rx=new Random();
		return rx.nextInt(m.getHeight());
	}
	
	private int getYCoordinate(){
		Random ry=new Random();
		return ry.nextInt(m.getWidth());
	}
	
	public void addCell(Cell c){
		c.toString();
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
	

}

