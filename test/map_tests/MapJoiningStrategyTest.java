package map_tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import map.*;

public class MapJoiningStrategyTest {
	
	MapJoiningStrategy strategy;

	@Test()
	public void testJoinAverage() {

		List<double[][]> maps = new ArrayList<double[][]>();
		double[][] map1 = {	{1, 0}, {3, 4} };
		double[][] map2 = {	{0, 3}, {4, 1} };
		double[][] map3 = {	{3, 4}, {1, 0} };
		double[][] map4 = {	{4, 1}, {0, 3} };
		maps.add(map1);
		maps.add(map2);
		maps.add(map3);
		maps.add(map4);
				
		strategy = new MapJoiningAverage();
		
		double[][] resultMap = strategy.joinMaps(maps);
		
		assertEquals(2, resultMap[0][0], 0.000001);
		assertNotEquals(3, resultMap[0][0], 0.000001);
	}
}
