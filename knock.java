import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alex on 4/12/2015.
 */
public class knock {

    public static void main(String args[]) {
    	String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try {
    		Socket kkSocket = new Socket(hostName, portNumber);
    		PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
    		BufferedReader in = new BufferedReader(
        	new InputStreamReader(kkSocket.getInputStream()));
		}
    	catch(Exception e)
        {
            e.printStackTrace();
        }
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
    }
}
