import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * This class builds io streams with the client, handling request, sending responds and logging.
 * @author 200009734
 *
 */
public class ClientHandler extends Thread{
	private Socket socket;
	private String dirPath;
	private Date date;
	private InputStream in;
	private OutputStream out;
	private BufferedReader br;
	static final String OK = "200";
	static final String NotFound = "404";
	static final String NotImplemented = "501";
	/**
	 * Everytime a thread is made, the connected client no. increases by 1,
	 * input and output streams are built.
	 * @param socket The socket for communicating with the client.
	 * @param dirPath The directory from which the server server serves the client.
	 */
	public ClientHandler(Socket socket, String dirPath) {
		this.socket = socket;
		this.dirPath = dirPath;
		this.date = new Date();
		WebServerMain.clientConnectedNo += 1;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(in));
		}catch(IOException ioe){
			System.out.println("Instantiating ClientHandler: " + ioe.getMessage());
		}
		
	}
	/**
	 * Response for "GET" and "HEAD" are implemented in this method. 
	 * Either images or texts can by sent by bytes.
	 * Request information is logged into a file after each response.
	 */
	public void run(){
		/*
		 * read each line of the request into an ArrayList of String.
		 */
		String line;
		ArrayList<String> requestLines = new ArrayList<String>();
		try {
			line = br.readLine();
			while(!line.isBlank()){
				requestLines.add(line);
				line = br.readLine();
			}
			/*
			 * split the first request line into parts including request type and file path.
			 */
			String[] parts = requestLines.get(0).split(" ");
			String requestType = parts[0];
			String filePath = parts[1];
			File file = new File(dirPath, filePath);
			if(requestType.equals("HEAD")) {
				/*
				 * for request type "HEAD",
				 * if file requested exists, send back "HTTP/1.1 200 OK" and content type and length of the file,
				 * else send back "HTTP/1.1 404 Not Found".
				 */
				if(file.exists()) {
					out.write(("HTTP/1.1 " + OK + " OK" + "\n").getBytes());
					out.write(("Content-Type: " + Files.probeContentType(Path.of(dirPath, filePath)) + "\n").getBytes());
					out.write(("Content-Length: " + Files.size(Path.of(dirPath, filePath)) + "\n").getBytes());
					logging(requestType, OK);
				}else {
					out.write(("HTTP/1.1 " + NotFound + " Not Found" + "\n").getBytes());
					logging(requestType, NotFound);
				}
			}else if(requestType.equals("GET")) {
				/*
				 * for request type "GET",
				 * if file requested exists, send proper header followed by the content of the file as body
				 * (either images or texts can be sent to the client),
				 * else send back "HTTP/1.1 404 Not Found"
				 */
				if(file.exists()) {
					out.write(("HTTP/1.1 " + OK + " OK" + "\n").getBytes());
					out.write(("Content-Type: " + Files.probeContentType(Path.of(dirPath, filePath)) + "\n").getBytes());
					out.write(("Content-Length: " + + Files.size(Path.of(dirPath, filePath)) + "\n\n").getBytes());
					FileInputStream fileIn = new FileInputStream(file);
					byte[] buf = new byte[1024];
					int length = 0;
					while((length = fileIn.read(buf))!= -1){
						out.write(buf, 0, length);					
					}
					fileIn.close();
					logging(requestType, OK);
				}else {
					out.write(("HTTP/1.1 " + NotFound + " Not Found" + "\n").getBytes());
					logging(requestType, NotFound);
				}
			}else {
				/*
				 * if the request type is neither "GET" or "HEAD",
				 * send back "HTTP/1.1 501 Not Implemented"
				 */
				out.write(("HTTP/1.1 " + NotImplemented + " Not Implemented" + "\n").getBytes());
				logging(requestType, NotImplemented);
			}
			out.flush();
			socket.close();
			WebServerMain.clientConnectedNo -= 1; // connected client no. gets decreased by 1 after the connection is closed.
			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
	}
	/**
	 * This method logs request time, type and respond code into a file "Log.txt",
	 * by appending these information at the end of the file.
	 * If the Log.txt does not exist, create one.
	 * @param requestType
	 * @param responseCode
	 */
	private synchronized void logging(String requestType, String responseCode) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try{
			File logFile = new File("Log.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile,true));
			PrintWriter pw = new PrintWriter(bw);
			pw.println(timeFormat.format(date));
			pw.println("Request Type" + requestType);
			pw.println("Response Code: " + responseCode);
			pw.println("");
			bw.close();
		}catch(IOException ioe){
			System.out.println("IOException caught when wrting to file: " + ioe.getMessage());
		}
	}
}
