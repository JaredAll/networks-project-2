package networks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class WebTest {

  public static void main(String[] args) throws IOException {
    String targetURL = "http://www.google.com/index.htm";
    //System.out.println("Response: " + executePost(targetURL, ""));
    //System.out.println("Response: " + sendPost(targetURL));
    System.out.println("Response: " + TCPPost(targetURL));
  }
  
  public static String executePost(String targetURL, String urlParameters) {
    HttpURLConnection connection = null;
    try {
      // Create connection
      URL url = new URL(targetURL);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
      connection.setRequestProperty("Content-Language", "en-US");  
      connection.setUseCaches(false);
      connection.setDoOutput(true);

      // Send request
      DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.close();

      // Get Response  
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();
      return response.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
  
  public static String sendPost(String targetURL) throws IOException {
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
  
  public static String TCPPost(String targetURL) throws IOException {
    String pageAddr = targetURL;
    URL url = new URL(pageAddr);
    String websiteAddress = url.getHost();

    String file = url.getFile();
    Socket clientSocket = new Socket(websiteAddress, 80);

    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    OutputStreamWriter outWriter = new OutputStreamWriter(clientSocket.getOutputStream());
    outWriter.write("GET " + file + " HTTP/1.0\r\n\n");
    outWriter.flush();
    BufferedWriter out = new BufferedWriter(new FileWriter(file));
    boolean more = true;
    String input;
    while (more) {
      input = inFromServer.readLine();
      if (input == null)
        more = false;
      else {
        out.write(input);
      }
    }
    out.close();
    clientSocket.close();
    return "";
  }


}
