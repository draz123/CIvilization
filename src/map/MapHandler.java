package map;

import cells.Cell;

public class MapHandler {
	
	//Map from xml or csv
	private static Cell map[][];
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

	public MapHandler(){
		//reading map
	}
	
	public static Cell[][] getMap(){
		return map;
	}
	
	public static void updateMap(Cell map[][]){
		//coloring map after society evaluation
	}
	
}
