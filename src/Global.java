import java.io.IOException;

import com.moseph.gis.raster.Raster;
import com.moseph.gis.raster.RasterReader;

import atm.Society;
import map.MapHandler;

public class Global {

	private static final int TURNS = 1000;

	public static void main(String args[]) throws IOException, RuntimeException {
		//Reading map
		
		//Setting civilizations?
		Simulation s=new Simulation();
		s.setMap(MapHandler.getMap(),MapHandler.getHeight(),MapHandler.getWidth());
		
		s.addSociety(new Society());
		s.addSociety(new Society());
		s.addSociety(new Society());
		
		for(int i=0; i< TURNS;i++){
		//Civilization grown process
			s.doTurn();
		}
		
		
		//Getting results and printing them
	}

}
