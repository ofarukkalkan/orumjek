/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "page")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Page.findAll", query = "SELECT p FROM Page p")
    , @NamedQuery(name = "Page.findById", query = "SELECT p FROM Page p WHERE p.id = :id")
    , @NamedQuery(name = "Page.findByUrl", query = "SELECT p FROM Page p WHERE p.url = :url")
    , @NamedQuery(name = "Page.findByScore", query = "SELECT p FROM Page p WHERE p.score = :score")})
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3072)
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "html_data")
    private String htmlData;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "text_data")
    private String textData;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private double score;
    @JoinTable(name = "page_link", joinColumns = {
        @JoinColumn(name = "reference_page_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "source_page_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Page> pageCollection;
    @ManyToMany(mappedBy = "pageCollection")
    private Collection<Page> pageCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "page")
    private Collection<KeywordIndex> keywordIndexCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pageId")
    private Collection<PlannedPage> plannedPageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pageId")
    private Collection<VisitedPage> visitedPageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startPage")
    private Collection<Visit> visitCollection;

    public Page() {
    }

    public Page(Long id) {
        this.id = id;
    }

    public Page(Long id, String url, String htmlData, String textData, double score) {
        this.id = id;
        this.url = url;
        this.htmlData = htmlData;
        this.textData = textData;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @XmlTransient
    public Collection<Page> getPageCollection() {
        return pageCollection;
    }

    public void setPageCollection(Collection<Page> pageCollection) {
        this.pageCollection = pageCollection;
    }

    @XmlTransient
    public Collection<Page> getPageCollection1() {
        return pageCollection1;
    }

    public void setPageCollection1(Collection<Page> pageCollection1) {
        this.pageCollection1 = pageCollection1;
    }

    @XmlTransient
    public Collection<KeywordIndex> getKeywordIndexCollection() {
        return keywordIndexCollection;
    }

    public void setKeywordIndexCollection(Collection<KeywordIndex> keywordIndexCollection) {
        this.keywordIndexCollection = keywordIndexCollection;
    }

    @XmlTransient
    public Collection<PlannedPage> getPlannedPageCollection() {
        return plannedPageCollection;
    }

    public void setPlannedPageCollection(Collection<PlannedPage> plannedPageCollection) {
        this.plannedPageCollection = plannedPageCollection;
    }

    @XmlTransient
    public Collection<VisitedPage> getVisitedPageCollection() {
        return visitedPageCollection;
    }

    public void setVisitedPageCollection(Collection<VisitedPage> visitedPageCollection) {
        this.visitedPageCollection = visitedPageCollection;
    }

    @XmlTransient
    public Collection<Visit> getVisitCollection() {
        return visitCollection;
    }

    public void setVisitCollection(Collection<Visit> visitCollection) {
        this.visitCollection = visitCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Page)) {
            return false;
        }
        Page other = (Page) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.orumjek.entity.Page[ id=" + id + " ]";
    }
    
}
