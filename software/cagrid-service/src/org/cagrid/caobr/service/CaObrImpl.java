/*L
 * Copyright Washington University at St. Louis and Persistent Systems
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caobr/LICENSE.txt for details.
 */

package org.cagrid.caobr.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Concept;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;
import edu.wustl.caobr.service.ObrThreadPoolExecutor;
import edu.wustl.caobr.service.RestApiInvoker;
import edu.wustl.caobr.service.XmlToObjectTransformer;
import edu.wustl.caobr.service.cache.OntologyCache;
import edu.wustl.caobr.service.cache.ResourceCache;
import edu.wustl.caobr.service.util.RestApiInfo;
import edu.wustl.caobr.service.util.SearchBean;

/**
 * This class is the entry point to service. All methods exposed by service are
 * implemented here.
 * 
 * @created by Introduce Toolkit version 1.3
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class CaObrImpl extends CaObrImplBase {

    private static final Logger logger = Logger.getLogger(CaObrImpl.class);

    private static final long delay = 0L;

    private static final long interval = 3600000 * 12;

    static {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    // Any exception kills the thread. So adding this try catch
                    // to keep thread alive
                    OntologyCache.getInstance();
                    ResourceCache.getInstance();
                } catch (Throwable t) {
                    logger.error(t);
                }
            }
        }, delay, interval);               
    }

    /**
     * Constructor
     * @throws RemoteException
     */
    public CaObrImpl() throws RemoteException {
        super();
    }

    /**
     * @param token
     * @return Annotations
     * @throws RemoteException
     */
    public Annotation[] getAllAnnotations(String token) throws RemoteException {
        return getAnnotations(null, null, token);
    }

    /**
     * @param conceptName
     * @return Concepts
     * @throws RemoteException
     */
    public Concept[] getAllConcepts(String conceptName) throws RemoteException {
        return getConcepts(null, conceptName);
    }

    /**
     * @return
     * @throws RemoteException
     */
    public Ontology[] getAllOntologies() throws RemoteException {
        return OntologyCache.getInstance().getAllOntologies();
    }

    /**
     * @return Resources from OBR 
     * @throws RemoteException
     */
    public Resource[] getAllResources() throws RemoteException {
        return ResourceCache.getInstance().getAllResources();
    }

    /**
     * @param fromOntologies
     * @param fromResources
     * @param token
     * @return Annotations  having conceptIds present in fromOntologies from resources.
     * @throws RemoteException
     */
    public Annotation[] getAnnotations(Ontology[] fromOntologies, Resource[] fromResources, String token)
            throws RemoteException {

    	logger.debug(" getAnnotations called with ::"+token);    	
    	
        String url;
        if (fromOntologies == null || fromOntologies.length == 0) {
            url = getTargetUrlWithOutAnyOntology(token);
        } else {
            url = getTargetUrl(fromOntologies, token);
        }
        String result = RestApiInvoker.getResult(url);
        List<SearchBean> seachBeans = new XmlToObjectTransformer().toSearchBean(result);

        Set<Annotation> annotation = new HashSet<Annotation>();
        if (fromResources == null || fromResources.length == 0) {
            fromResources = getAllResources();
        }

        for (Resource r : fromResources) {
            List<Map<String, String>> urlParamForPostCall = new ArrayList<Map<String, String>>(seachBeans.size());
            for (SearchBean searchBean : seachBeans) {
                urlParamForPostCall.add(getParamStr(searchBean, r));
            }
            ObrThreadPoolExecutor executor = new ObrThreadPoolExecutor();
            annotation.addAll(executor.getAnnotations(urlParamForPostCall));
        }
        return annotation.toArray(new Annotation[0]);
    }

    /**
     * @param searchBean
     * @param r
     * @return
     */
    private Map<String, String> getParamStr(SearchBean searchBean, Resource r) {

        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("localConceptIDs", encode(searchBean.getOntologyId() + "/"
                + searchBean.getConceptIdShort()));
        parametersMap.put("resourceID", encode(r.getResourceId()));
        parametersMap.put("elementDetails", encode("true"));
        parametersMap.put("virtual", encode("true"));
        parametersMap.put("withContext", encode("true"));
        parametersMap.put("from", encode("1"));
        parametersMap.put("number", encode(Integer.toString(Integer.MAX_VALUE)));

        return parametersMap;
    }

    /**
     * @param str
     * @return
     */
    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("UnSupported Encoding Exception", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fromOntologies
     * @param conceptName
     * @return Concepts present in fromOntologies for a conceptName
     * @throws RemoteException
     */
    public Concept[] getConcepts(Ontology[] fromOntologies, String conceptName) throws RemoteException {
        String trimmedTerm = conceptName.trim();
        if (trimmedTerm.equals("")) {
            return new Concept[0];
        }

        String url = null;
        if (fromOntologies == null) {
            url = getTargetUrlWithOutAnyOntology(trimmedTerm);
        } else {
            url = getTargetUrl(fromOntologies, trimmedTerm);
        }

        String result = RestApiInvoker.getResult(url);
        List<Concept> concepts = new XmlToObjectTransformer().toConcepts(result);
        return concepts.toArray(new Concept[0]);
    }

    /**
     * @param fromOntologies
     * @param searchTerm
     * @return true  if searchTerm is a concept in fromOntologies
     * @throws RemoteException
     */
    public boolean isConcept(Ontology[] fromOntologies, String searchTerm) throws RemoteException {
    	
    	logger.debug(" isConcept called with ::"+searchTerm+ " fromOntologies ::"+fromOntologies);    	
    	
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("") || checkSpecialCharacter(trimmedTerm)) {
            return false;
        }
        String targetUrl = getTargetUrl(fromOntologies, trimmedTerm);
        String result = RestApiInvoker.getResult(targetUrl);
        List<SearchBean> beans = new XmlToObjectTransformer().toSearchBean(result);
        if (beans.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @param searchTerm
     * @return true  if searchTerm is a concept  
     * @throws RemoteException
     */
    public boolean isConceptInAnyOntology(String searchTerm) throws RemoteException {
    	
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("") || checkSpecialCharacter(trimmedTerm)) {
            return false;
        }
        try{
        	 String targetUrl = getTargetUrlWithOutAnyOntology(trimmedTerm);
             String result = RestApiInvoker.getResult(targetUrl);
             if (result.equals("")) {
                 logger.debug("Could not find concept for +" + trimmedTerm);
                 return false;
             }
             List<SearchBean> beans = new XmlToObjectTransformer().toSearchBean(result);
             if (beans.isEmpty()) {
                 return false;
             }
        }catch (RuntimeException re) {
        	logger.error(" RuntimeException Occured :: "+re,re);
        	return false;
		}       
        return true;
    }

    private boolean checkSpecialCharacter(String token) {
        String specialCharacters = "!@#$'/\"'^&*(),.{}+=~*[];:|\\<>?";

        for (int i = 0; i < specialCharacters.length(); i++) {
            for (int j = 0; j < token.length(); j++) {
                if (token.charAt(j) == specialCharacters.charAt(i))
                    return true;
            }
        }
        return false;
    }

    /**
     * @param tokens
     * @param ontologies
     * @return true  if each token in tokens is a concept in ontologies , otherwise returns false
     * @throws RemoteException
     */
    public boolean[] isConcepts(String[] tokens, Ontology[] ontologies) throws RemoteException {
        boolean[] flags = new boolean[tokens.length];
        int i = 0;
        for (String token : tokens) {
            flags[i] = isConcept(ontologies, token);
            i++;
        }
        return flags;
    }

    /**
     * @param tokens
     * @return true  if each token in tokens is a concept ,otherwise returns false
     * @throws RemoteException
     */
    public boolean[] isConceptsInAnyOntology(String[] tokens) throws RemoteException {
        boolean[] flags = new boolean[tokens.length];
        int i = 0;
        for (String token : tokens) {
            flags[i] = isConceptInAnyOntology(token);
            i++;
        }
        return flags;
    }

    /**
     * @param ontologies
     * @param conceptName
     * @return target URL for searching concept 
     */
    private String getTargetUrl(Ontology[] ontologies, String conceptName) {

        String ontologyIds = "?ontologyids=";
        for (int i = 0; i < ontologies.length; i++) {
            Ontology ontology = ontologies[i];
            if (i != ontologies.length - 1) {
                ontologyIds = ontologyIds + ontology.getOntologyId() + ",";
            } else {
                ontologyIds = ontologyIds + ontology.getOntologyId();
            }
        }
        String targetUrl = null;
        try {
            targetUrl = RestApiInfo.getConceptIdURL() + URLEncoder.encode(conceptName, "UTF-8") + "/"
                    + ontologyIds + "&isexactmatch=1";
        } catch (UnsupportedEncodingException e) {
            logger.info("Unsupported Encoding ");
            e.printStackTrace();
        }
        return targetUrl;

    }

    /**
     * @param term
     * @return target URL for searching concept
     */
    private String getTargetUrlWithOutAnyOntology(String term) {
        return RestApiInfo.getConceptIdURL() + encode(term) + "/" + "?isexactmatch=1";
    }

}
