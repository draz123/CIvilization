package concurrent;

import java.io.IOException;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangRangeException;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpInputStream;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import abm.Cell;
import main.Global;
import map.MapHandler;
import visual.MapVisualizer;

public class ErlangSimulation {

	private MapHandler map;
	private MapVisualizer visual;
	static String erlangNodeName = "server"; // $ erl -sname server
    OtpNode javaNode;
    OtpMbox mailbox; 
	
	public ErlangSimulation(MapHandler map, MapVisualizer visual) {
		this.map = map;
		this.visual = visual;
	}
	
	public void start() {
        establishConnection();
        sendSimulationParameters(); // TODO
        sendFertilityMap();
        receiveAndVisualizeColorMap();
	}
	
	private void establishConnection() {
		try {
            javaNode = new OtpNode("java_node"); // this node's name
            mailbox = javaNode.createMbox();
            System.out.println("Connecting to the Erlang node...");
            if (javaNode.ping(erlangNodeName, 2000)) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Could not connect to the Erlang node, exit!");
                return;
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	private void sendSimulationParameters() {
		
	}
	
	private void sendFertilityMap() {
		int rows = map.getHeight();
		int cols = map.getWidth();
    	
		OtpErlangObject[] message = new OtpErlangObject[rows*cols + 1]; //jedynka dla identyfikatora procesu
		message[0] = mailbox.self(); // javaNode's PID
		
    	int currentCellIndex = 1;
    	
    	for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i, j);
				int currentCellFertility = currentCell.getFertility();
				message[currentCellIndex++] = new OtpErlangInt(currentCellFertility);
			}
		}
       
        OtpErlangList map = new OtpErlangList(message);
        mailbox.send("getMap", erlangNodeName, map);
        System.out.println("Map sent!");
	}
	
	private void receiveAndVisualizeColorMap() {
		int turn = 0;
		int rows = map.getHeight();
		int cols = map.getWidth();
		
        while (true) {
            try {            	
                OtpErlangObject erlangObject = mailbox.receive();
                
                if (erlangObject instanceof OtpErlangAtom && 
                		((OtpErlangAtom) (OtpErlangObject) erlangObject).atomValue().equals("stop")) {
                	System.out.println("Erlang told me to stahp... :c");
                	break;
                }

                if (!(erlangObject instanceof OtpErlangList)) 
                    throw new IllegalArgumentException("The data is not a list of rgb tuples!!");
                
                OtpErlangList list = (OtpErlangList) erlangObject;
                
                int listSize = list.arity();
                if (listSize != rows * cols) throw new IllegalArgumentException("The data is not complete!");
                OtpErlangObject[] elements = list.elements();
                
            	for (int i = 0; i < listSize; i++) {
					OtpErlangTuple rgb = (OtpErlangTuple) elements[i];
					int r = Integer.parseInt(rgb.elementAt(0).toString());
					int g = Integer.parseInt(rgb.elementAt(1).toString());
					int b = Integer.parseInt(rgb.elementAt(2).toString());
					
					int[] twoDimIndex = MapHandler.count2DimIndex(i, rows, cols);
					int row = twoDimIndex[0];
					int col = twoDimIndex[1];
					
					map.getCell(row, col).setColor(r, g, b);
            	} 
            	
            	visual.paintMap(map.getMap(), turn);
            	System.out.println("Simulation: turn " + turn);
                turn++;
            } catch (OtpErlangExit e) {
                e.printStackTrace();
                break;
            } catch (OtpErlangDecodeException e) {
                e.printStackTrace();
            }
        }     
	}
}
