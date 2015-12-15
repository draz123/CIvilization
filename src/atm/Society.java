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
//tu dzialam tera
		if(MapHandler.getMap()[getCoordinates()[1]][getCoordinates()[2]].getValue()!=0){
			c.setX(_x);
			c.setY(_y);
		}
		
		c.setHasNoSociety(false);
		addCell(c);	
		updateSociety();
		size++;
	}
	
	private int[] getCoordinates(){
		Random rx=new Random(MapHandler.getHeight());
		Random ry=new Random(MapHandler.getWidth());
		int[] result=new int[2];
		result[0]=rx.nextInt();
		result[1]=rx.nextInt();;
		return result;
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
			wealth+=c.getValue();
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

