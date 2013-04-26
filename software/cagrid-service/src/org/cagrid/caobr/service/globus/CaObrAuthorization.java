/*L
 * Copyright Washington University at St. Louis and Persistent Systems
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caobr/LICENSE.txt for details.
 */

package org.cagrid.caobr.service.globus;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import gov.nih.nci.cagrid.introduce.servicetools.security.AuthorizationExtension;
import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.globus.wsrf.config.ContainerConfig;
import org.w3c.dom.Node;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This is a PDP for use with the globus authorization callout.
 * This class will have a authorize method for each method on this grid service.
 * The method is responsibe for making any authorization callouts required to satisfy the 
 * authorization requirements placed on each method call.  Each method will either return
 * apon a successful authorization or will throw an exception apon a failed authorization.
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class CaObrAuthorization implements PDP {

	public static final String SERVICE_NAMESPACE = "http://caobr.cagrid.org/CaObr";
	
	Map authorizationClassMap = new HashMap();
	
	
	public CaObrAuthorization() {
	}
	
	protected String getServiceNamespace(){
		return SERVICE_NAMESPACE;
	}
	
	public static String getCallerIdentity() {
		String caller = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}
					
	public void authorizeGetMultipleResourceProperties(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetResourceProperty(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeQueryResourceProperties(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetServiceSecurityMetadata(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAllAnnotations(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAllConcepts(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAllOntologies(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAllResources(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAnnotations(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetConcepts(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeIsConcept(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeIsConceptInAnyOntology(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeIsConcepts(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeIsConceptsInAnyOntology(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   
	
	public boolean isPermitted(Subject peerSubject, MessageContext context, QName operation)
		throws AuthorizationException {
		
		if(!operation.getNamespaceURI().equals(getServiceNamespace())){
		  return false;
		}
		if(operation.getLocalPart().equals("getMultipleResourceProperties")){
			authorizeGetMultipleResourceProperties(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getResourceProperty")){
			authorizeGetResourceProperty(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("queryResourceProperties")){
			authorizeQueryResourceProperties(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getServiceSecurityMetadata")){
			authorizeGetServiceSecurityMetadata(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAllAnnotations")){
			authorizeGetAllAnnotations(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAllConcepts")){
			authorizeGetAllConcepts(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAllOntologies")){
			authorizeGetAllOntologies(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAllResources")){
			authorizeGetAllResources(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAnnotations")){
			authorizeGetAnnotations(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getConcepts")){
			authorizeGetConcepts(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("isConcept")){
			authorizeIsConcept(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("isConceptInAnyOntology")){
			authorizeIsConceptInAnyOntology(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("isConcepts")){
			authorizeIsConcepts(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("isConceptsInAnyOntology")){
			authorizeIsConceptsInAnyOntology(peerSubject, context, operation);
			return true;
		} 		
		return false;
	}
	

	public Node getPolicy(Node query) throws InvalidPolicyException {
		return null;
	}


	public String[] getPolicyNames() {
		return null;
	}


	public Node setPolicy(Node policy) throws InvalidPolicyException {
		return null;
	}


	public void close() throws CloseException {


	}


	public void initialize(PDPConfig config, String name, String id) throws InitializeException {
    	try{
    		String serviceName = (String)config.getProperty(name, "serviceName");
    	    String etcPath = ContainerConfig.getBaseDirectory() + File.separator + (String)config.getProperty(name, "etcDirectoryPath");

    	
    	} catch (Exception e){
        	throw new InitializeException(e.getMessage(),e);
		}
	}
	
	
}
