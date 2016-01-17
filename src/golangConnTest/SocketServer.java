package golangConnTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
	public static void startServer() {
		
		while(true) {
			System.out.println("while poszedl");
			int portNumber = 8888;
			
			
		    try (
		        ServerSocket serverSocket = new ServerSocket(portNumber);
		        Socket clientSocket = serverSocket.accept();     
		        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
		        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    ) {
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) {
		            out.println(inputLine);
		            System.out.println(inputLine);
		        }
		    } catch (IOException e) {
		        System.out.println("Exception caught when trying to listen on port "
		            + portNumber + " or listening for a connection");
		        System.out.println(e.getMessage());
		    }
		}
	}
	public static void main(String args[]) {
		startServer();
	}
}
