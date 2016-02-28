package io.github.galaipa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class WebAPI {
    public static Boolean telegramBidali(String testua, String jokalaria) {
        URL url;
        try {
            String a =  ("http://gamerauntsia.eus/api/1.0/send_mctelebot_msg/" + jokalaria +"/?text=" + testua );
            a = a.replaceAll("\\s+", "%20");
            System.out.println(a);
            url = new URL(a);
            URLConnection conn = url.openConnection();

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    //System.out.println(inputLine);
                    if(inputLine.equalsIgnoreCase("true")){
                        return true;
                    }
                    else{           
                    }
                }
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return false;
    }
    public static JSONObject web(String name){
        StringBuilder content = new StringBuilder();
        try
    {
            URL url =  new URL("http://gamerauntsia.eus/api/1.0/get_minecraft_user?username=" + name);
            URLConnection urlConnection = url.openConnection();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line + "\n");
                } 
            }
        }
        catch(Exception e){
          return null;
        }
            try{
            String lortu = content.toString();
            JSONObject jo = (JSONObject) new JSONParser().parse(lortu);
            return jo;
            }
            catch(Exception e){
                  return null; 
            }
      }
}
