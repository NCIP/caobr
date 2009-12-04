package edu.wustl.caobr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.wustl.caobr.Annotation;

/**
 * @author chandrakant_talele
 */
public class ObrThreadPoolExecutor {
    private final int poolSize = 10;

    private List<Future<List<Annotation>>> futures;

    /**
     * @param urlParamForPostCall
     * @return
     */
    public List<Annotation> getAnnotations(List<String> urlParamForPostCall) {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        futures = new ArrayList<Future<List<Annotation>>>(100);
        for (String urlParam : urlParamForPostCall) {
            FetchAnnotationFromRestTask task = new FetchAnnotationFromRestTask(urlParam);
            Future<List<Annotation>> future = executor.submit(task);
            futures.add(future);
        }
        executor.shutdown();
        return getAnnotations();
    }

    /**
     * @return
     */
    private List<Annotation> getAnnotations() {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Future<List<Annotation>> future : futures) {
            try {
                List<Annotation> result = future.get();
                annotations.addAll(result);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return annotations;
    }
}
