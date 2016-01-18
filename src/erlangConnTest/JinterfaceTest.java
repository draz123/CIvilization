package erlangConnTest;

import java.io.IOException;
import com.ericsson.otp.erlang.*;

import abm.Cell;
import map.MapHandler;
 
public class JinterfaceTest {
	
    static String server = "server";
    OtpNode self;
    OtpMbox mbox; 
    
    public void test(MapHandler map) throws Exception {
        try {
            self = new OtpNode("mynode", "test"); // node, cookie
            mbox = self.createMbox("facserver");
 
            if (self.ping(server, 2000)) {
                System.out.println("remote is up");
            } else {
                System.out.println("remote is not up");
                return;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
    	int rows = map.getHeight();
		int cols = map.getWidth();
    	
		OtpErlangObject[] msgToSend = new OtpErlangObject[rows*cols + 1]; //jedynka dla identyfikatora procesu
		msgToSend[0] = mbox.self(); //pierwszym elementem jest PID procesu utworzonego w Jabie
		
    	int erlangListCounter = 1;
    	
    	for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell currentCell = map.getCell(i,j);
				//Integer currentCellFertility = new Integer(currentCell.getFertility());
				int currentCellFertility = currentCell.getFertility();
				msgToSend[erlangListCounter++] = new OtpErlangInt(currentCellFertility);
			}
		}
       
        OtpErlangList list = new OtpErlangList(msgToSend);
        mbox.send("pong", server, list);
        System.out.println("wyslane");
        
        while (true) {
            try {
                OtpErlangObject erlangObject = mbox.receive();
                System.out.println("erlangObject.toString()" + erlangObject.toString());
                
                OtpErlangTuple erlangTuple = (OtpErlangTuple) erlangObject; //odbieram KROTKE (za nic nie da sie odebrac listy), pierwszy element to atom z ID procesu
                OtpErlangPid erlangProcessPid = (OtpErlangPid)erlangTuple.elementAt(0); //wyciagam ID, to dziala jak nalezy
                
                OtpErlangObject[] erlangObjectArray = erlangTuple.elements();
                
                for (int i = 0; i < erlangObjectArray.length; i++) {
                	if(i>0) {
                		int liczba = Integer.parseInt(erlangObjectArray[i].toString());;
                		System.out.println(i + " = " + liczba);
                	}
				}
                
                System.out.println("Message: " + erlangObjectArray.toString() + " received from:  " + erlangProcessPid.toString());
                              
                OtpErlangAtom ok = new OtpErlangAtom("stop"); //atom 'stop' zatrzymuje dzialanie procesu w erlangu
                mbox.send(erlangProcessPid, ok);
                break;
 
            } catch (OtpErlangExit e) {
                e.printStackTrace();
                break;
            } catch (OtpErlangDecodeException e) {
                e.printStackTrace();
            }
        }
    }

    
}