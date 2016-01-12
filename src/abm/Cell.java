package abm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import main.Global;
import map.MapHandler;

public class Cell {
   
    private int col;
    private int row;
    private int fertility;
    private int maxAgentsNr;
    private Color color;
    protected ArrayList<Agent> agents;
    private HashMap<Color, Integer> citizens;
    private boolean borderCell = true;

    public Cell() {
    	setInitColor();
    }

    public Cell(int fertility, int row, int col) {
    	this.fertility = fertility;
        this.col = col;
        this.row = row;
        maxAgentsNr = Global.MAX_AGENTS_CELL_LIMIT / Global.MAX_FERTILITY * fertility;
        setInitColor();
        agents = new ArrayList<Agent>();
        citizens = new HashMap<Color, Integer>();
    }

    public boolean isBorderCell() {
        return borderCell;
    }

    public int getFertility() {
        return this.fertility;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Color getColor() {
    	return color;
    }
    
    private void setInitColor() {
    	if (this.fertility == 0) this.color = new Color(0, 0, 0);
    	else this.color = new Color(255, 255, 255);
    }
    
    public void updateColor() {
    	int times = getAgentsNumber() / (Global.MAX_AGENTS_CELL_LIMIT / Global.MAX_FERTILITY);
    	Color c = getDominantCivilization();
    	for (int i=0; i<times; i++) c = c.darker();
    	this.color = c;
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
    	if (citizens.entrySet().size() == 0) return color;
    	
    	HashMap.Entry<Color, Integer> maxEntry = null;

    	for (HashMap.Entry<Color, Integer> entry : citizens.entrySet())
    	    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
    	        maxEntry = entry;
    	
    	return maxEntry.getKey();
    }
    
    public boolean hasRoomForAgent() {
    	return getFreeRoom() > 0;
    }
    
    public int getFreeRoom() {
    	return maxAgentsNr - getAgentsNumber();
    }
    
}
