package edu.wustl.caobr.service.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.service.RestApiInvoker;
import edu.wustl.caobr.service.XmlToObjectTransformer;
import edu.wustl.caobr.service.util.RestApiInfo;

/**
 * Singleton implementation of the OntologyResourceCache class
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class OntologyCache {
    private static final Logger logger = Logger.getLogger(OntologyCache.class);

    private static OntologyCache cache = null;

    private Map<String, Ontology> ontologyMap = new HashMap<String, Ontology>();

    /**
     * This gives the singleton instance of the OntologyCache class. If not it creates it. 
     * @return Returns the singleton instance of the pathFinder class.
     */
    public static synchronized OntologyCache getInstance() {
        if (cache == null) {
            logger.info("OntologyResourceCache Called first Time. Loading cache...");
            cache = new OntologyCache();
        }
        return cache;
    }
    private OntologyCache() {
        refresh();
    }
    public void refresh() {
        Map<String, String> ontologiesUsedWhileAnnotating = getOntologiesUsedWhileAnnotating();
        String targetUrl = RestApiInfo.getOntologyDetailsURL();
        String result = RestApiInvoker.getResult(targetUrl);
        List<Ontology> obean = new XmlToObjectTransformer().toOntologies(result, ontologiesUsedWhileAnnotating);

        for (Ontology ontology : obean) {
            ontologyMap.put(ontology.getOntologyId(), ontology);
        }
    }

    private Map<String, String> getOntologiesUsedWhileAnnotating() {
        String targetUrl = RestApiInfo.getOntologyURL();
        String result = RestApiInvoker.getResult(targetUrl);
        Map<String, String> virtualOntlogyMap = new XmlToObjectTransformer().getOntologiesUsedWhileAnnotating(result);
        return virtualOntlogyMap;
    }

 
    /**
     * @param ontologyId
     * @return Ontology
     */
    public Ontology getOntology(String ontologyId) {
        return ontologyMap.get(ontologyId);

    }

    /**
     * @return ontologyMap
     */
    public Map<String, Ontology> getOntologyMap() {
        return ontologyMap;

    }

    /**
     * @return All ontologies which are used for generating annotation
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
}