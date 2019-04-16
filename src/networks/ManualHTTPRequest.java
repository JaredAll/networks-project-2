package networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ManualHTTPRequest {

  public static void main(String[] args) {
    
    ServerSocket serverSocket = null;
    Socket socket = null;
    InputStream inStream = null;
    OutputStream outStream = null;
    
    Socket clientSocket = null;
    InputStream clientInStream = null;
    OutputStream clientOutStream = null;
    
    try {
      serverSocket = new ServerSocket(6969);
      while (true) {
        // Getting request from wget
        socket = serverSocket.accept();
        inStream = socket.getInputStream();        
        outStream = socket.getOutputStream();
        System.out.println("\nConnected");
        System.out.println("Client Port: " + socket.getPort());
        System.out.println("Server Port: " + socket.getLocalPort());
        int available = inStream.available();
        byte[] b = new byte[available];
        inStream.read(b, 0, available);
        System.out.println("\n" + new String(b));
        
        // Forwarding request
        clientSocket = new Socket(InetAddress.getByName("www.google.com"), 80);
        clientInStream = clientSocket.getInputStream();
        clientOutStream = clientSocket.getOutputStream();
        clientOutStream.write(b);
        
        // Retrieving request
        Thread.sleep(5000);
        byte[] n = new byte[clientInStream.available()];
        clientInStream.read(n, 0, clientInStream.available());
        System.out.println("\n" + new String(n));
      }     
    } catch (IOException io) {
      io.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
  }
  
}
