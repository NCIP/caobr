package edu.wustl.caobr.client;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;

import org.cagrid.caobr.service.CaObrImpl;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

/**
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class ServiceInvoker {
    
//    private static final String SERVICE_URL = "http://caobr.wustl.edu:8080/wsrf/services/cagrid/CaObr";
//    //private static final String SERVICE_URL = "http://ps4902:8080/wsrf/services/cagrid/CaObr";
//
//    private CaObrClient client = null;
//
//    public ServiceInvoker() {
//
//        try {
//            client = new CaObrClient(SERVICE_URL);
//        } catch (MalformedURIException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    public Ontology[] getOntologies() {
//
//        Ontology[] ontologies = null;
//        try {
//            ontologies = client.getAllOntologies();
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Arrays.sort(ontologies, new Comparator<Ontology>() {
//            public int compare(Ontology o1, Ontology o2) {
//                return o1.getDisplayLable().compareToIgnoreCase(o2.getDisplayLable());
//            }
//        });
//        System.out.println("------------------------\n\tOntologies\n------------------------");
//        for (Ontology o : ontologies) {
//            System.out.println(o.getDisplayLable());
//        }
//        return ontologies;
//    }
//
//    public Resource[] getResources() {
//
//        Resource[] resources = null;
//        try {
//            resources = client.getAllResources();
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Arrays.sort(resources, new Comparator<Resource>() {
//            public int compare(Resource r1, Resource r2) {
//                return r1.getName().compareToIgnoreCase(r2.getName());
//            }
//        });
//        System.out.println("------------------------\n\tResources\n------------------------");
//        for (Resource r : resources) {
//            System.out.println(r.getName());
//        }
//        return resources;
//    }
//
//    public Annotation[] getAnnotations(Ontology[] ontologies, Resource[] resources, String token) {
//        Annotation[] annotations = new Annotation[0];
//        try {
//            annotations = client.getAnnotations(ontologies, resources, token);
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return annotations;
//    }
    
///////////////////////////////////////////////////////////////////////////
    
    static {
        System.setProperty("http.proxyHost","ptproxy.persistent.co.in");
        System.setProperty("http.proxyPort","8080");
                           
    }
    public Ontology[] getOntologies() {

        Ontology[] ontologies = null;
        try {
            ontologies = new CaObrImpl().getAllOntologies();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Arrays.sort(ontologies, new Comparator<Ontology>() {
            public int compare(Ontology o1, Ontology o2) {
                return o1.getDisplayLable().compareToIgnoreCase(o2.getDisplayLable());
            }
        });
        System.out.println("------------------------\n\tOntologies\n------------------------");
        for (Ontology o : ontologies) {
            System.out.println(o.getDisplayLable());
        }
        return ontologies;
    }

    public Resource[] getResources() {

        Resource[] resources = null;
        try {
            resources = new CaObrImpl().getAllResources();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Arrays.sort(resources, new Comparator<Resource>() {
            public int compare(Resource r1, Resource r2) {
                return r1.getName().compareToIgnoreCase(r2.getName());
            }
        });
        System.out.println("------------------------\n\tResources\n------------------------");
        for (Resource r : resources) {
            System.out.println(r.getName());
        }
        return resources;
    }

    public Annotation[] getAnnotations(Ontology[] ontologies, Resource[] resources, String token) {
        Annotation[] annotations = new Annotation[0];
        try {
            annotations = new CaObrImpl().getAnnotations(ontologies, resources, token);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return annotations;
    }
    
    
}
