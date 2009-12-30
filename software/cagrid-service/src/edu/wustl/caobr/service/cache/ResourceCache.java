package edu.wustl.caobr.service.cache;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.wustl.caobr.Resource;
import edu.wustl.caobr.service.RestApiInvoker;
import edu.wustl.caobr.service.XmlToObjectTransformer;
import edu.wustl.caobr.service.util.RestApiInfo;

/**
 * Singleton implementation of the OntologyResourceCache class
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class ResourceCache {
    private static final Logger logger = Logger.getLogger(ResourceCache.class);

    private static ResourceCache cache = null;

    private Map<String, Resource> resourceMap = new HashMap<String, Resource>();

    /**
     * This gives the singleton instance of the OntologyResourceCache class. If not it creates it. 
     * @return Returns the singleton instance of the pathFinder class.
     */
    public static synchronized ResourceCache getInstance() {
        if (cache == null) {
            logger.info("OntologyResourceCache Called first Time. Loading cache...");
            cache = new ResourceCache();
        }
        return cache;
    }

    private ResourceCache() {
        refresh();
    }

    /**
     * It initializes resource Map
     * @throws RemoteException
     */
    public void refresh() {
        String targetUrl = RestApiInfo.getResourceURL();
        String result = RestApiInvoker.getResult(targetUrl);
        List<Resource> resource = new XmlToObjectTransformer().toResources(result);
        for (Resource r : resource) {
            resourceMap.put(r.getResourceId(), r);
        }
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
}