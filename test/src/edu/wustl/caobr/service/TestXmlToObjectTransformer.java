package edu.wustl.caobr.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

public class TestXmlToObjectTransformer extends TestCase {

    public void testToOntologiesFail() {
        String xml = getXmlString("test/src/edu/wustl/caobr/service/ontologies.xml");
        List<Ontology> ontologies = new XmlToObjectTransformer().toOntologies(xml, new HashMap<String, String>());
        assertEquals(0, ontologies.size());

    }
    
    public void testToOntologiesSuccess() {
        Map<String, String> idSet = new HashMap<String, String>();
        idSet.put("1083","40655");

        String xml = getXmlString("test/src/edu/wustl/caobr/service/ontologies.xml");


        List<Ontology> ontologies = new XmlToObjectTransformer().toOntologies(xml, idSet);
        assertEquals(1, ontologies.size());
        assertEquals("1083",ontologies.get(0).getOntologyId());

    }

    public void testToResources() {
        String xml = getXmlString("test/src/edu/wustl/caobr/service/resources.xml");
        List<Resource> resources = new XmlToObjectTransformer().toResources(xml);
        assertEquals(20,resources.size());
        
    }

    private String getXmlString(String file) {
        StringBuilder buffer = new StringBuilder();
        File f = new File(file);
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));

            while ((result = br.readLine()) != null) {
                buffer.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}