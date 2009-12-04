package edu.wustl.caobr.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestOntology {
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