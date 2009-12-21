package edu.wustl.caobr.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This has all methods that deals with REST API invocation
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class RestApiInvoker {
    private static final Logger logger = Logger.getLogger(RestApiInvoker.class);

    /**
     * It invokes given URL using GET method and returns the string of response 
     * @param targetUrl URL to invoke
     * @return HTTP response string
     */
    public static String getResult(String targetUrl) {
        logger.debug("using GET method, URL=" + targetUrl);
        StringBuilder retrunData = new StringBuilder();
        try {

            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestMethod("GET");

            conn.setDoOutput(true);
            conn.setUseCaches(false);

            InputStream inputStream = conn.getInputStream();

            InputStreamReader ins = new InputStreamReader(inputStream);
            BufferedReader in = new BufferedReader(ins);
            String data = in.readLine();
            if (data == null) {
                retrunData.append("<set/>");
            }
            while (data != null) {
                retrunData.append(data);
                data = in.readLine();
            }
            in.close();
        } catch (Exception e) {
            logger.debug("Could not get result from service  :" + targetUrl, e);
        }
        logger.debug("Result XML " + retrunData);
        return retrunData.toString();

    }

    /**
     * It invokes given URL using POST method and returns the string of response. 
     * It also passes the parameters to the call  
     * @param targetUrl URL to invoke
     * @param parameters request parameters
     * @return HTTP response string
     */
    public String getResultFromPost(String targetUrl, Map<String, String> paramtersMap) {
        logger.debug("using POST method, URL=" + targetUrl);

        StringBuilder parametersString = new StringBuilder();
        int i = 0;
        for (String key : paramtersMap.keySet()) {
            String parameterValue = paramtersMap.get(key);
            if (i == 0) {
                parametersString.append(key).append("=").append(parameterValue);
            } else {
                parametersString.append("&").append(key).append("=").append(parameterValue);
            }
            i++;
        }
        logger.debug("Paramters are " + parametersString);
        
        StringBuilder retrunData = new StringBuilder();
        try {

            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setUseCaches(false);

            //Send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(parametersString.toString());
            wr.flush();
            wr.close();

            //Get Response    

            InputStream inputStream = conn.getInputStream();

            InputStreamReader ins = new InputStreamReader(inputStream);
            BufferedReader in = new BufferedReader(ins);
            String data = in.readLine();
            if (data == null) {
                retrunData.append("<set/>");
            }
            while (data != null) {
                retrunData.append(data);
                data = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("Result XML " + retrunData);
        return retrunData.toString();
    }
}