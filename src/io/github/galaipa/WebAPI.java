package io.github.galaipa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WebAPI {
    public static Boolean telegramBidali(String testua, String jokalaria){
        URL url;
        try {
            String a =  ("https://gamerauntsia.eus/api/1.0/send_mctelebot_msg/" + jokalaria +"/?text=" + testua );
            a = a.replaceAll("\\s+", "%20");
            System.out.println(a);
            url = new URL(a);
            URLConnection conn = url.openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if(inputLine.equalsIgnoreCase("true")){
                        return true;
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
        try{
            URL url =  new URL("https://gamerauntsia.eus/api/1.0/get_minecraft_user?username=" + name);
            URLConnection urlConnection = url.openConnection();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line).append("\n");
                } 
            }
        }
        catch(IOException e){
        }
        try{
            String lortu = content.toString();
            System.out.println("Content:" +lortu);
            JSONObject jo = (JSONObject) new JSONParser().parse(lortu);
            return jo;
        }
        catch(ParseException e){
        }
        return null;
      }
    
    // Iturria: http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
    public static void httpsOn(){
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(WebAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException ex) {
            Logger.getLogger(WebAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}

