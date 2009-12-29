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
import edu.wustl.caobr.service.cache.OntologyResourceCache;
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
                    OntologyResourceCache.getInstance();
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
    public edu.wustl.caobr.Annotation[] getAllAnnotations(java.lang.String token) throws RemoteException {
        return getAnnotations(null, null, token);
    }

    /**
     * @param conceptName
     * @return Concepts
     * @throws RemoteException
     */
    public edu.wustl.caobr.Concept[] getAllConcepts(java.lang.String conceptName) throws RemoteException {
        return getConcepts(null, conceptName);
    }

    /**
     * @return
     * @throws RemoteException
     */
    public edu.wustl.caobr.Ontology[] getAllOntologies() throws RemoteException {
        return OntologyResourceCache.getInstance().getAllOntologies();
    }

    /**
     * @return
     * @throws RemoteException
     */
    public edu.wustl.caobr.Resource[] getAllResources() throws RemoteException {
        return OntologyResourceCache.getInstance().getAllResources();
    }

    /**
     * @param fromOntologies
     * @param fromResources
     * @param token
     * @return Annotations
     * @throws RemoteException
     */
    public edu.wustl.caobr.Annotation[] getAnnotations(edu.wustl.caobr.Ontology[] fromOntologies,
                                                       edu.wustl.caobr.Resource[] fromResources,
                                                       java.lang.String token) throws RemoteException {
        System.out.println("Fetching Annotation:");
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
     * @return
     * @throws RemoteException
     */
    public edu.wustl.caobr.Concept[] getConcepts(edu.wustl.caobr.Ontology[] fromOntologies,
                                                 java.lang.String conceptName) throws RemoteException {
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
     * @return
     * @throws RemoteException
     */
    public boolean isConcept(edu.wustl.caobr.Ontology[] fromOntologies, java.lang.String searchTerm)
            throws RemoteException {
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("")) {
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
     * @return
     * @throws RemoteException
     */
    public boolean isConceptInAnyOntology(java.lang.String searchTerm) throws RemoteException {
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("")) {
            return false;
        }
        if (checkSpecialCharacter(trimmedTerm)) {
            return false;
        }
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
     * @return
     * @throws RemoteException
     */
    public boolean[] isConcepts(java.lang.String[] tokens, edu.wustl.caobr.Ontology[] ontologies)
            throws RemoteException {
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
     * @return
     * @throws RemoteException
     */
    public boolean[] isConceptsInAnyOntology(java.lang.String[] tokens) throws RemoteException {
        boolean[] flags = new boolean[tokens.length];
        int i = 0;
        for (String token : tokens) {
            System.out.println("Searching for " + token);
            flags[i] = isConceptInAnyOntology(token);
            i++;
        }
        return flags;
    }

    /**
     * This method returns the target URL for searching concept
     * 
     * @param ontologies
     * @param conceptName
     * @return
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
        System.out.println("Target :" + targetUrl);
        return targetUrl;

    }

    /**
     * @param term
     * @return
     */
    private String getTargetUrlWithOutAnyOntology(String term) {
        return RestApiInfo.getConceptIdURL() + encode(term) + "/" + "&isexactmatch=1";
    }
}
