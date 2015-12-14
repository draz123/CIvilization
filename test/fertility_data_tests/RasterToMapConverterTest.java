package fertility_data_tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fertility_data.DataParser;
import fertility_data.RasterMod;
import fertility_data.RasterToMapConverter;

public class RasterToMapConverterTest {
	
	RasterMod r;
	RasterToMapConverter conv;
	double[][] map;
	
	@Before
	public void setUp() throws Exception {
		r = DataParser.parseData("test/resources/data/sq1_nutrient_availability.asc");
		conv = new RasterToMapConverter(r);
		map = conv.convertRasterToFertilityMap();
	}

	@Test
	public void testRowsNumber() {

		assertEquals(241, map.length, 0.00001);
	}
	
	@Test
	public void testColsNumber() {

		assertEquals(601, map[0].length, 0.00001);
	}
	
	@Test
	public void testValue() {
		
		double val = r.getValue(540, 2040);
		System.out.println(val);
		assertEquals(val, map[0][0], 0.00001);
	}
	
}
