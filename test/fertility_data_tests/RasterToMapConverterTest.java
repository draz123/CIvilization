package fertility_data_tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fertility_data.DataParser;
import fertility_data.RasterMod;
import fertility_data.RasterToMapConverter;

public class RasterToMapConverterTest {
	
	List<RasterMod> rasters;

	List<double[][]> maps;
	
	@Before
	public void setUp() throws Exception {
		rasters = DataParser.parseAllSoilQualityData();
		maps = RasterToMapConverter.convertRastersToFertilityMaps(rasters);
	}

	@Test
	public void testRowsNumber() {

		assertEquals(241, maps.get(0).length, 0.00001);
	}
	
	@Test
	public void testColsNumber() {

		assertEquals(601, maps.get(0)[0].length, 0.00001);
	}
	
	@Test
	public void testValue() {
		
		double val = rasters.get(0).getValue(540, 2040);
		System.out.println(val);
		assertEquals(val, maps.get(0)[0][0], 0.00001);
	}
	
}
