package cells;

import map.MapHandler;

public class Cell {

	private int fertility;
	//first cordinate of cell
	private int x;
	//second coordinate of cell
	private int y;
	
	private boolean isBorderCell=true;
	private boolean hasNoSociety=true;
	
	public Cell(){		
	}
	
	public void setHasNoSociety(boolean hasNoSociety){
		this.hasNoSociety=hasNoSociety;
	}
	
	public Cell(int fertility){
		this.fertility=fertility;
	}
	
	public int getWealth() {
		return fertility;
	}

	public void setFertility(int fertility) {
		this.fertility = fertility;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	private void checkBorderCondition(){
		for(int i=x-1;i<=x+1;i++){
			for(int j=y-1;j<=y+1;i++){
				if(!hasNoSociety){
					isBorderCell=false;
				}
			}
		}
	}
	
}
