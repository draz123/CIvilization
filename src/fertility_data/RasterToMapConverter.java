package fertility_data;

import java.util.ArrayList;
import java.util.List;

public class RasterToMapConverter {
	
	public static double[][] convertRasterToFertilityMap(RasterMod raster, int skipCols, int cols, int skipRows, int rows) {
		
		double[][] map = new double[rows][cols];
		double[][] rasterData = raster.getData();
		
		for (int j=0; j<rows; j++) System.arraycopy(rasterData[j+skipRows], skipCols, map[j], 0, cols);
		
		return map;
	}
	
	public static List<double[][]> convertRastersToFertilityMaps(List<RasterMod> rasters) {
		
		return convertRastersToFertilityMaps(rasters, 45, -10, 25, 40);
	}
	
	public static List<double[][]> convertRastersToFertilityMaps(List<RasterMod> rasters, int leftUpperLat, int leftUpperLon, int rightBottomLat, int rightBottomLon) {
		
		int skipCols = (int) Math.floor((leftUpperLon - (-180)) / rasters.get(0).getCellsize());
		int cols = (int) Math.ceil((rightBottomLon - leftUpperLon) / rasters.get(0).getCellsize());
		int skipRows = (int) Math.floor((90 - leftUpperLat) / rasters.get(0).getCellsize());
		int rows = (int) Math.ceil((leftUpperLat - rightBottomLat) / rasters.get(0).getCellsize());
		
		List<double[][]> maps = new ArrayList<double[][]>(rasters.size());
		
		for (RasterMod raster : rasters) maps.add(convertRasterToFertilityMap(raster, skipCols, cols, skipRows, rows));
		
		return maps;
	}
	
}
