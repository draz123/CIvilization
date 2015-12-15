package fertility_data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {
	
	static RasterReaderMod reader = new RasterReaderMod();
	
	public static RasterMod parseData() throws IOException, RuntimeException {
		return reader.readRaster("resources/fertility_data/");
	}
	
	public static RasterMod parseData(String path) throws IOException, RuntimeException {
		return reader.readRaster(path);
	}
	
	public static List<RasterMod> parseAllSoilQualityData() throws IOException, RuntimeException {

		return parseAllSoilQualityData("resources/fertility_data/");
	}
	
	public static List<RasterMod> parseAllSoilQualityData(String path) throws IOException, RuntimeException {
		List<RasterMod> soilQualityRasters = new ArrayList<RasterMod>();
		
		soilQualityRasters.add(parseData(path + "sq1_nutrient_availability.asc"));
		soilQualityRasters.add(parseData(path + "sq2_nutrient_retention_capacity.asc"));
		soilQualityRasters.add(parseData(path + "sq3_rooting_conditions.asc"));
		soilQualityRasters.add(parseData(path + "sq4_oxygen_availability_to_roots.asc"));
		soilQualityRasters.add(parseData(path + "sq5_excess_salts.asc"));	
		soilQualityRasters.add(parseData(path + "sq6_toxicity.asc"));
		soilQualityRasters.add(parseData(path + "sq7_workability.asc"));

		return soilQualityRasters;
	}
}
