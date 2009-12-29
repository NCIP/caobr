package org.cagrid.caobr.service.globus;

import org.cagrid.caobr.service.CaObrImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the CaObrImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class CaObrProviderImpl{
	
	CaObrImpl impl;
	
	public CaObrProviderImpl() throws RemoteException {
		impl = new CaObrImpl();
	}
	

    public org.cagrid.caobr.stubs.GetAllAnnotationsResponse getAllAnnotations(org.cagrid.caobr.stubs.GetAllAnnotationsRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetAllAnnotationsResponse boxedResult = new org.cagrid.caobr.stubs.GetAllAnnotationsResponse();
    boxedResult.setAnnotation(impl.getAllAnnotations(params.getToken()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.GetAllConceptsResponse getAllConcepts(org.cagrid.caobr.stubs.GetAllConceptsRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetAllConceptsResponse boxedResult = new org.cagrid.caobr.stubs.GetAllConceptsResponse();
    boxedResult.setConcept(impl.getAllConcepts(params.getConceptName()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.GetAllOntologiesResponse getAllOntologies(org.cagrid.caobr.stubs.GetAllOntologiesRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetAllOntologiesResponse boxedResult = new org.cagrid.caobr.stubs.GetAllOntologiesResponse();
    boxedResult.setOntology(impl.getAllOntologies());
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.GetAllResourcesResponse getAllResources(org.cagrid.caobr.stubs.GetAllResourcesRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetAllResourcesResponse boxedResult = new org.cagrid.caobr.stubs.GetAllResourcesResponse();
    boxedResult.setResource(impl.getAllResources());
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.GetAnnotationsResponse getAnnotations(org.cagrid.caobr.stubs.GetAnnotationsRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetAnnotationsResponse boxedResult = new org.cagrid.caobr.stubs.GetAnnotationsResponse();
    boxedResult.setAnnotation(impl.getAnnotations(params.getFromOntologies().getOntology(),params.getFromResources().getResource(),params.getToken()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.GetConceptsResponse getConcepts(org.cagrid.caobr.stubs.GetConceptsRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.GetConceptsResponse boxedResult = new org.cagrid.caobr.stubs.GetConceptsResponse();
    boxedResult.setConcept(impl.getConcepts(params.getFromOntologies().getOntology(),params.getConceptName()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.IsConceptResponse isConcept(org.cagrid.caobr.stubs.IsConceptRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.IsConceptResponse boxedResult = new org.cagrid.caobr.stubs.IsConceptResponse();
    boxedResult.setResponse(impl.isConcept(params.getFromOntologies().getOntology(),params.getSearchTerm()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.IsConceptInAnyOntologyResponse isConceptInAnyOntology(org.cagrid.caobr.stubs.IsConceptInAnyOntologyRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.IsConceptInAnyOntologyResponse boxedResult = new org.cagrid.caobr.stubs.IsConceptInAnyOntologyResponse();
    boxedResult.setResponse(impl.isConceptInAnyOntology(params.getSearchTerm()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.IsConceptsResponse isConcepts(org.cagrid.caobr.stubs.IsConceptsRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.IsConceptsResponse boxedResult = new org.cagrid.caobr.stubs.IsConceptsResponse();
    boxedResult.setResponse(impl.isConcepts(params.getTokens(),params.getOntologies().getOntology()));
    return boxedResult;
  }

    public org.cagrid.caobr.stubs.IsConceptsInAnyOntologyResponse isConceptsInAnyOntology(org.cagrid.caobr.stubs.IsConceptsInAnyOntologyRequest params) throws RemoteException {
    org.cagrid.caobr.stubs.IsConceptsInAnyOntologyResponse boxedResult = new org.cagrid.caobr.stubs.IsConceptsInAnyOntologyResponse();
    boxedResult.setResponse(impl.isConceptsInAnyOntology(params.getTokens()));
    return boxedResult;
  }

}