package edu.wustl.caobr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.service.util.RestApiInfo;

/**
 * @author chandrakant_talele
 */
public class FetchAnnotationFromRestTask implements Callable<List<Annotation>> {
    private String urlParamForPostCall;

    /**
     * @param urlParamForPostCall
     */
    public FetchAnnotationFromRestTask(String urlParamForPostCall) {
        this.urlParamForPostCall = urlParamForPostCall;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    public List<Annotation> call() throws Exception {
        String targetUrl = RestApiInfo.getObrResultURL();
        try {
            String res = new RestApiInvoker().getResultFromPost(targetUrl, urlParamForPostCall);
            return RestApiInvoker.getParsedAnnotation(res);
        } catch (Exception e1) {
            //TODO Do exception handling
            e1.printStackTrace();
        }
        return new ArrayList<Annotation>(0);
    }
}
