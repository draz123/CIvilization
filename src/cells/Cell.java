package cells;

public class Cell {

	private int slope;
	private int fertility;
	
	public Cell(){		
	}
	
	public Cell(int slope, int fertility){
		this.slope=slope;
		this.fertility=fertility;
	}
	
	public void setSlope(int slope) {
		this.slope = slope;
	}
	
	public int getSlope(){
		return slope;
	}
	
	public int getFertility() {
		return fertility;
	}

	public void setFertility(int fertility) {
		this.fertility = fertility;
	}
	
}
