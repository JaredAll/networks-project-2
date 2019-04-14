package networks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cache {
  
  public static void main(String[] args) {
    Map m = Collections.synchronizedMap(new HashMap());  
    m.put("google.com", "html doc google");
    m.put("twitter.com", "html doc twitter");
    m.put("wikipedia.org", "html doc wiki");
    m.put("truman.edu", "html doc truman");
    System.out.println(m.containsKey("google.com"));
    System.out.println(m.get("google.com"));
    
    // Code to calculate timestamp to store in timestamp hashmap
    System.out.println(System.currentTimeMillis());
  }
  
}
