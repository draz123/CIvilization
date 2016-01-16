package erlangConnTest;

import java.io.IOException;
import com.ericsson.otp.erlang.*;

import abm.Cell;
import map.MapHandler;
 
public class JinterfaceTest {
    static String server = "server";
    OtpNode self;
    OtpMbox mbox; 
    
    public JinterfaceTest() {
    	self = null;
    	mbox = null;
    }
    
    public void test(MapHandler map) throws Exception {
    	OtpErlangObject[] msgToSend = new OtpErlangObject[map.getWidth()*map.getHeight() + 1]; //jedynka dla identyfikatora procesu
    	int erlangListCounter = 1;
    	
    	System.out.println("Przed petlami = " + map.getWidth()*map.getHeight() + 1);
    	
    	for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				Cell currentCell = map.getCell(i,j);
				Integer currentCellFertility = new Integer(currentCell.getFertility());
				msgToSend[erlangListCounter++] = new OtpErlangAtom(currentCellFertility.toString());
			}
		}
    	
    	
        try {
            self = new OtpNode("mynode", "test");
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
 
        //OtpErlangObject[] msgToSend = new OtpErlangObject[4];
        msgToSend[0] = mbox.self(); //pierwszym elementem jest PID nodA javy
       // msgToSend[1] = new OtpErlangAtom("ping1");
       // msgToSend[2] = new OtpErlangAtom("ping2");
       // msgToSend[3] = new OtpErlangAtom("ping3");
        
       // OtpErlangTuple tuple = new OtpErlangTuple(msgToSend);
        OtpErlangList list = new OtpErlangList(msgToSend);
        //mbox.send("pong", server, tuple);
        mbox.send("pong", server, list);
        System.out.println("wyslane");
        
        while (true)
            try {
                OtpErlangObject erlangObject = mbox.receive();  
                System.out.println("erlangObject.toString()" + erlangObject.toString());
                
                OtpErlangTuple erlangTuple = (OtpErlangTuple) erlangObject; //na poczatku odbieram krotke, pierwszy element to atom z ID procesu
                OtpErlangPid erlangProcessPid = (OtpErlangPid)erlangTuple.elementAt(0); //wyciagam ID, to dziala jak nalezy
                
                OtpErlangObject[] erlangObjectArray = erlangTuple.elements();
                
                for (int i = 0; i < erlangObjectArray.length; i++) {
                	System.out.println(i + " = " + erlangObjectArray[i].toString());
				}
                
                System.out.println("Message: " + erlangObjectArray.toString() + " received from:  " + erlangProcessPid.toString());
                
                //System.out.println("Message: " + rmsg + " received from:  " + fromPid.toString());
                
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