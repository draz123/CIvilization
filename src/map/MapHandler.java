package map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import abm.Cell;
import fertility_data.DataParser;
import fertility_data.RasterMod;
import fertility_data.RasterToMapConverter;

public class MapHandler {

    private Cell[][] map;
    private int rows, cols;
    private Random random = new Random();
    
    public MapHandler() throws IOException, RuntimeException {
        this(new MapJoiningAverage());
    }

    public MapHandler(MapJoiningStrategy strategy) throws IOException, RuntimeException {
        List<RasterMod> rasters = DataParser.parseAllSoilQualityData();
        List<double[][]> maps = RasterToMapConverter.convertRastersToFertilityMaps(rasters);
        double[][] fertilityMap = strategy.joinMaps(maps);

        rows = fertilityMap.length;
        cols = fertilityMap[0].length;
        map = new Cell[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                map[i][j] = new Cell((int) fertilityMap[i][j], i, j);
    }

    public int getHeight() {
        return rows;
    }

    public int getWidth() {
        return cols;
    }

    public Cell[][] getMap() {
        return map;
    }
    
    public Cell getCell(int row, int col) {
    	return map[row][col];
    }
    
	public int getRandomColCoordinate() {
		return random.nextInt(cols);
	}

	public int getRandomRowCoordinate() {
		return random.nextInt(rows);
	}

    public boolean isOnMap(int row, int col) {
    	return (row >= 0 && col >= 0 && row < rows && col < cols);
    }
    
    public boolean isOnLand(int row, int col) {
    	return isOnMap(row, col) && getCell(row, col).getFertility() != 0;
    }
    
    public ArrayList<Cell> getNeighbours(int row, int col) {
    	ArrayList<Cell> neighbours = new ArrayList<>();
    	if (isOnLand(row-1, col-1)) neighbours.add(getCell(row-1, col-1));
    	if (isOnLand(row-1, col)) neighbours.add(getCell(row-1, col));
    	if (isOnLand(row-1, col+1)) neighbours.add(getCell(row-1, col+1));
    	if (isOnLand(row, col+1)) neighbours.add(getCell(row, col+1));
    	if (isOnLand(row+1, col+1)) neighbours.add(getCell(row+1, col+1));
    	if (isOnLand(row+1, col)) neighbours.add(getCell(row+1, col));
    	if (isOnLand(row+1, col-1)) neighbours.add(getCell(row+1, col-1));
    	if (isOnLand(row, col-1)) neighbours.add(getCell(row, col-1));
    	
    	return neighbours;
    }
}
