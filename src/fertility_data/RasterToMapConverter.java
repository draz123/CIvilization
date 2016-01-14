package fertility_data;

import java.util.ArrayList;
import java.util.List;

public class RasterToMapConverter {

    public static List<double[][]> convertRastersToFertilityMaps(List<RasterMod> rasters) {
        return convertRastersToFertilityMaps(rasters, 50, -10, 25, 40);
        //return convertRastersToFertilityMaps(rasters, 90, -90, 0, 90);
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
    
    public static double[][] convertRasterToFertilityMap(RasterMod raster, int skipCols, int cols, int skipRows, int rows) {
        double[][] map = new double[rows][cols];
        double[][] rasterData = raster.getData();

        for (int i = 0; i < rows; i++) System.arraycopy(rasterData[i + skipRows], skipCols, map[i], 0, cols);
        for (int i = 0; i < rows; i++)
        	for (int j = 0; j < cols; j++) {
        		// 0 is for sea as it was originally, then the bigger number the better the soil 
        		double val = map[i][j];
        		if (val == 0 || val == 7) // seas or waterbodies
        			val = 0;
        		else if (val == 6 || val == 5) // permafrost or non-soil
        			val = 1; 
        		else if (val == 4) // very severe constraints
        			val = 2; 
         		else if (val == 3) // severe constraints
        			val = 4; 
         		else if (val == 2) // moderate constraints
        			val = 6;
         		else // non or slight constraints
        			val = 7;
        		map[i][j] = val;
        	}

        return map;
    }

}
