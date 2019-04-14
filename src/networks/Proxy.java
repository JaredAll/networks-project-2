package networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {
  
  public static void main(String[] args) {
    
    Cache cache = new Cache();

    // Sample code to make a web request
    WebRequest request = null;
    try {
      request = new WebRequest("https://google.com/"); // Initializing web request
      request.setIfModifiedSince(System.currentTimeMillis()); // Setting if-modified-since header (pulled from timestamp hashmap)
      request.connect(); // Start the connection
      request.sendRequest(); // Sending http request
      // If request status code = 304, no html response update is required, but we should update the timestamp for the request
      System.out.println("Status: " + request.getStatusCode() + "\nLast Modified: " + request.getLastModified() + "\nIf Modified Since: " + request.getIfModifiedSince());
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    ServerSocket serverSocket = null;
    Socket socket = null;
    InputStream inStream = null;
    OutputStream outStream = null;
    try {
      serverSocket = new ServerSocket(6969);
      // Main loop that receives incoming requests and handles them accordingly
      while (true) {
        socket = serverSocket.accept();
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
        System.out.println("Connected");
        System.out.println(socket.getPort());
        System.out.println(socket.getLocalPort());
        outStream.write(request.getResponse().getBytes());
      }     
    } catch (IOException io) {
      io.printStackTrace();
    }
    
  }
  
}
