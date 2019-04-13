package networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class WebTest {

  public static void main(String[] args) throws IOException {
    
    // Creating URL object of website to talk to
    String targetURL = "https://www.wikipedia.org/";
    URL url = new URL(targetURL);
    
    // Creating URL connection and http URL connection
    URLConnection urlConnection = url.openConnection();
    HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;
    
    // Setting If-Modified-Since header field to 1 hour ago
    httpConnection.setIfModifiedSince(System.currentTimeMillis()-1000*60*60);
    
    // Setting request method to GET and opening http URL connection
    httpConnection.setRequestMethod("GET");
    httpConnection.connect();
    
    // Printing out Status Code
    System.out.println("Code: " + httpConnection.getResponseCode());
    
    // Printing out If-Modified-Since header field
    System.out.println("If-Modified-Since: " + new Date(httpConnection.getIfModifiedSince()).toString());
    
    // Printing out Last-Modified header field
    System.out.println("Last-Modified: " + new Date(httpConnection.getLastModified()).toString());
    
    // Printing out http request response
    //System.out.println("Response: " + sendRequest(urlConnection));
    
  }
  
  public static String sendRequest(String targetURL) throws IOException {
    URL url = new URL(targetURL);
    URLConnection urlCon = url.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
    String inputLine;
    String response = "";
    while ((inputLine = in.readLine()) != null) 
        response += inputLine;
    in.close();
    return response;
  }

}
