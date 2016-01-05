package cells;

import main.Simulation;
import java.awt.Color;
import map.MapHandler;

public class Cell implements Comparable<Cell> {

    private int fertility;
    //first coordinate of cell
    private int x;
    //second coordinate of cell
    private int y;

    private Color color;

    private boolean borderCell = true;
    private boolean hasNoSociety = true;

    public Cell() {
    	setInitColor();
    }
    
    public Cell(int fertility) {
        this.fertility = fertility;
        setInitColor();
    }

    public Cell(int fertility, int x, int y) {
        setFertility(fertility);
        setX(x);
        setY(y);
        setInitColor();
    }

    public boolean isBorderCell() {
        return borderCell;
    }

    public boolean hasNoSociety() {
        return hasNoSociety;
    }

    public void setHasNoSociety(boolean hasNoSociety) {
        this.hasNoSociety = hasNoSociety;
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

    public void checkBorderCondition() {
        int counter = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < MapHandler.getHeight() && j < MapHandler.getWidth() && !Simulation.map[i][j].hasNoSociety && i >= 0 && j >= 0 && i != x && j != y) {
                    counter++;
                }
            }
        }
        if (counter == 8) {
            borderCell = false;
        }
    }

    @Override
    public int compareTo(Cell o) {
        return o.getFertility() - this.getFertility();
    }

}
