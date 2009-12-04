package edu.wustl.caobr.service.cache;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;
import edu.wustl.caobr.service.RestApiInvoker;
import edu.wustl.caobr.service.XmlToObjectTransformer;
import edu.wustl.caobr.service.util.RestApiInfo;

/**
 * Singleton implementation of the OntologyResourceCache class
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class OntologyResourceCache {
    private static final Logger logger = Logger.getLogger(OntologyResourceCache.class);

    private static OntologyResourceCache cache = null;

    private Map<String, Ontology> ontologyMap = new HashMap<String, Ontology>();

    private Map<String, Resource> resourceMap = new HashMap<String, Resource>();

    /**
     * This gives the singleton instance of the OntologyResourceCache class. If not it creates it. 
     * @return Returns the singleton instance of the pathFinder class.
     */
    public static synchronized OntologyResourceCache getInstance() {
        if (cache == null) {
            logger.info("OntologyResourceCache Called first Time. Loading cache...");
            cache = new OntologyResourceCache();
            cache.refreshCache();
        }
        return cache;
    }

    private void intializeOntologyMap() {
        Set<String> ontologiesUsedWhileAnnotating = getOntologiesUsedWhileAnnotating();
        String targetUrl = RestApiInfo.getOntologyDetailsURL();
        String result = RestApiInvoker.getResult(targetUrl);
        List<Ontology> obean = new XmlToObjectTransformer().toOntologies(result,ontologiesUsedWhileAnnotating);

        for (Ontology ontology : obean) {
            ontologyMap.put(ontology.getOntologyId(), ontology);
        }
    }
    
    private Set<String> getOntologiesUsedWhileAnnotating() {
        String targetUrl = RestApiInfo.getOntologyURL();
        String result = RestApiInvoker.getResult(targetUrl);
        Set<String> idSet = new XmlToObjectTransformer().getOntologiesUsedWhileAnnotating(result);
        return idSet;
    }

    /**
     * It initializes resource Map
     * @throws RemoteException
     */
    private void intializeResourceMap() {
        String targetUrl = RestApiInfo.getResourceURL();
        String result = RestApiInvoker.getResult(targetUrl);
        List<Resource> resource = new XmlToObjectTransformer().toResources(result);
        for (Resource r : resource) {
            resourceMap.put(r.getResourceId(), r);
        }
    }

    /**
     * @param ontologyId
     * @return
     */
    public Ontology getOntology(String ontologyId) {
        return ontologyMap.get(ontologyId);

    }

    /**
     * @return
     */
    public Map<String, Ontology> getOntologyMap() {
        return ontologyMap;

    }

    /**
     * @return
     */
    public Ontology[] getAllOntologies() {
        Map<String, Ontology> map = getOntologyMap();
        Ontology[] ontologies = new Ontology[map.keySet().size()];
        Set<String> keys = map.keySet();

        int i = 0;
        for (String key : keys) {
            ontologies[i] = map.get(key);
            i++;
        }
        return ontologies;
    }

    /**
     * @param resourceId
     * @return
     */
    public Resource getResource(String resourceId) {
        return resourceMap.get(resourceId);

    }

    /**
     * @return
     */
    public Map<String, Resource> getResourceMap() {
        return resourceMap;

    }

    /**
     * @return
     */
    public Resource[] getAllResources() {
        Map<String, Resource> map = getResourceMap();
        Resource[] resources = new Resource[map.keySet().size()];
        Set<String> keys = map.keySet();

        int i = 0;
        for (String key : keys) {
            resources[i] = map.get(key);
            i++;
        }
        return resources;
    }

    /**
     * 
     */
    public void refreshCache() {
        logger.info("Refreshing  otologies and Resources..");
        intializeOntologyMap();
        intializeResourceMap();

    }
}