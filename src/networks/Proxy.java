package networks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 

public class Proxy 
{
  
	public static void main(String[] args) 
	{
    
		ServerSocket serverSocket = null;
	    Socket socket = null;
	    int MAX_T = 10;
				    
	    try {
	    	
	    	// Socket used to listen for new clients
	    	serverSocket = new ServerSocket( 6969 );
	    	
	    	ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
	    	
	    	while( true )
	    	{
		    	
	    		System.out.println("\nWaiting for a client...");
		        
	    		// Setting up socket and streams, waits on .accept() until a new client appears
	    		socket = serverSocket.accept();
	    		RequestHandler handler = new RequestHandler( socket );
	    		pool.execute( handler );
	    	}
	    }
	    catch( IOException e)
	    {
	    	e.printStackTrace();
	    }
	    
	}
}
