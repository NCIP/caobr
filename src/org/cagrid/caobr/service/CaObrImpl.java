package org.cagrid.caobr.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import edu.wustl.caobr.service.cache.OntologyResourceCache;
import edu.wustl.caobr.service.util.RestApiInfo;
import edu.wustl.caobr.service.util.SearchBean;

/** 
 * This class is the entry point to service. 
 * All methods exposed by service are implemented here.
 * 
 * @created by Introduce Toolkit version 1.3
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class CaObrImpl extends CaObrImplBase {

    private static final Logger logger = Logger.getLogger(CaObrImpl.class);

    private static final long delay = 0L;

    private static final long interval = 3600000 * 12;

    // Is this really needed ? Below thread will execute it at least once, isn't it?
    static {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    //Any exception kills the thread. So adding this try catch to keep thread alive
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
     * @return
     * @throws RemoteException
     */
    public Annotation[] getAllAnnoations(String token) throws RemoteException {
        return getAnnotations(null, null, token);
    }

    /**
     * @param conceptName
     * @return
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
        return OntologyResourceCache.getInstance().getAllOntologies();
    }

    /**
     * @return
     * @throws RemoteException
     */
    public Resource[] getAllResources() throws RemoteException {
        return OntologyResourceCache.getInstance().getAllResources();
    }

    /**
     * @param fromOntologies
     * @param fromResources
     * @param token
     * @return
     * @throws RemoteException
     */
    public Annotation[] getAnnotations(Ontology[] fromOntologies, Resource[] fromResources, String token)
            throws RemoteException {
        System.out.println("Fetching Annotation:");
        List<SearchBean> seachBeans;
        if (fromOntologies == null) {
            seachBeans = RestApiInvoker.getSearchBeans(getTargetUrlWithOutAnyOntology(token));
        } else {
            seachBeans = RestApiInvoker.getSearchBeans(getTargetUrl(fromOntologies, token));
        }
        Set<Annotation> annotation = new HashSet<Annotation>();
        if (fromResources == null) {
            fromResources = getAllResources();
        }

        for (Resource r : fromResources) {
            List<String> urlParamForPostCall = new ArrayList<String>(seachBeans.size());
            for (SearchBean searchBean : seachBeans) {
                urlParamForPostCall.add(getParamStr(searchBean, r));
            }
            ObrThreadPoolExecutor executor = new ObrThreadPoolExecutor();
            annotation.addAll(executor.getAnnotations(urlParamForPostCall));
        }
        Annotation[] returnedAnnotation = new Annotation[annotation.size()];

        int i = 0;
        for (Annotation ann : annotation) {
            returnedAnnotation[i] = ann;
            i++;
        }
        return returnedAnnotation;
    }

    /**
     * @param searchBean
     * @param r
     * @return
     */
    private String getParamStr(SearchBean searchBean, Resource r) {
        //TODO need to check this with lalit. Can it be reduced to List of attribute-value pairs ?
            
        String urlParameters = "localConceptIDs=" + encode(searchBean.getOntologyId() + "/" + searchBean.getConceptIdShort())
        + "&resourceID="      + encode(r.getResourceId()) 
        + "&elementDetails="  + encode("true") 
        + "&virtual="         + encode("true") 
        + "&withContext="     + encode("true");
        
//        String urlParameters =
//                    "localOntologyIDs=" + encode(searchBean.getOntologyVersionId().toString())
//                    + "&localConceptIDs=" + encode(searchBean.getOntologyVersionId() + "/" + searchBean.getConceptIdShort())
//                  + "&resourceID="      + encode(r.getResourceId()) 
//                  + "&elementDetails="  + encode("true") 
//                  + "&withContext="     + encode("true");
            return urlParameters;
    }

    /**
     * @param str
     * @return
     */
    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /**
     * @param fromOntologies
     * @param conceptName
     * @return
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

        List<Concept> concepts = RestApiInvoker.getConcepts(url);
        Concept[] returnConcepts = new Concept[concepts.size()];
        int i = 0;
        for (Concept concept : returnConcepts) {
            returnConcepts[i] = concept;
            i++;
        }
        return returnConcepts;
    }

    /**
     * @param fromOntologies
     * @param searchTerm
     * @return
     * @throws RemoteException
     */
    public boolean isConcept(Ontology[] fromOntologies, String searchTerm) throws RemoteException {
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("")) {
            return false;
        }
        String targetUrl = getTargetUrl(fromOntologies, trimmedTerm);

        if (RestApiInvoker.getSearchBeans(targetUrl).isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @param searchTerm
     * @return
     * @throws RemoteException
     */
    public boolean isConceptInAnyOntology(String searchTerm) throws RemoteException {
        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.equals("")) {
            return false;
        }
        String targetUrl = getTargetUrlWithOutAnyOntology(searchTerm);

        if (RestApiInvoker.getSearchBeans(targetUrl).isEmpty()) {
            return false;
        }
        System.out.println(targetUrl);
        return true;

    }

    /**
     * @param tokens
     * @param ontologies
     * @return
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
     * @return
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
     * This method returns the target URL for searching concept 
     * @param ontologies
     * @param conceptName
     * @return
     */
    private String getTargetUrl(Ontology[] ontologies, String conceptName) {

        String ontologyIds = "?ontologyids="; // TODO from  1 dec 09 looks like it should be small case
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
            targetUrl =
                    RestApiInfo.getConceptIdURL() + URLEncoder.encode(conceptName, "UTF-8") + "/" + ontologyIds
                            + "&isexactmatch=1";
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