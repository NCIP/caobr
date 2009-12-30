package edu.wustl.caobr.service;

import java.rmi.RemoteException;

import junit.framework.TestCase;

import org.cagrid.caobr.service.CaObrImpl;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Concept;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

public class TestCaObrImpl extends TestCase {

    public void testGetAllOntologies() {
        try {
            Ontology[] ontologies = new CaObrImpl().getAllOntologies();
            assertTrue("No ontologies found", (ontologies.length > 0));
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Exception while getting all ontologies");
        }
    }

    public void testGetAllResources() {
        try {
            Resource[] resources = new CaObrImpl().getAllResources();
            assertTrue("No resources found", (resources.length > 0));
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Exception while getting all resources");
        }
    }

    public void testGetAllConcepts() {
        try {
            Concept[] concepts = new CaObrImpl().getAllConcepts("melanoma");
            assertTrue("No concepts found for melanoma", (concepts.length > 0));
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Exception while getting all resources");
        }
    }

    public void testGetAllAnnotations() {
        Resource r = new Resource(null, null, null, null, null, null, "CANANO", null);
        Resource[] arr = new Resource[1];
        arr[0] = r;

        Ontology[] onto = new Ontology[1];
        onto[0] = new Ontology(null, null, null, null, "1083", null);

        try {
            Annotation[] res = new CaObrImpl().getAnnotations(onto, arr, "nanorod");
            assertTrue(res.length > 10); // As of 30 dec we get more than 10 annotations
            for (Annotation a : res) {
                Ontology o = a.getConcept().getConcept().getOntology().getOntology();
                assertNotNull(o);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception while getting all resources");
        }
    }

    public void testGetConcepts() {
        Ontology[] ontologies = new Ontology[1];
        ontologies[0] = new Ontology(null, null, null, null, "1083", null);
        Concept[] cons = null;
        try {
            cons = new CaObrImpl().getConcepts(ontologies, "nanorod");

        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in getConcepts()");
        }
        assertNotNull(cons);
        assertEquals(1, cons.length);
        assertNotNull(cons[0].getLocalConceptId());
        assertEquals("1083", cons[0].getOntology().getOntology().getOntologyId());
        assertNotNull(cons[0].getPreferredName());

    }

    public void testIsConceptTrue() {
        String concept = "nanorod";
        Ontology[] onto = new Ontology[1];
        onto[0] = new Ontology(null, null, null, null, "1083", null);
        try {
            assertTrue(new CaObrImpl().isConcept(onto, concept));
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConcept()");
        }
    }

    public void testIsConceptFalse() {
        Ontology[] onto = new Ontology[1];
        onto[0] = new Ontology(null, null, null, null, "1083", null);
        try {
            assertFalse(new CaObrImpl().isConcept(onto, "dog"));
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConcept()");
        }
    }

    public void testIsConceptInAnyOntologyTrue() {
        String concept = "nanorod";
        try {
            assertTrue(new CaObrImpl().isConceptInAnyOntology(concept));
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConceptInAnyOntology()");
        }
    }

    public void testIsConceptInAnyOntologyFalse() {
        try {
            assertFalse(new CaObrImpl().isConceptInAnyOntology("asasbhyuasvgja"));
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConceptInAnyOntology()");
        }
    }

    public void testIsConcepts() {
        String tokens[] = { "PET/MRI", "dy&*e  ", "melanoma" };

        Ontology[] onto = new Ontology[3];
        onto[0] = new Ontology(null, null, null, null, "1083", null);
        onto[1] = new Ontology(null, null, null, null, "1245", null);
        onto[2] = new Ontology(null, null, null, null, "1083", null);
        boolean[] arr = null;
        try {
            arr = new CaObrImpl().isConcepts(tokens, onto);

        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConcepts()");
        }
        assertEquals(3, arr.length);
        assertFalse(arr[0]);
        assertFalse(arr[1]);
        assertTrue(arr[2]);
        
    }

    public void testIsConceptsInAnyOntology() {
        String tokens[] = { "PET/MRI", "dye  ", "melanoma" };

        boolean[] arr = null;
        try {
            arr = new CaObrImpl().isConceptsInAnyOntology(tokens);
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception in isConceptsInAnyOntology()");
        }
        assertFalse(arr[0]);
        assertTrue(arr[1]);
        assertTrue(arr[2]);
    }
}

/*
  static List<DetailedOntologyBean> detailedOntologyBeans;

    static List<SmallOntologyBean> smallOntologyBeans;

    public static void main(String[] args) throws Exception {
        getDetailedOntologyBeans();
        getSmallOntologyBeans();
        for (SmallOntologyBean small : smallOntologyBeans) {
            boolean areDetailsPresent = false;
            for (DetailedOntologyBean detailed : detailedOntologyBeans) {
                if(small.localOntologyID.equals(detailed.id)) {
                    areDetailsPresent = true;
                }
            }
            if(!areDetailsPresent) {
                System.out.println(small.localOntologyID + "," + small.ontologyVersion + "," + small.virtualOntologyID + ","
                                   + small.ontologyName);
            }
        }
        
    }

    public static void getDetailedOntologyBeans() throws Exception {

        String ontologyDetailsUrl = "http://rest.bioontology.org/bioportal/ontologies";

        String xmlResult = RestApiInvoker.getResult(ontologyDetailsUrl);

        detailedOntologyBeans = new ArrayList<DetailedOntologyBean>();
        NodeList list = getElementByTagName(xmlResult, "ontologyBean");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList allOntologyNodes = node.getChildNodes();
            DetailedOntologyBean bean = new DetailedOntologyBean();
            for (int j = 0; j < allOntologyNodes.getLength(); j++) {
                Node child = allOntologyNodes.item(j);
                if ("id".equals(child.getNodeName())) {
                    bean.id = child.getTextContent();
                } else if ("abbreviation".equals(child.getNodeName())) {
                    bean.abbreviation = child.getTextContent();
                } else if ("description".equals(child.getNodeName())) {
                    bean.description = "\"" + child.getTextContent() + "\"";
                } else if ("displayLabel".equals(child.getNodeName())) {
                    bean.displayLabel = child.getTextContent();
                } else if ("format".equals(child.getNodeName())) {
                    bean.format = child.getTextContent();
                } else if ("ontologyId".equals(child.getNodeName())) {
                    bean.ontologyId = child.getTextContent();
                } else if ("versionNumber".equals(child.getNodeName())) {
                    bean.versionNumber = child.getTextContent();
                }

            }
            detailedOntologyBeans.add(bean);
        }
        System.out.println("id,abbreviation,displayLabel,format,ontologyId,versionNumber,description");
//        for (DetailedOntologyBean b : detailedOntologyBeans) {
//            System.out.println(b.id + "," + b.abbreviation + "," + b.displayLabel + "," + b.format + ","
//                    + b.ontologyId + "," + b.versionNumber + "," + b.description);
//        }

    }

    public static void getSmallOntologyBeans() throws Exception {
        String ontologyUrl = "http://rest.bioontology.org/resource_index/obs/ontologies/";
        String xmlResult = RestApiInvoker.getResult(ontologyUrl);

        smallOntologyBeans = new ArrayList<SmallOntologyBean>();
        NodeList list = getElementByTagName(xmlResult, "obs.common.beans.OntologyBean");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList allOntologyNodes = node.getChildNodes();
            SmallOntologyBean bean = new SmallOntologyBean();
            for (int j = 0; j < allOntologyNodes.getLength(); j++) {
                Node child = allOntologyNodes.item(j);
                if ("localOntologyID".equals(child.getNodeName())) {
                    bean.localOntologyID = child.getTextContent();
                } else if ("ontologyName".equals(child.getNodeName())) {
                    bean.ontologyName = "\"" + child.getTextContent() + "\"";
                } else if ("ontologyVersion".equals(child.getNodeName())) {
                    bean.ontologyVersion = child.getTextContent();
                } else if ("virtualOntologyID".equals(child.getNodeName())) {
                    bean.virtualOntologyID = child.getTextContent();
                }

            }
            smallOntologyBeans.add(bean);
        }
        System.out.println("localOntologyID,ontologyVersion,virtualOntologyID,ontologyName");
//        for (SmallOntologyBean b : smallOntologyBeans) {
//            System.out.println(b.localOntologyID + "," + b.ontologyVersion + "," + b.virtualOntologyID + ","
//                    + b.ontologyName);
//        }

    }

    private static NodeList getElementByTagName(String xmlString, String tag) throws ParserConfigurationException,
            SAXException, IOException {
        Document doc = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource src = new InputSource(new StringReader(xmlString));

        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        doc = documentBuilder.parse(src);

        return doc.getElementsByTagName(tag);
    }
}

class SmallOntologyBean {
    String localOntologyID;

    String ontologyName;

    String ontologyVersion;

    String virtualOntologyID;
}

class DetailedOntologyBean {
    String id;

    String ontologyId;

    String displayLabel;

    String abbreviation;

    String format;

    String versionNumber;

    String description;
} 
 */
