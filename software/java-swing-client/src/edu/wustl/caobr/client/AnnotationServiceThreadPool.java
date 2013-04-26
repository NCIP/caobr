/*L
 * Copyright Washington University at St. Louis and Persistent Systems
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caobr/LICENSE.txt for details.
 */

package edu.wustl.caobr.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

/**
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class AnnotationServiceThreadPool {

    private final int poolSize = 10;

    private List<Future<Annotation[]>> futures;

    /**
     * @param fromOntologies
     * @param fromResources
     * @param term
     */
    public Annotation[] getAnnotations(Ontology[] fromOntologies, Resource[] fromResources, String term) {
        ServiceInvoker client = new ServiceInvoker();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        futures = new ArrayList<Future<Annotation[]>>(fromResources.length);
        for (Resource resource : fromResources) {

            Resource[] arr = new Resource[1];
            arr[0] = resource;
            FetchAnnotationTask task = new FetchAnnotationTask(fromOntologies, arr, term, client);
            Future<Annotation[]> future = executor.submit(task);
            futures.add(future);
        }
        executor.shutdown();
        return getAnnotations();
    }

    /**
     * Method to get annotations. It waits for all threads to finish before returning 
     * @return Array of annotations
     */
    private Annotation[] getAnnotations() {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Future<Annotation[]> future : futures) {
            try {
                Annotation[] result = future.get();
                if (result != null) {
                    for (int i = 0; i < result.length; i++) {
                        annotations.add(result[i]);
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return annotations.toArray(new Annotation[annotations.size()]);
    }
}