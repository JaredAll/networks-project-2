package networks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cache {
	
	Map<String, String> webMap;
	Map<String, Long> timeMap;

	public Cache()
	{
		webMap = Collections.synchronizedMap(new HashMap<>());
		timeMap = Collections.synchronizedMap(new HashMap<>());
	}
	
	//Updates the webMap with a new entry or a newer version of an old entry.
	public void updateWebMap(String URL, String WebPage)
	{
		webMap.put(URL, WebPage);		
	}
	
	//Returns the webpage value associated with a particular key.
	public String getWebPage(String URL)
	{
		return webMap.get(URL);
	}
	
	//Deletes a specific key-value mapping in the webMap.
	public void deleteWebPage(String URL)
	{
		webMap.remove(URL);
	}
	
	//Checks if the webMap has a specific key-value pair.
	public boolean hasWebPage(String URL)
	{
		return webMap.containsKey(URL);
	}
	
	//Updates timeMap with new key-value entry or a newer version of an old entry.
	public void updateTimeMap(String URL, long timeStamp)
	{
		timeMap.put(URL, timeStamp);
	}
	
	//Return time value associated with a specific key.
	public long getTimeStamp(String URL)
	{
		return timeMap.get(URL);
	}
	
	//Deletes a specific key-value mapping in the timeMap
	public void deleteTimeStamp(String URL)
	{
		timeMap.remove(URL);
	}
	
	
  /* Jon's reference code. (please don't delete)
  public static void main(String[] args) 
  {
    Map m = Collections.synchronizedMap(new HashMap());  
    m.put("google.com", "html doc google");
    m.put("twitter.com", "html doc twitter");
    m.put("wikipedia.org", "html doc wiki");
    m.put("truman.edu", "html doc truman");
    System.out.println(m.containsKey("google.com"));
    System.out.println(m.get("google.com"));
    System.out.println("Hello I'm just testing git!");
    
    // Code to calculate timestamp to store in timestamp hashmap
    System.out.println(System.currentTimeMillis());
  }
  */
  
  
}
