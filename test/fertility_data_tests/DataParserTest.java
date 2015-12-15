package fertility_data_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import fertility_data.DataParser;
import fertility_data.RasterMod;

public class DataParserTest {

	@Test(expected = FileNotFoundException.class)
	public void testParseData1() throws IOException, RuntimeException {
		RasterMod r = DataParser.parseData("test/resources/data/b.txt");
	}
	
	@Test
	public void testParseData2() throws IOException, RuntimeException {
		RasterMod r = DataParser.parseData("test/resources/data/sq1_nutrient_availability.asc");
		assertEquals(6, r.getValue(228, 0), 0.00001);
	}
	
	@Test
	public void testParseAllSoilQualityData1() throws IOException, RuntimeException {
		List<RasterMod> list = DataParser.parseAllSoilQualityData("resources/fertility_data/");
		assertEquals(6, list.get(6).getValue(270, 0), 0.00001);
	}
	
	@Test
	public void testParseAllSoilQualityData2() throws IOException, RuntimeException {
		List<RasterMod> list = DataParser.parseAllSoilQualityData();
		assertEquals(6, list.get(6).getValue(270, 0), 0.00001);
	}
	
}
