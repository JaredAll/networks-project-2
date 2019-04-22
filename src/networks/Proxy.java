package networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {
  
  public static void main(String[] args) {
    
    Cache cache = new Cache();
    
    ServerSocket serverSocket = null;
    Socket socket = null;
    InputStream inStream = null;
    OutputStream outStream = null;
    
    try {
      
      // Socket used to listen for new clients
      serverSocket = new ServerSocket(6969);
      
      // Main loop that receives incoming requests and handles them accordingly
      while (true) {
        
        System.out.println("\nWaiting for a client...");
        
        // Setting up socket and streams, waits on .accept() until a new client appears
        socket = serverSocket.accept();
        inStream = socket.getInputStream();        
        outStream = socket.getOutputStream();
        
        // Printing proxy server port and client port
        System.out.println("\nConnected");
        System.out.println("Client Port: " + socket.getPort());
        System.out.println("Server Port: " + socket.getLocalPort());
        
        // Receiving and printing wget's HTTP request
        int available = inStream.available();
        byte[] b = new byte[available];
        inStream.read(b, 0, available);
        String request = new String(b);
        System.out.println("\n" + request);
        
        // Retreiving, forwarding, and printing response
        String response = sendWebRequest(request.split(" ")[1], 0);
        outStream.write(response.getBytes());
        System.out.println("\n" + response);
        socket.close();
        
      }
    } catch (IOException io) {
      io.printStackTrace();
    }
    
  }
  
  private static String sendWebRequest(String targetURL, int ifModifiedSince) {
    WebRequest request = null;
    try {
      request = new WebRequest(targetURL); // Initializing web request
      if (ifModifiedSince != 0) {        
        request.setIfModifiedSince(System.currentTimeMillis() - ifModifiedSince); // Setting if-modified-since header (pulled from timestamp hashmap)
      }
      request.connect(); // Start the connection
      request.sendRequest(); // Sending http request
      // If request status code = 304, no html response update is required, but we should update the timestamp for the request
      System.out.println("Status: " + request.getStatusCode() + "\nLast Modified: " + request.getLastModified() + "\nIf Modified Since: " + request.getIfModifiedSince());
      return request.getResponse();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
}
