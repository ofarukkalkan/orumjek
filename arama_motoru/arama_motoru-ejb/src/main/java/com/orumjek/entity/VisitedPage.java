/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "visited_page")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VisitedPage.findAll", query = "SELECT v FROM VisitedPage v")
    , @NamedQuery(name = "VisitedPage.findById", query = "SELECT v FROM VisitedPage v WHERE v.id = :id")})
public class VisitedPage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Page pageId;
    @JoinColumn(name = "visit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Visit visitId;

    public VisitedPage() {
    }

    public VisitedPage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Page getPageId() {
        return pageId;
    }

    public void setPageId(Page pageId) {
        this.pageId = pageId;
    }

    public Visit getVisitId() {
        return visitId;
    }

    public void setVisitId(Visit visitId) {
        this.visitId = visitId;
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
        if (!(object instanceof VisitedPage)) {
            return false;
        }
        VisitedPage other = (VisitedPage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.orumjek.entity.VisitedPage[ id=" + id + " ]";
    }
    
}
