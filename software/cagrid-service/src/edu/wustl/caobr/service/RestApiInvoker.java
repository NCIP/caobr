package edu.wustl.caobr.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Concept;
import edu.wustl.caobr.service.util.SearchBean;

/**
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class RestApiInvoker {

    /**
     * @param targetUrl
     * @return It calls the GET method on REST services
     * @throws RemoteException
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
     * @param targetUrl
     * @param parameters
     * @return It calls the POST method on REST services
     * @throws RemoteException
     */
    public String getResultFromPost(String targetUrl, String parameters) throws RemoteException {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Printing..................");
        System.out.println(retrunData);
        return retrunData.toString();

    }

    /**
     * @param xml
     * @return
     */
    public static List<Annotation> getParsedAnnotation(String xml) {
        return new XmlToObjectTransformer().getAnnotation(xml);
    }
    /**
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