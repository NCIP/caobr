package edu.wustl.caobr.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;
import edu.wustl.caobr.service.util.RestApiInfo;

public class XmlToObjectTransformerTest extends TestCase {
public void test() {
    String targetUrl = RestApiInfo.getOntologyURL();
    String result = RestApiInvoker.getResult(targetUrl);
    Set<String> idSet = new XmlToObjectTransformer().getOntologiesUsedWhileAnnotating(result);
    
}
//        public void testToOntologies() {
//            String xml = getXmlString("test/src/edu/wustl/caobr/service/ontologies.xml");
//            Set<String> set = new HashSet<String>();
//            set.add("39336");
//            set.add("40734");
//            
//            List<Ontology> ontologies = new XmlToObjectTransformer().toOntologies(xml,set);
//            printOntologies(ontologies);
//        }

//    public void testToResources() {
//        String xml = getXmlString("test/src/edu/wustl/caobr/service/resources.xml");
//        List<Resource> resources = new XmlToObjectTransformer().toResources(xml);
//        printResources(resources);
//    }

    private void printResources(List<Resource> resources) {
        System.out.println("-----------------------------------------------------------");
        System.out.println("              Resource Details");
        System.out.println("-----------------------------------------------------------");
        for (Resource r : resources) {

            System.out.println("Name  : " + r.getName());
            System.out.println("rscID : " + r.getResourceId());
            System.out.println("url   : " + r.getElementURL());
            System.out.println("Logo  : " + r.getLogoURL());
            System.out.println("mCtx  : " + r.getMainContext());
            System.out.println("ctxs  : " + r.getContext());
            System.out.println("Desc  : " + r.getDescription());
            System.out.println("-----------------------------------------------------------");
        }
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

    private void printOntologies(List<Ontology> ontologies) {
        System.out.println("-----------------------------------------------------------");
        System.out.println("OntoId\tAbbr\t\tFormat\tVer\tLable\tDesc");
        System.out.println("-----------------------------------------------------------");
        for (Ontology o : ontologies) {
            System.out.print(o.getOntologyId());
            System.out.print("\t" + o.getAbbrevation());
            System.out.print("\t\t" + o.getFormat());
            System.out.print("\t" + o.getVersion());
            System.out.print("\t" + o.getDisplayLable());
            System.out.print("\t" + o.getDescription() + "\n");
        }
    }
}
