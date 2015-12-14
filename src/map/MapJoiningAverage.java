package map;

import java.util.List;

public class MapJoiningAverage implements MapJoiningStrategy {

	@Override
	public double[][] joinMaps(List<double[][]> maps) {
		
		int rows = maps.get(0).length;
		int cols = maps.get(0)[0].length;
		int mapsLength = maps.size();
		double[][] resultMap = new double[rows][cols];
		
		for (int j=0; j<rows; j++)
			for (int i=0; i<cols; i++) {
				double sum = 0;
				for (double[][] map : maps) sum += map[j][i];
				resultMap[j][i] = Math.floor(sum / mapsLength);
			}
			
		return resultMap;
	}

}
