package edu.wustl.caobr.client;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

/**
 * @author chandrakant_talele
 * @author lalit_chand
 */
public class SwingClientcaObrService extends JFrame {
    private static final long serialVersionUID = 1L;

    //    private final String resultHeader = " List of annotations retrieved for : ";
    private JList ontologyList = null;

    private JList resourceList = null;

    private JTextField keywordtext = null;

    private JEditorPane resultPanel = null;

    private JLabel resultLabel = null;

    private Map<String, Ontology> mapOntoId = null;

    private Map<String, Resource> mapResourceId = null;

    private Font font = new JList().getFont();

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(new File("c:/dbdump/out_"+System.currentTimeMillis()+".xml")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        SwingClientcaObrService frame = new SwingClientcaObrService();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(getStartPosition(frame.getWidth(), frame.getHeight()));
        frame.setResizable(true);
        frame.setVisible(true);
    }

    public SwingClientcaObrService() {
        super("caOBR Client");
        JLabel keywordLabel = new JLabel("                                Enter Keyword : ");
        keywordLabel.setFont(font.deriveFont(Font.BOLD, font.getSize() + 1F));
        keywordtext = new JTextField(20);

        JPanel keywordPanel = new JPanel(new GridLayout(2, 3));

        keywordPanel.add(keywordLabel);
        keywordPanel.add(keywordtext);
        keywordPanel.add(new JLabel("  "));

        keywordPanel.add(new JLabel(" "));
        keywordPanel.add(new JLabel(" "));
        keywordPanel.add(new JLabel(" "));

        JPanel upperAnother = new JPanel(new BorderLayout());
        upperAnother.add(new JLabel("  "), BorderLayout.NORTH);
        upperAnother.add(keywordPanel, BorderLayout.CENTER);

        JLabel ontologiesLabel = new JLabel("Select Ontologies");
        JLabel recourcesLabel = new JLabel("Select Resources");

        JScrollPane ontologiesListScroller = getOntologyList();
        JScrollPane resourcesListScroller = getResourceList();

        JPanel gridPanel1 = new JPanel(new BorderLayout());
        gridPanel1.add(ontologiesLabel, BorderLayout.NORTH);
        gridPanel1.add(ontologiesListScroller, BorderLayout.LINE_START);

        JPanel gridPanel2 = new JPanel(new BorderLayout());
        gridPanel2.add(recourcesLabel, BorderLayout.NORTH);
        gridPanel2.add(resourcesListScroller, BorderLayout.LINE_START);

        JPanel gridPanel3 = new JPanel(new FlowLayout());
        gridPanel3.add(gridPanel1);
        gridPanel3.add(gridPanel2);

        resultPanel = new JEditorPane();
        resultPanel.setContentType("text/html");
        resultPanel.setEditable(false);
        resultPanel.addHyperlinkListener(new ActivatedHyperlinkListener(this, resultPanel));
        JPanel rPanel = new JPanel(new GridLayout(0, 1));
        rPanel.add(new JScrollPane(resultPanel));

        JPanel full = new JPanel(new BorderLayout());
        full.add(upperAnother, BorderLayout.NORTH);
        full.add(gridPanel3, BorderLayout.CENTER);
        full.add(getButtonPanel(), BorderLayout.SOUTH);

        JPanel full2 = new JPanel(new GridLayout(2, 1));
        full2.add(full);
        full2.add(rPanel);
        add(full2);

    }

