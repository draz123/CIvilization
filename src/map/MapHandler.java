package map;

import java.io.IOException;
import java.util.List;

import cells.Cell;
import fertility_data.DataParser;
import fertility_data.RasterMod;
import fertility_data.RasterToMapConverter;

public class MapHandler {
	
	private static int width;
	private static int height;
	
	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		MapHandler.height = height;
	}

	public static void setWidth(int width) {
		MapHandler.width = width;
	}
	
	public static int getWidth() {
		return width;
	}

	private Cell[][] map;
	private int rows, cols;
	
	public MapHandler() throws IOException, RuntimeException{
		this(new MapJoiningAverage());
	}
	
	public MapHandler(MapJoiningStrategy strategy) throws IOException, RuntimeException {
		
		List<RasterMod> rasters = DataParser.parseAllSoilQualityData();
		List<double[][]> maps = RasterToMapConverter.convertRastersToFertilityMaps(rasters);
		double[][] fertilityMap = strategy.joinMaps(maps);
		
		rows = fertilityMap.length;
		cols = fertilityMap[0].length;
		map = new Cell[rows][cols];
		
		for (int j=0; j<rows; j++)
			for (int i=0; i<cols; i++) map[j][i] = new Cell((int)fertilityMap[j][i]);
			//TODO: change fertility to int (no casting)
	}
	
	public MapHandler(
			MapJoiningStrategy strategy,
			String path,
			int leftUpperLat,
			int leftUpperLon,
			int rightBottomLat,
			int rightBottomLon) {
		//TODO: implement
		
	}
	
	public Cell[][] getMap() {
		return map;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;

	}
	
	public static Cell[][] getMap(){
		return map;
	}
	
	public static void updateMap(Cell map[][]){
		//coloring map after society evaluation
	}
	
	
}
