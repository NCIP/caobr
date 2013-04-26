/*L
 * Copyright Washington University at St. Louis and Persistent Systems
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caobr/LICENSE.txt for details.
 */

package edu.wustl.caobr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.service.util.RestApiInfo;

/**
 * Callable task to get annotations for given URL parameters using a POST call.
 * @author chandrakant_talele
 */
public class FetchAnnotationFromRestTask implements Callable<List<Annotation>> {
    private static final Logger logger = Logger.getLogger(ObrThreadPoolExecutor.class);

    private Map<String, String> urlParamForPostCall;

    /**
     * Constructor
     * @param urlParamForPostCall
     */
    public FetchAnnotationFromRestTask(Map<String, String> urlParamForPostCall) {
        this.urlParamForPostCall = urlParamForPostCall;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    public List<Annotation> call() throws Exception {
        String targetUrl = RestApiInfo.getObrResultURL();
        try {        	
            String res = new RestApiInvoker().getResultFromPost(targetUrl, urlParamForPostCall);            
            return new XmlToObjectTransformer().getAnnotation(res);
        } catch (Exception e) {
            logger.error("Exception Thrown ", e);
        }
        return new ArrayList<Annotation>(0);
    }
}