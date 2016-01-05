package atm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import main.Simulation;
import cells.Cell;
import map.MapHandler;

public class Society {

    private int size;
    private int wealth;
    private Color color;
    private ArrayList<Cell> cells;
    private ArrayList<Cell> candidateCells;
    private ArrayList<Cell> borderCells;
    private int maintenance;


    public Society() {
        candidateCells = new ArrayList<>();
        borderCells = new ArrayList<>();
        cells = new ArrayList<>();
        this.size = 0;
        this.wealth = 0;
        this.maintenance = 0;
        Random r = new Random();
        this.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        setStartPosition();
    }

    private void setStartPosition() {
        int _x = getXCoordinate();
        int _y = getYCoordinate();
        while (Simulation.map[_x][_y].getFertility() == 0) {
            _x = getXCoordinate();
            _y = getYCoordinate();
        }
        System.out.println("Civilization's settlement coordinates are:[" + Simulation.map[_x][_y].getX() + "," + Simulation.map[_x][_y].getY() + "]");
        addCell(Simulation.map[_x][_y]);
        updateSociety();

    }

    private int getXCoordinate() {
        Random rx = new Random();
        return rx.nextInt(MapHandler.getHeight());
    }

    private int getYCoordinate() {
        Random ry = new Random();
        return ry.nextInt(MapHandler.getWidth());
    }

    public void addCell(Cell c) {
        if (c.hasNoSociety()) {
            Simulation.map[c.getX()][c.getY()].setHasNoSociety(false);
            c.setHasNoSociety(false);
            c.setColor(this.color);
            size++;
            cells.add(c);
        }
    }

    public void removeCell(Cell c) {
        Simulation.map[c.getX()][c.getY()].setHasNoSociety(true);
        c.setHasNoSociety(true);
        // usuń kolor cywilizacji z komórki?
        size--;
        cells.remove(c);
    }

    public void updateSociety() {
        wealth = 0;
        candidateCells.clear();
        borderCells.clear();
        for (Cell c : cells) {
            c.checkBorderCondition();
            if (c.isBorderCell()) {
                borderCells.add(c);
            }
            wealth += c.getFertility();
        }
        maintenance = (int) (wealth * 0.5);
        System.out.println("Wealth: " + wealth + " Maintenance: " + maintenance + " Size: " + size);
        if (wealth > maintenance) {
            addNewCells();
        } else {
            loseCells();
        }
    }
    
    public Color getColor() {
    	return this.color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    private void loseCells() {
        Collections.sort(borderCells);
        int difference = maintenance - wealth;
        while (difference > maintenance * 0.8) {
            removeCell(borderCells.get(borderCells.size() - 1));
            borderCells.remove(borderCells.size() - 1);
            difference -= 2;
        }
    }

    private void addNewCells() {
        int difference = wealth - maintenance;
        for (Cell c : borderCells) {
            checkFreeBorderCells(c);
        }
        Collections.sort(candidateCells);
        while (difference > maintenance * 0.7) {
            addNewCellFromCandidateCells();
            difference -= 2;
        }
    }

    private void addNewCellFromCandidateCells() {
        if (!candidateCells.isEmpty()) {
            addCell(candidateCells.get(0));
            candidateCells.remove(0);
        }
    }

    private void checkFreeBorderCells(Cell c) {
        for (int i = c.getX() - 1; i <= c.getX() + 1; i++) {
            for (int j = c.getY() - 1; j <= c.getY() + 1; j++) {
                if (i < MapHandler.getHeight() && j < MapHandler.getWidth() && Simulation.map[i][j].hasNoSociety() && Simulation.map[i][j].getFertility() > 0 && i >= 0 && j >= 0) {
                    candidateCells.add(Simulation.map[i][j]);
                }
            }
        }
    }
   
}


//  1. Dodajemy spolecznosci metoda addSociety()
//      a) Dla kazdej spolecznosci obliczane sa losowe miejsca startowe
//  2.Rozpoczynamy symulacje
//  3.Tura to:
//      a)aktualizacja spolecznosci
//          -zliczamy bogactwo cywilizacji
//          -znajdujemy komorki brzegowe cywilizacji
//          -obliczamy koszt utrzymania
//          -na podstawie bogactwa i kosztu utrzymania okreslamy czy spoleczenstwo sie rozrasta czy maleje
//              *Rozrasta:
//                  +Dodajemy nowe komorki, na podstawie komorek brzegowych szukamy wolnych komorek w otoczeniui dodajemy je do candidateCells
//                  +sortujemy candidateCells wg. fertility, im wiecej, tym lepiej
//                  +w zaleznosci od przyjetego warunku dodajemy mozliwa ilosc komorek(na razje jest to diff=-2)
//              *Maleje:
//                  +sortujemy borderCells, na koncu listy sa komorki o najnizszej wartosci fertility
//                  +usuwamy komorki o najmniejszej wartosci ze spolecznosci
//       b)aktualizacja mapy
//          -kolorujemy mape
