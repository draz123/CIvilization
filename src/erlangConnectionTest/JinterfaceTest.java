package erlangConnectionTest;

import java.io.IOException;
import com.ericsson.otp.erlang.*;
 
public class JinterfaceTest {
    static String server = "server";
 
    public static void main(String[] _args) throws Exception {
    	OtpNode self = null;
        OtpMbox mbox = null;
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
 
        OtpErlangObject[] msg = new OtpErlangObject[4];
        msg[0] = mbox.self(); //pierwszym elementem jest PID nodA javy
        msg[1] = new OtpErlangAtom("ping1");
        msg[2] = new OtpErlangAtom("ping2");
        msg[3] = new OtpErlangAtom("ping3");
        
       // OtpErlangTuple tuple = new OtpErlangTuple(msg);
        OtpErlangList list = new OtpErlangList(msg);
        //mbox.send("pong", server, tuple);
        mbox.send("pong", server, list);
        System.out.println("wyslane");
        
        while (true)
            try {
                OtpErlangObject erlangObject = mbox.receive();  
                System.out.println("erlangObject.toString()" + erlangObject.toString());
                
                OtpErlangTuple erlangTuple = (OtpErlangTuple) erlangObject; //na poczatku odbieram krotke, pierwszy element to atom z ID procesu
                OtpErlangPid fromPid = (OtpErlangPid)erlangTuple.elementAt(0); //wyciagam ID, to dziala jak nalezy
                OtpErlangList rList = (OtpErlangList) erlangObject;
                
                
                //OtpErlangList rList = new OtpErlangList(rtuple.elementAt(1)); //wyciagam liste z krotki
                OtpErlangObject[] a = new OtpErlangObject[4];
               // a = rList.elements();
                System.out.println(rList.elementAt(0).toString());
                System.out.println(rList.elementAt(1).toString());
                System.out.println(rList.elementAt(2).toString());
                System.out.println(rList.elementAt(3));
                //OtpErlangObject rmsg = rList.elementAt(1);
                System.out.println("Message: " + a[0] + " received from:  " + fromPid.toString());
                
                //System.out.println("Message: " + rmsg + " received from:  " + fromPid.toString());
 
                OtpErlangAtom ok = new OtpErlangAtom("stop");
                mbox.send(fromPid, ok);
                break;
 
            } catch (OtpErlangExit e) {
                e.printStackTrace();
                break;
            } catch (OtpErlangDecodeException e) {
                e.printStackTrace();
            }
    }
}