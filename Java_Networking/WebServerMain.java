import java.io.IOException;
import java.net.*;
/**
 * 
 * @author 200009734
 *
 */
public class WebServerMain {
	static String dirPath;
	static int port;
	static int clientConnectedNo = 0;
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Usage: java WebServerMain <document_root> <port>");
			System.exit(0);
		}
		/*
		 * if the second argument is not a positive integer,
		 * a exception will be thrown either by the system or a line of code.
		 */
		try {
			port = Integer.valueOf(args[1]);
			if(port <= 0) {
				throw new Exception();
			}
		}catch(Exception e) {
			System.out.println("Port number has to be a positive integer.");
			System.exit(0);
		}
		dirPath = args[0];
		/*
		 * create a TCP server socket listening on the specific port.
		 * the server can support up to 5 concurrent client connection requests.
		 */
		try(ServerSocket ss = new ServerSocket(port);){
			while(true) {
				if(clientConnectedNo <= 5) {
					Socket conn = ss.accept();
					/*
					 * create a thread for this client connection.
					 */
					ClientHandler ch = new ClientHandler(conn, dirPath);
					ch.start();		
				}	
			}
		} catch (IOException ioe) {
			System.out.println("WebServerMain: " + ioe.getMessage());
		}
	 
	}
}
