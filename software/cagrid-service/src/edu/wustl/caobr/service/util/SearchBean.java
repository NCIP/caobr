/*L
 * Copyright Washington University at St. Louis and Persistent Systems
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caobr/LICENSE.txt for details.
 */

package edu.wustl.caobr.service.util;

/**
 * A base search bean that define a single search result entity
 * @author lalit_chand
 */
public class SearchBean {

    private String ontologyVersionId;

    private String ontologyId;

    private String conceptId;

    private String conceptIdShort;

    private String preferredName;

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public SearchBean() {
    }

    /**
     * @return the ontologyVersionId
     */
    public String getOntologyVersionId() {
        return ontologyVersionId;
    }

    /**
     * @param ontologyVersionId
     *            the ontologyVersionId to set
     */
    public void setOntologyVersionId(String ontologyVersionId) {
        this.ontologyVersionId = ontologyVersionId;
    }

    /**
     * @return the ontologyId
     */
    public String getOntologyId() {
        return ontologyId;
    }

    /**
     * @param ontologyId
     *            the ontologyId to set
     */
    public void setOntologyId(String ontologyId) {
        this.ontologyId = ontologyId;
    }

    /**
     * @return the conceptId
     */
    public String getConceptId() {
        return conceptId;
    }

    /**
     * @param conceptId
     *            the conceptId to set
     */
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    /**
     * @return the conceptIdShort
     */
    public String getConceptIdShort() {
        return conceptIdShort;
    }

    /**
     * @param conceptIdShort
     *            the conceptIdShort to set
     */
    public void setConceptIdShort(String conceptIdShort) {
        this.conceptIdShort = conceptIdShort;
    }
}