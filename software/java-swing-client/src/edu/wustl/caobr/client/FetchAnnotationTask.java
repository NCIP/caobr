package edu.wustl.caobr.client;

import java.util.concurrent.Callable;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

/**
 * @author chandrakant_talele
 */
class FetchAnnotationTask implements Callable<Annotation[]> {
    Ontology[] ontologies;

    Resource[] resources;

    String token;

    ServiceInvoker client;

    /**
     * @param ontologies
     * @param resources
     * @param term
     * @param client
     */
    public FetchAnnotationTask(Ontology[] ontologies, Resource[] resources, String term, ServiceInvoker client) {
        this.ontologies = ontologies;
        this.resources = resources;
        this.token = term;
        this.client = client;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    public Annotation[] call() throws Exception {
        return client.getAnnotations(ontologies, resources, token);
    }
}