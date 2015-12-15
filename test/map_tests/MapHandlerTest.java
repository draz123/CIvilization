package map_tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import cells.Cell;
import map.MapHandler;

public class MapHandlerTest {

	@Test
	public void createMap() throws IOException, RuntimeException {
		
		MapHandler mapHandler = new MapHandler();
		Cell[][] map = mapHandler.getMap();
		
		assertEquals(0, map[0][0].getFertility(), 0.00001); 
	}
}