    private JPanel getButtonPanel() {
        JButton button = new JButton("Search !");
        button.addActionListener(new SearchButtonActionListener());
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(button);
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));
        buttonPanel.add(new JLabel(" "));

        resultLabel = new JLabel(" List of annotations retrieved");
        resultLabel.setFont(font.deriveFont(font.getSize() + 1F));
        buttonPanel.add(resultLabel);
        buttonPanel.add(new JLabel(" "));
        return buttonPanel;
    }

    private static Point getStartPosition(int width, int height) {
        DisplayMode displayMode =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        Float w = (displayMode.getWidth() - width) / 2F;
        Float h = (displayMode.getHeight() - height) / 2F;
        return new Point(w.intValue(), h.intValue());
    }

    private JScrollPane getOntologyList() {
        Ontology[] ontologies = new ServiceInvoker().getOntologies();
        String[] ontoId = new String[ontologies.length];
        mapOntoId = new HashMap<String, Ontology>();
        int i = 0;
        for (Ontology bean : ontologies) {
            ontoId[i] = bean.getDisplayLable();
            mapOntoId.put(ontoId[i], bean);
            i++;
        }

        ontologyList = getList(ontoId); //data has type Object[]
        JScrollPane listScroller = new JScrollPane(ontologyList);
        listScroller.setPreferredSize(new Dimension(300, 150));
        return listScroller;
    }

    private JScrollPane getResourceList() {
        Resource[] resources = new ServiceInvoker().getResources();
        mapResourceId = new HashMap<String, Resource>();
        String[] resourceId = new String[resources.length];
        int i = 0;
        for (Resource re : resources) {
            resourceId[i] = re.getName();
            mapResourceId.put(resourceId[i], re);
            i++;
        }
        resourceList = getList(resourceId);
        JScrollPane listScroller = new JScrollPane(resourceList);
        listScroller.setPreferredSize(new Dimension(300, 150));
        return listScroller;
    }

    private JList getList(Object[] data) {
        JList list = new JList(data); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setFont(font.deriveFont(Font.PLAIN));
        return list;
    }

    private Annotation[] getAnnotations(Ontology[] fromOntologies, Resource[] fromResources, String term) {
        AnnotationServiceThreadPool mt = new AnnotationServiceThreadPool();
        return mt.getAnnotations(fromOntologies, fromResources, term);
    }

    class SearchButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            resultPanel.repaint();
            String term = keywordtext.getText();
            System.out.println("User Input :" + term);
            //            resultLabel.setText( " List of annotations retrieved for : '" + term.toString() + "'");
            //            resultLabel.repaint();
            if (ontologyList.getSelectionModel().isSelectionEmpty()
                    || resourceList.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(SwingClientcaObrService.this, "No Resource / Ontology is selected",
                                              "Cannot Proceed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> selectedOntologies = getUserSelection(ontologyList);
            Ontology[] ontologyBeans = new Ontology[selectedOntologies.size()];
            for (int i = 0; i < selectedOntologies.size(); i++) {
                mapOntoId.get(selectedOntologies.get(i));
                ontologyBeans[i] = mapOntoId.get(selectedOntologies.get(i));
            }

            List<String> selectedResources = getUserSelection(resourceList);
            Resource[] resources = new Resource[selectedResources.size()];
            for (int i = 0; i < selectedResources.size(); i++) {
                resources[i] = mapResourceId.get(selectedResources.get(i));
            }

            /*                   annotations = new CaObrImpl().getAnnotations(ontologyBeans.toArray(new Ontology[ontologyBeans.size()]),
                                                                   resources.toArray(new Resource[resources.size()]), term);
            */
            /* annotations = client.getAnnotations(ontologyBeans.toArray(new Ontology[ontologyBeans.size()]),
                                                  resources.toArray(new Resource[resources.size()]), term);
             */
            Annotation[] annotations = getAnnotations(ontologyBeans, resources, term);

            List<Annotation> directAnnotations = new ArrayList<Annotation>();
            List<Annotation> mappedAnnotations = new ArrayList<Annotation>();
            if (annotations != null && annotations.length != 0) {
                for (Annotation ann : annotations) {
                    if (ann.isIsDirect()) {
                        directAnnotations.add(ann);
                    } else {
                        mappedAnnotations.add(ann);
                    }
                }
            }
            resultPanel.setText(getHtmlString(directAnnotations, mappedAnnotations));
        }

        /**
         * Find out which indexes are selected.
         * @param list
         * @return
         */
        private List<String> getUserSelection(JList list) {
            ListSelectionModel lsm = list.getSelectionModel();
            List<String> selections = new ArrayList<String>();
            int min = lsm.getMinSelectionIndex();
            int max = lsm.getMaxSelectionIndex();
            for (int i = min; i <= max; i++) {
                if (lsm.isSelectedIndex(i)) {
                    String name = (String) list.getModel().getElementAt(i);
                    selections.add(name);
                }
            }
            return selections;
        }

        private String getHtmlString(List<Annotation> directAnnotations, List<Annotation> mappedAnnotations) {
            StringBuffer buff = new StringBuffer("<html>  <head>  </head>  <body>");

            addAnnotationUrls(directAnnotations, buff, "Direct");
            addAnnotationUrls(mappedAnnotations, buff, "Mapped");

            buff.append("</body></html>");
            return buff.toString();
        }

        private void addAnnotationUrls(List<Annotation> annotations, StringBuffer buff, String name) {
            buff.append("Number of ");
            buff.append(name);
            buff.append(" Annotations found: ");
            buff.append(annotations.size());
            buff.append("<hr />");
            int i=1;
            for (Annotation ann : annotations) {
                
                buff.append(i);
                buff.append(". Resource <b>");
                buff.append(ann.getResource().getResource().getName());
                buff.append(". </b><a href=\"");
                buff.append(ann.getUrl());
                buff.append("\">");
                buff.append("Element id=");
                buff.append(ann.getElementId());
                buff.append("</a> <br> <i> Description: </i>");
                buff.append(ann.getDescription());
                buff.append("<hr />");
                
                System.out.println("---------------------------------------------------");
                System.out.println(i+". Resource : " + ann.getResource().getResource().getName());
                System.out.println("Element id : " + ann.getElementId());
                System.out.println("Description : " + ann.getDescription());
                i++;
            }
            buff.append("<br>");
        }
    }

    class ActivatedHyperlinkListener implements HyperlinkListener {
        Frame frame;

        JEditorPane editorPane;

        public ActivatedHyperlinkListener(Frame frame, JEditorPane editorPane) {
            this.frame = frame;
            this.editorPane = editorPane;
        }

        public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
            HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
            final URL url = hyperlinkEvent.getURL();
            if (type == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(url.toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}