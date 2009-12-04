package edu.wustl.caobr.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.AnnotationAnnotationContext;
import edu.wustl.caobr.AnnotationConcept;
import edu.wustl.caobr.AnnotationContext;
import edu.wustl.caobr.AnnotationContextContext;
import edu.wustl.caobr.AnnotationResource;
import edu.wustl.caobr.Concept;
import edu.wustl.caobr.ConceptOntology;
import edu.wustl.caobr.Context;
import edu.wustl.caobr.ContextOntology;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;
import edu.wustl.caobr.ResourceContext;
import edu.wustl.caobr.ResourceMainContext;
import edu.wustl.caobr.service.cache.OntologyResourceCache;
import edu.wustl.caobr.service.util.RestApiInfo;
import edu.wustl.caobr.service.util.SearchBean;

/**
 * @author chandrakant_talele
 */
public class XmlToObjectTransformer {

    static Map<String, String> idvsOntologyIdMap = new HashMap<String, String>();

    Set<String> resourceIds = new HashSet<String>();

    Set<String> contextNames = new HashSet<String>();

    Map<String, Double> weightValuesMap = new HashMap<String, Double>();

    Map<String, String> ontologyValuesMap = new HashMap<String, String>();

    /**
     * @param xmlResult
     * @return
     */
    public List<Concept> toConcepts(String xmlResult) {
        NodeList list = getElementsFromXML(xmlResult, "searchBean", false);
        List<Concept> concepts = new ArrayList<Concept>();
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            concepts.add(toConceptObject(node));
        }
        return concepts;
    }

    /**
     * @param node
     * @return
     */
    private static Concept toConceptObject(Node node) {
        NodeList list = node.getChildNodes();
        Concept concept = new Concept();

        for (int j = 0; j < list.getLength(); j++) {
            Node child = list.item(j);
            if ("localOntologyID".equals(child.getNodeName())) {
                String id = child.getNodeValue();
                String ontologyId = idvsOntologyIdMap.get(id);
                ConceptOntology conceptOntology =
                        new ConceptOntology(OntologyResourceCache.getInstance().getOntology(ontologyId));
                concept.setOntology(conceptOntology);
            } else if ("preferredName".equals(child.getNodeName())) {
                concept.setPreferredName(child.getNodeValue());
            } else if ("isRoot".equals(child.getNodeName())) {
                concept.setIsRoot(Boolean.parseBoolean(child.getNodeValue()));
            } else if ("localConceptID".equals(child.getNodeName())) {
                concept.setLocalId(child.getNodeValue());
            }
        }
        return concept;
    }

    /**
     * @param xmlResult
     * @return
     */
    public List<SearchBean> toSearchBean(String xmlResult) {
        NodeList list = getElementsFromXML(xmlResult, "searchBean", false);

        List<SearchBean> searchBeans = new ArrayList<SearchBean>();
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            searchBeans.add(toSearchtObject(node));
        }
        return searchBeans;
    }

    /**
     * @param node
     * @return
     */
    private static SearchBean toSearchtObject(Node node) {
        NodeList list = node.getChildNodes();
        SearchBean searchBean = new SearchBean();

        for (int j = 0; j < list.getLength(); j++) {
            Node child = list.item(j);
            if ("ontologyId".equals(child.getNodeName())) {
                searchBean.setOntologyId(child.getTextContent());
            } else if ("ontologyVersionId".equals(child.getNodeName())) {
                searchBean.setOntologyVersionId(child.getTextContent());
            } else if ("conceptId".equals(child.getNodeName())) {
                searchBean.setConceptId(child.getTextContent());
            } else if ("conceptIdShort".equals(child.getNodeName())) {
                searchBean.setConceptIdShort(child.getTextContent());
            }
        }
        return searchBean;
    }

    /**
     * @param xmlResult
     * @param ontologyIdsToLookFor 
     * @return
     */
    public List<Ontology> toOntologies(String xmlResult, Set<String> ontologiesUsedWhileAnnotating) {
        NodeList list = getElementsFromXML(xmlResult, "ontologyBean", false);

        List<Ontology> ontologies = new ArrayList<Ontology>();
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            if (isUsedForAnnotation(node, ontologiesUsedWhileAnnotating)) {
                Ontology o = toOntologyObject(node);
                ontologies.add(o);
            }
        }
        return ontologies;
    }

    /**
     * @param node
     * @return
     */
    private static Ontology toOntologyObject(Node node) {
        NodeList list = node.getChildNodes();
        Ontology o = new Ontology();
        for (int j = 0; j < list.getLength(); j++) {
            String id = null;
            String ontologyId = null;
            Node child = list.item(j);
            if ("ontologyId".equals(child.getTextContent())) {
                ontologyId = child.getTextContent();
                o.setOntologyId(ontologyId);
            } else if ("abbreviation".equals(child.getNodeName())) {
                o.setAbbrevation(child.getTextContent());
            } else if ("description".equals(child.getNodeName())) {
                o.setDescription(child.getTextContent());
            } else if ("displayLabel".equals(child.getNodeName())) {
                o.setDisplayLable(child.getTextContent());
            } else if ("format".equals(child.getNodeName())) {
                o.setFormat(child.getTextContent());
            } else if ("versionNumber".equals(child.getNodeName())) {
                o.setVersion(child.getTextContent());
            } else if ("id".equals(child.getNodeName())) {
                id = child.getTextContent();
                idvsOntologyIdMap.put(id, ontologyId);
            } else if ("ontologyId".equals(child.getNodeName())) {
                ontologyId = child.getTextContent();
                o.setOntologyId(ontologyId);
            }
        }
        return o;
    }

    /**
     * @param node
     * @param ontologyIdsToLookFor
     * @return
     */
    private static boolean isUsedForAnnotation(Node node, Set<String> ontologiesUsedWhileAnnotating) {
        NodeList list = node.getChildNodes();
        boolean isUsedForAnnotation = false;
        for (int j = 0; j < list.getLength(); j++) {
            Node child = list.item(j);
            if ("ontologyId".equals(child.getNodeName())) {
                String id = child.getTextContent();
                isUsedForAnnotation = ontologiesUsedWhileAnnotating.contains(id);
            }
        }
        return isUsedForAnnotation;
//        return true;
    }
    public  Set<String> getOntologiesUsedWhileAnnotating(String xmlResult) {
        Set<String> set = new HashSet<String>();
        NodeList list = getElementsFromXML(xmlResult, "virtualOntologyID", false);
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            set.add(node.getTextContent());
        }
        return set;
    }
    /**
     * @param xmlResult
     * @return
     */
    public List<Resource> toResources(String xmlResult) {
        NodeList list = getElementsFromXML(xmlResult, RestApiInfo.getTagNameResourceRoot(), true);
        List<Resource> resources = new ArrayList<Resource>();
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            resources.add(toResourceObject(node));
        }
        return resources;
    }

    /**
     * @param node
     * @return
     */
    private Resource toResourceObject(Node node) {
        NodeList list = node.getChildNodes();
        Resource r = new Resource();
        for (int j = 0; j < list.getLength(); j++) {
            Node child = list.item(j);
            if (RestApiInfo.getTagNameResourceName().equals(child.getNodeName())) {
                r.setName(child.getTextContent());
            } else if (RestApiInfo.getTagNameResourceDescription().equals(child.getNodeName())) {
                r.setDescription(child.getTextContent());
            } else if (RestApiInfo.getTagNameResourceElementURL().equals(child.getNodeName())) {
                r.setElementURL(child.getTextContent());
            } else if (RestApiInfo.getTagNameResourceLogo().equals(child.getNodeName())) {
                r.setLogoURL(child.getTextContent());
            } else if (RestApiInfo.getTagNameResourceId().equals(child.getNodeName())) {
                r.setResourceId(child.getTextContent());
            } else if (RestApiInfo.getTagNameResourceMainContext().equals(child.getNodeName())) {

                Iterator<String> keyIterator = contextNames.iterator();
                List<Context> contexts = new ArrayList<Context>();

                while (keyIterator.hasNext()) {

                    String key = keyIterator.next();
                    if (r.getResourceId().equals(key.split("_")[0])) {

                        Double weight = weightValuesMap.get(key);
                        String id = ontologyValuesMap.get(key);
                        String ontologyId = idvsOntologyIdMap.get(id);
                        Ontology ontology = OntologyResourceCache.getInstance().getOntology(ontologyId);

                        ContextOntology cntxOntology = new ContextOntology();
                        cntxOntology.setOntology(ontology);

                        Context context = new Context();
                        context.setOntology(cntxOntology);
                        context.setName(key);
                        context.setWeight(weight.floatValue());

                        if (key.equals(child.getTextContent())) {
                            ResourceMainContext mainContext = new ResourceMainContext();
                            mainContext.setContext(context);
                            r.setMainContext(mainContext);
                        }
                        contexts.add(context);

                        ResourceContext rContext =
                                new ResourceContext(contexts.toArray(new Context[contexts.size()]));
                        r.setContext(rContext);
                    }
                }
            }
        }
        return r;
    }

    private Document getDocument(String xmlString) {
        Document doc = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource src = new InputSource(new StringReader(xmlString));

        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            doc = documentBuilder.parse(src);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * @param file
     * @param nodeName
     * @param flag
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException 
     */
    private NodeList getElementsFromXML(String file, String nodeName, boolean flag) {
        Document doc = getDocument(file);
        if (flag) {
            setValues(doc);
        }
        NodeList list = doc.getElementsByTagName(nodeName);
        return list;
    }

    /**
     * @param doc
     * @throws XPathExpressionException
     * @throws FileNotFoundException
     * @throws SAXException
     * @throws IOException
     */
    private void setValues(Document doc) {
        XPathFactory fact = XPathFactory.newInstance();
        XPath xpath = fact.newXPath();
        setAllResourceIds(doc, xpath, "//resourceID/text()");
        setAllContextsName(doc, xpath, "//contexts/entry/string/text()");
        setOntologyValues(doc, xpath, "//ontoIDs/entry/string/text()");
        setWeightValues(doc, xpath);

    }

    /**
     * @param doc
     * @param xpath
     * @param compileValue
     * @throws XPathExpressionException
     */
    private void setAllResourceIds(Document doc, XPath xpath, String compileValue) {
        NodeList list = evaluate(doc, xpath, compileValue);
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            resourceIds.add(node.getTextContent());
        }

    }

    /**
     * @param doc
     * @param xpath
     * @param compileValue
     * @throws XPathExpressionException
     */
    private void setAllContextsName(Document doc, XPath xpath, String compileValue) {

        NodeList list = evaluate(doc, xpath, compileValue);
        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            contextNames.add(node.getTextContent());
        }

    }

    /**
     * @param doc
     * @param xpath
     * @param compileValue
     * @throws XPathExpressionException
     */
    private void setOntologyValues(Document doc, XPath xpath, String compileValue) {
        NodeList list = evaluate(doc, xpath, compileValue);
        for (int j = 0; j < list.getLength(); j++) {
            String key = list.item(j).getTextContent();
            String values = list.item(++j).getTextContent();
            ontologyValuesMap.put(key, values);
        }
    }

    /**
     * @param doc
     * @param xpath
     * @throws XPathExpressionException
     */
    private void setWeightValues(Document doc, XPath xpath) {
        NodeList list = evaluate(doc, xpath, "//weights/entry/string/text()");
        NodeList doubleList = evaluate(doc, xpath, "//weights/entry/double/text()");
        for (int j = 0; j < list.getLength(); j++) {
            String key = list.item(j).getTextContent();
            Double values = Double.valueOf(doubleList.item(j).getTextContent());
            weightValuesMap.put(key, values);
        }
    }

    /**
     * @param annotationXML
     * @return
     */
    public List<Annotation> getAnnotation(String annotationXML) {
        Document doc = getDocument(annotationXML);

        XPathFactory fact = XPathFactory.newInstance();
        XPath xpath = fact.newXPath();
        List<Annotation> annotationList = new ArrayList<Annotation>();
        String annotationPath = "//directAnnotations/obs.common.beans.ObrAnnotationBeanDetailled";
        setAllAnnotation(annotationList, doc, xpath, true, annotationPath);

        annotationPath = "//mappingAnnotations/obs.common.beans.ObrAnnotationBeanDetailled";
        setAllAnnotation(annotationList, doc, xpath, true, annotationPath);

        return annotationList;

    }

    /**
     * @param annotationList
     * @param doc
     * @param xpath
     * @param isDirectAnnotation
     * @param annotationPath
     * @throws XPathExpressionException
     */
    void setAllAnnotation(List<Annotation> annotationList, Document doc, XPath xpath, boolean isDirectAnnotation,
                          String annotationPath) {
        NodeList list = evaluate(doc, xpath, annotationPath);

        for (int j = 0; j < list.getLength(); j++) {

            Node node = list.item(j);
            Annotation annotation = null;
            String contextName = null;
            boolean isDirect = false;
            BigInteger termID = null;
            String termName = null;
            Concept concept = null;
            String localElementID = null;
            String annotationDescription = null;
            float score = 0.0f;

            NodeList chilrenNodeList = node.getChildNodes();

            for (int k = 0; k < chilrenNodeList.getLength(); k++) {
                Node child = chilrenNodeList.item(k);
                if ("concept".equals(child.getNodeName())) {
                    concept = toConceptObject(child);
                } else if ("context".equals(child.getNodeName())) {
                    contextName = getChild(child, "contextName").getTextContent();
                    isDirect = Boolean.parseBoolean(getChild(child, "isDirect").getTextContent());
                    Node nodeTermID = getChild(child, "termID");
                    if (!(nodeTermID == null)) {

                        termID = BigInteger.valueOf(Long.parseLong(nodeTermID.getTextContent()));
                    }

                    Node nodeTermName = getChild(child, "termName");

                    if (!(nodeTermName == null)) {
                        termName = nodeTermName.getTextContent();
                    }

                } else if ("localElementID".equals(child.getNodeName())) {
                    localElementID = child.getTextContent();
                } else if ("score".equals(child.getNodeName())) {
                    score = Float.parseFloat(child.getTextContent());
                } else if ("element".equals(child.getNodeName())) {

                    Node elementStructure = getChild(child, "elementStructure");
                    Node resourceId = getChild(elementStructure, "resourceID");
                    Node contexts = getChild(elementStructure, "contexts");
                    List<Node> entries = getChildren(contexts, "entry");
                    for (Node entry : entries) {
                        String entryContextName = entry.getChildNodes().item(1).getTextContent();
                        String entryContextValue = entry.getChildNodes().item(3).getTextContent();

                        Resource resource =
                                OntologyResourceCache.getInstance().getResource(resourceId.getTextContent());

                        int length = resource.getContext().getContext().length;
                        AnnotationContext[] annotationContexts = new AnnotationContext[length];
                        int i = 0;
                        for (Context resourceContext : resource.getContext().getContext()) {
                            if (resourceContext.getName().equals(entryContextName)) {
                                AnnotationContextContext annotationContextContext = new AnnotationContextContext();
                                annotationContextContext.setContext(resourceContext);
                                isDirect = entryContextName.equals(contextName);
                                AnnotationContext annotationContext =
                                        new AnnotationContext(annotationContextContext, isDirect, termID,
                                                termName, entryContextValue);

                                annotationContexts[i] = annotationContext;
                                i++;
                                if (resource.getMainContext().getContext().getName().equals(entryContextName)) {
                                    annotationDescription = entryContextValue;
                                }
                            }

                        }
                        AnnotationConcept annotationConcept = new AnnotationConcept(concept);
                        AnnotationAnnotationContext annotationAnnotationContext =
                                new AnnotationAnnotationContext(annotationContexts);

                        AnnotationResource annotationResource = new AnnotationResource(resource);

                        String annotationURL = resource.getElementURL() + localElementID;
                        annotation =
                                new Annotation(annotationAnnotationContext, annotationConcept,
                                        annotationDescription, localElementID, isDirectAnnotation,
                                        annotationResource, score, annotationURL);

                    }
                }
            }
            annotationList.add(annotation);
        }
    }

    private NodeList evaluate(Document doc, XPath xpath, String compileValue) {
        try {
            XPathExpression expr = xpath.compile(compileValue);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            return (NodeList) result;
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param node
     * @param matchingvalue
     * @return
     */
    private Node getChild(Node node, String matchingvalue) {

        NodeList list = node.getChildNodes();
        for (int j = 0; j < list.getLength(); j++) {
            Node childeNode = list.item(j);
            if (childeNode.getNodeName().equals(matchingvalue)) {
                return childeNode;
            }
        }
        return null;
    }

    /**
     * @param node
     * @param matchingvalue
     * @return
     */
    private List<Node> getChildren(Node node, String matchingvalue) {

        NodeList list = node.getChildNodes();

        List<Node> retrunList = new ArrayList<Node>();

        for (int j = 0; j < list.getLength(); j++) {
            Node childeNode = list.item(j);
            if (childeNode.getNodeName().equals(matchingvalue)) {
                retrunList.add(childeNode);
            }
        }
        return retrunList;
    }
}