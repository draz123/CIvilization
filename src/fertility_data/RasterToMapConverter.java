package fertility_data;

public class RasterToMapConverter {

	private RasterMod raster;
	
	public RasterToMapConverter(RasterMod raster) {
		this.raster = raster;
	}
	
	public double[][] convertRasterToFertilityMap() {
		
		return convertRasterToFertilityMap(45, -10, 25, 40);
	}
	
	public double[][] convertRasterToFertilityMap(int leftUpperLat, int leftUpperLon, int rightBottomLat, int rightBottomLon) {
		
		int skipCols = (int) Math.floor((leftUpperLon - (-180)) / raster.getCellsize());
		int cols = (int) Math.ceil((rightBottomLon - leftUpperLon) / raster.getCellsize());
		int skipRows = (int) Math.floor((90 - leftUpperLat) / raster.getCellsize());
		int rows = (int) Math.ceil((leftUpperLat - rightBottomLat) / raster.getCellsize());
		
		double[][] map = new double[rows][cols];
		double[][] rasterData = raster.getData();
		
		for (int j=0; j<rows; j++) System.arraycopy(rasterData[j+skipRows], skipCols, map[j], 0, cols);
		
		return map;
	} 
	
}
