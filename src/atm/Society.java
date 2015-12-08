package atm;

import java.util.ArrayList;

import cells.Cell;

public class Society {
	
	private int size;
	private int wealth;
	
	private ArrayList<Cell> cells;
	
	public Society(){
	}
	
	public Society(int size,int wealth){
		this.size=size;
		this.wealth=wealth;
	}
	
	public void updateSociety(){
		for(Cell c:cells){
			//
			//Algoritm of evaluating population's size an wealth depends on cells
			//
		}
	}
	
	public void doAction(){
		//Algorithm where we will count the next population's move
	}
}

