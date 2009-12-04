package edu.wustl.caobr.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Concept;
import edu.wustl.caobr.service.util.SearchBean;

/**
 * This has all methods that deals with REST API invocation
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class RestApiInvoker {

    /**
     * It invokes given URL using GET method and returns the string of response 
     * @param targetUrl URL to invoke
     * @return HTTP response string
     */
    public static String getResult(String targetUrl) {

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
        } catch (RemoteException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrunData.toString();

    }

    /**
     * It invokes given URL using POST method and returns the string of response. 
     * It also passes the parameters to the call  
     * @param targetUrl URL to invoke
     * @param parameters request parameters
     * @return HTTP response string
     */
    public String getResultFromPost(String targetUrl, String parameters) {

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
            wr.writeBytes(parameters);
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
//        System.out.println("Printing..................");
//        System.out.println(retrunData);
        return retrunData.toString();

    }
    /**
     * This parses the given XML string to generate list of Search Bean
     * @param targetUrl
     * @return
     */
    public static List<SearchBean> getSearchBeans(String targetUrl) {
        List<SearchBean> seachBeans = new XmlToObjectTransformer().toSearchBean(getResult(targetUrl));
        return seachBeans;
    }

    /**
     * @param url
     * @return
     */
    public static List<Concept> getConcepts(String url) {
        return new XmlToObjectTransformer().toConcepts(getResult(url));

    }
}