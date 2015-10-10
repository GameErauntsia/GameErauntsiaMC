package io.github.galaipa;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Json {
    public static String pluginFolder;
    
    public static void idatziJSON(JSONObject obj,String jokalaria, Boolean berretsi) {
        JSONArray jokalariak = new JSONArray();
        File file = new File(pluginFolder + File.separator + "jokalariak.json");
        try{
            File filePath = new File(pluginFolder + File.separator + "");
            filePath.mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                }
            JSONParser parser = new JSONParser();
            Object jokalari = parser.parse(new FileReader(file));
            if(jokalari != null && jokalari instanceof JSONArray){
                jokalariak = (JSONArray) jokalari;
                if(berretsi){
                for (int i = 0; i < jokalariak.size(); i++) {
                  JSONObject o = (JSONObject) jokalariak.get(i);
                  if(o.get("Erabiltzailea").equals(jokalaria)){
                      jokalariak.remove(o);
                  }
                }
                }
            }
            if(obj != null){
              jokalariak.add(obj);
            }
            FileWriter fileWriter = new FileWriter(file); 
            fileWriter.write(jokalariak.toJSONString());
            fileWriter.flush(); 
            fileWriter.close(); 
            
    } catch (IOException | ParseException e) {}
    }public static void ezabatuJSON(String jokalaria) {
        if(jokalaria != null){
            idatziJSON(null,jokalaria,true);
        }
    }
    public static  JSONObject irakurriJSON(String gakoa, String jokalaria) {
        JSONArray jokalariak = new JSONArray();
        try {
          JSONParser parser = new JSONParser();
          File file = new File(pluginFolder + File.separator + "jokalariak.json");
          Object jokalari = parser.parse(new FileReader(file));

          if(jokalari != null && jokalari instanceof JSONArray){
              jokalariak = (JSONArray) jokalari;
              for (Object jokalariak1 : jokalariak) {
                  JSONObject o = (JSONObject) jokalariak1;
                  if(o.get(gakoa).equals(jokalaria)){
                      return o;
                  }
              }
          }
        } catch (Exception e) {}
        return null;
    }public static  JSONArray zerrendaJSON() {
        try {
          JSONParser parser = new JSONParser();
          File file = new File(pluginFolder + File.separator + "jokalariak.json");
          Object jokalari = parser.parse(new FileReader(file));
          if(jokalari != null && jokalari instanceof JSONArray){
              return (JSONArray) jokalari;
          }
        } catch (Exception e) {}
        return null;
    }
    
}
