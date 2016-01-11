package cells;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import abm.Agent;
import main.Global;
import map.MapHandler;

public class Cell implements Comparable<Cell> {
   
    private int x;
    private int y;
    private int fertility;
    private int maxAgentsNr;
    private Color color;
    private ArrayList<Agent> agents;
    
    private boolean borderCell = true;
    private boolean hasNoAgent = true;
    private HashMap<Color, Integer> citizens;
    
    public Cell() {
    	setInitColor();
    }
    
//    public Cell(int fertility) {
//        this.fertility = fertility;
//        maxAgentsNr = Global.MAX_AGENTS_NR_LIMIT / 7 * fertility;
//        setInitColor();
//        agents = new ArrayList<Agent>();
//    }

    public Cell(int fertility, int x, int y) {
    	this.fertility = fertility;
        setX(x);
        setY(y);
        maxAgentsNr = Global.MAX_AGENTS_NR_LIMIT / 7 * fertility;
        setInitColor();
        agents = new ArrayList<Agent>(maxAgentsNr);
        citizens = new HashMap<Color, Integer>();
    }

    public boolean isBorderCell() {
        return borderCell;
    }

    public boolean hasNoAgent() {
        return hasNoAgent;
    }

    public void setHasNoAgent(boolean hasNoAgent) {
        this.hasNoAgent = hasNoAgent;
    }

    public void setFertility(int fertility) {
        this.fertility = fertility;
    }

    public int getFertility() {
        return this.fertility;
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
    
    public void setColor(Color color) {
    	this.color = color;
    }
    
    public Color getColor() {
    	return color;
    }
    
    private void setInitColor() {
    	if (this.fertility == 0) this.color = new Color(0, 0, 0);
    	else this.color = new Color(255, 255, 255);
    }
    
    public void addAgent(Agent agent) {
        agents.add(agent);
        Color agentsColor = agent.getColor();
        if (citizens.containsKey(agentsColor))
        	citizens.put(agentsColor, citizens.get(agentsColor)+1);
        else 
        	citizens.put(agent.getColor(), 1);
    }
    
    public void removeAgent(int i) {
    	Color agentsColor = agents.get(i).getColor();
    	citizens.put(agentsColor, citizens.get(agentsColor)-1);
    	agents.remove(i);
    }
    
    public int getAgentsNumber() {
    	return agents.size();
    }
    
    public Color getDominantCivilization() {
    	HashMap.Entry<Color, Integer> maxEntry = null;

    	for (HashMap.Entry<Color, Integer> entry : citizens.entrySet())
    	{
    	    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
    	    {
    	        maxEntry = entry;
    	    }
    	}
    	
    	return maxEntry.getKey();
    }

    public void updateIsBorderCell() {
        int counter = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (isOnMap(i,j) ) {
                    counter++;
                }
            }
        }
        if (counter == 8) {
            borderCell = false;
        }
    }
    
    private boolean isOnMap(int i, int j){
    	return i >= 0 && j >= 0 && i != x && j != y && i < MapHandler.getHeight() && j < MapHandler.getWidth() && !MapHandler.getMap()[i][j].hasNoAgent();
    }

    @Override
    public int compareTo(Cell o) {
        return o.getFertility() - this.getFertility();
    }

}
