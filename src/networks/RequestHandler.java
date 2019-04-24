package networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;

public class RequestHandler extends Thread 
{
	private static Cache cache = new Cache();
	private Socket socket;
	
	public RequestHandler( Socket socket )
	{
		this.socket = socket;
	}
	
	public RequestHandler()
	{
		this.socket = null;
	}
	
	
	public void renewCache()
	{
		Set<String> keys = cache.getWebMapKeys();
		for( String key : keys )
		{
			int notModified = 304;
			if( webPageStatus( key.split(" ")[1], cache.getTimeStamp( key ) ) != notModified )
			{
				String response = sendWebRequest( key.split(" ")[1], 0 );
				cache.updateTimeMap(key, System.currentTimeMillis());
				cache.updateWebMap(key, response);
			}
			else
			{
				cache.updateTimeMap( key, System.currentTimeMillis() );
			}
		}
	}
	
	public void run()
	{
	    InputStream inStream = null;
	    OutputStream outStream = null;
	    
	    try {
	    	
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
	       
	        if( cache.hasWebPage( request ) ) 
	        {
	        	String response = cache.getWebPage( request );
	        	outStream.write( response.getBytes() );
	        	System.out.println("This was cached.");
	        	socket.close();
	        }
	        else
	        {  
	        	// Retrieving, forwarding, and printing response
	        	String response = sendWebRequest( request.split(" ")[1], 0 );
	        	outStream.write( response.getBytes() );
	        	System.out.println( "\n" + response );
	        	cache.updateWebMap( request, response );
	        	cache.updateTimeMap( request, System.currentTimeMillis() );
	        	socket.close();
	        	
	        }
	    } 
	    catch (IOException io) 
	    {
	      io.printStackTrace();
	    }
	}
	
	private static int webPageStatus( String targetURL, long ifModifiedSince )
	{
		WebRequest request = null;
	    try {
	      request = new WebRequest(targetURL); // Initializing web request
	      if (ifModifiedSince != 0) {        
		        request.setIfModifiedSince(ifModifiedSince); // Setting if-modified-since header (pulled from timestamp hashmap)
		      }
	      request.connect(); // Start the connection
	      request.sendRequest(); // Sending http request
	      // If request status code = 304, no html response update is required, but we should update the timestamp for the request
	      /*DEBUG*/
	      System.out.println( "getting status for " + targetURL + "..." );
	      System.out.println( "Status is " + request.getStatusCode());
	      return request.getStatusCode();
	    } 
	    catch (IOException e) 
	    {
	      e.printStackTrace();
	      return 0;
	    }
	}
	
	private static String sendWebRequest(String targetURL, long ifModifiedSince) 
	  {
	    WebRequest request = null;
	    try {
	      request = new WebRequest(targetURL); // Initializing web request
	      if (ifModifiedSince != 0) {        
	        request.setIfModifiedSince(System.currentTimeMillis() - ifModifiedSince); // Setting if-modified-since header (pulled from timestamp hashmap)
	      }
	      request.connect(); // Start the connection
	      request.sendRequest(); // Sending http request
	      // If request status code = 304, no html response update is required, but we should update the timestamp for the request
	      System.out.println("Status: " + request.getStatusCode() + "\nLast Modified: " + request.getLastModified() + 
	    		  "\nIf Modified Since: " + request.getIfModifiedSince());
	      return request.getResponse();
	    } 
	    catch (IOException e) 
	    {
	      e.printStackTrace();
	      return null;
	    }
	  }
}
