package edu.wustl.caobr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.wustl.caobr.Ontology;

public class OntologyXmlParser extends DefaultHandler {

    List<Ontology> myEmpls = new ArrayList<Ontology>();

    private String tempVal;

    //to maintain context
    private Ontology tempEmp;

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("test/src/edu/wustl/caobr/service/ontologies.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {

        for (Ontology o : myEmpls) {
            System.out.println("------------------------------");
            System.out.print(o.getAbbrevation());
            System.out.println("  ");
            System.out.print(o.getDescription());
            System.out.println("  ");
            System.out.print(o.getDisplayLable());
            System.out.println("  ");
            System.out.print(o.getFormat());
            System.out.println("  ");
            System.out.print(o.getOntologyId());
            System.out.println("  ");
            System.out.print(o.getVersion());
            System.out.println("");
        }

    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("ontologyBean")) {
            //create a new instance of employee
            tempEmp = new Ontology();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("ontologyBean")) {
            //add it to the list
            myEmpls.add(tempEmp);

        } else if (qName.equalsIgnoreCase("abbreviation")) {
            tempEmp.setAbbrevation(tempVal);
        } else if (qName.equalsIgnoreCase("description")) {
            tempEmp.setDescription(tempVal);
        } else if (qName.equalsIgnoreCase("displayLabel")) {
            tempEmp.setDisplayLable(tempVal);
        } else if (qName.equalsIgnoreCase("format")) {
            tempEmp.setFormat(tempVal);
        } else if (qName.equalsIgnoreCase("ontologyId")) {
            tempEmp.setOntologyId(tempVal);
        } else if (qName.equalsIgnoreCase("versionNumber")) {
            tempEmp.setVersion(tempVal);
        }

    }

    public static void main(String[] args) {
        OntologyXmlParser spe = new OntologyXmlParser();
        spe.runExample();
    }

}
