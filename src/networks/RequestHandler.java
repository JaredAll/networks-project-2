package networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.UnknownHostException;

public class RequestHandler extends Thread 
{
	private static Cache cache = new Cache();
	private Socket socket;
	private String response501;
	private String response400;
	
	public RequestHandler( Socket socket )
	{
		this.socket = socket;
		response501 = readFile( "501.html" );
		response400 = readFile( "400.html" );
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
	        System.out.println("\n" + request );
	        
	        String response = "";
	       
	        if( cache.hasWebPage( request ) ) 
	        {
	        	response = cache.getWebPage( request );
	        	System.out.println("This was cached.");
	        }
	        else
	        { 
	        		
	        	int pageStatus = webPageStatus( request.split(" ")[1], 0 );
	        		
	        	if(  pageStatus == 501)
	        	{
	        		response = response501;
	        	}
	        	else if( pageStatus == 400 )
	        	{
	    	    	System.out.println( "Error Code 400; notifying client.");
	        		response = response400;
	        	}
	        	else
	        	{
	        		// Retrieving, forwarding, and printing response
	        		response = sendWebRequest( request.split(" ")[1], 0 );
	        		System.out.println( "\n" + response );
	        		cache.updateWebMap( request, response );
	        		cache.updateTimeMap( request, System.currentTimeMillis() );
	        	}	
	        }
	        outStream.write( response.getBytes());
	        socket.close();
	    } 
	    catch (IOException io) 
	    {
	      io.printStackTrace();
	    }
	}
	
	//This function taken from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
	private String readFile( String filePath )
	{
		String content = "";
		try
		{
			content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}
	
	private static int webPageStatus( String targetURL, long ifModifiedSince )
	{
		int error400 = 400;
		WebRequest request = null;
	    try {
	      request = new WebRequest(targetURL); // Initializing web request
	      if (ifModifiedSince != 0) {        
		        request.setIfModifiedSince( ifModifiedSince ); // Setting if-modified-since header (pulled from timestamp hashmap)
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
	      //e.printStackTrace();
	      return error400;
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
