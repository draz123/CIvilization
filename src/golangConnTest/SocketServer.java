package golangConnTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	private static ServerSocket serverSocket;

	public static void startServer() {
		boolean runningFlag = true;
		
		int portNumber = 8888;
		
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server started with port: " + portNumber);
			//PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			//BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out;
			BufferedReader in;
			
			while(runningFlag) {
				Socket clientSocket = serverSocket.accept();
				
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					out.println(inputLine);
					System.out.println(inputLine);
					if(inputLine.matches(".*stop.*")) {
						System.out.println("...kill me please... ");
						runningFlag = false;
					}
				}
			}
			
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		
		if(!serverSocket.isClosed())
			try {
				serverSocket.close();
				System.out.println("Server closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public static void main(String args[]) {
		startServer();
	}
}
