package networks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class WebRequest {

  // Connection variables
  private String targetURL;
  private URL url;
  private URLConnection urlConnection;
  private HttpURLConnection httpConnection;
  
  // Information variables
  private String htmlResponse;
  private String lastModified;
  private String ifModifiedSince;
  private int statusCode;
  private boolean connected;
  
  // Initializing a web request, setting up the connections
  public WebRequest(String targetURL) throws IOException {
    this.targetURL = targetURL;
    this.url = new URL(this.targetURL);
    this.urlConnection = url.openConnection();
    this.httpConnection = (HttpURLConnection)urlConnection;
    this.httpConnection.setRequestMethod("GET");
    this.htmlResponse = null;
    this.connected = false;
  }
  
  // Set the if-modified-since header field
  // Must be done BEFORE connecting
  public void setIfModifiedSince(long timestamp) throws IOException {
    httpConnection.setIfModifiedSince(timestamp);
    this.ifModifiedSince = new Date(httpConnection.getIfModifiedSince()).toString();
  }
  
  // Connects the httpConnection
  // Required for access to the status code and last-modified and if-modified-since header fields
  public void connect() throws IOException {
    this.httpConnection.connect();
    this.connected = true;
    this.statusCode = this.httpConnection.getResponseCode();
    this.ifModifiedSince = new Date(httpConnection.getIfModifiedSince()).toString();
    this.lastModified = new Date(httpConnection.getLastModified()).toString();
  }
  
  // Sends http request to the targetURL specified in constructor
  // Stores html response in this.htmlResponse
  public void sendRequest() throws IOException {
    if (!connected) {
      this.connect();
    }
    BufferedReader in = new BufferedReader(new InputStreamReader(this.urlConnection.getInputStream()));
    String inputLine;
    String response = "";
    while ((inputLine = in.readLine()) != null) 
        response += inputLine;
    in.close();
    this.htmlResponse = response;
  }
  
  // Returns the html response
  public String getResponse() {
    return this.htmlResponse;
  }
  
  // Returns the http status code
  public int getStatusCode() {
    return this.statusCode;
  }
  
  // Returns the if-modified-since header field
  public String getIfModifiedSince() {
    return this.ifModifiedSince;
  }

  // Returns the last-modified header field
  public String getLastModified() {
    return this.lastModified;
  }
  
}
