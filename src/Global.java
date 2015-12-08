
public class Global {

	private static final int TURNS = 1000;

	public static void main(String args[]){
		//Reading map
		
		//Setting civilizations?
		Simulation s=new Simulation();
		s.setMap();
		
		for(int i=0; i< TURNS;i++){
		//Civilization grown process
			s.doTurn();
		}
		
		
		//Getting results and printing them
	}

}
