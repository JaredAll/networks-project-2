package networks;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 

public class Proxy 
{
  
	public static void main( String[] args ) 
	{
    
		ServerSocket serverSocket = null;
	    Socket socket = null;
	    int MAX_THREADS = 10;
				    
	    try {
	    	
	    	// Socket used to listen for new clients
	    	serverSocket = new ServerSocket( 6969 );
	    	
	    	ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
	    	
	    	TimerTask RenewProxy = new TimerTask()
    		{
				RequestHandler renewer = new RequestHandler();
				
    			public void run()
    			{
    				renewer.renewCache();	
    			}
    			
    		};
    		
    		Timer timer = new Timer();
    		long tenMinutes = 600000L;
    		long delay = tenMinutes;
    		long period = tenMinutes;
    	    timer.scheduleAtFixedRate( RenewProxy, tenMinutes, tenMinutes );
    	    
    		System.out.println("\nServer initialized.");
	    	
	    	while( true )
	    	{   
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
