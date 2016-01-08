package main;

import java.util.ArrayList;

import atm.Society;
import cells.Cell;
import map.MapHandler;

public class Simulation {

    public static Cell map[][];
    private ArrayList<Society> societies;

    public Simulation() {
        societies = new ArrayList<Society>();
    }

    public void addSociety(Society society) {
        societies.add(society);
    }

    public void doTurn() {
        for (Society s : societies) {
            s.updateSociety();
        }
    }

    public void setMap(Cell[][] map, int height, int width) {
        this.map = map;
        //There is the setting and processing map values to cells

    }


}
