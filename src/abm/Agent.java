package abm;

import java.awt.Color;

public class Agent implements Comparable{

	private Color color;
	private int lifeTime; 
	
	public Agent(Color color) {
		this.color = color;
		lifeTime = 0;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public void incLifeTime() {
		lifeTime++;
	}

	public Color getColor() {
		return color;
	}
	
	@Override
	public int compareTo(Object o) {
		int comparedLifeTime = ((Agent) o).getLifeTime(); 
		return this.getLifeTime()-comparedLifeTime ;

	}

}
