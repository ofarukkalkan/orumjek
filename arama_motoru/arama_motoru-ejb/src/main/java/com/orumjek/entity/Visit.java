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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "visit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visit.findAll", query = "SELECT v FROM Visit v")
    , @NamedQuery(name = "Visit.findById", query = "SELECT v FROM Visit v WHERE v.id = :id")
    , @NamedQuery(name = "Visit.findByDepth", query = "SELECT v FROM Visit v WHERE v.depth = :depth")})
public class Visit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "depth")
    private short depth;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visitId")
    private Collection<PlannedPage> plannedPageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visitId")
    private Collection<VisitedPage> visitedPageCollection;
    @JoinColumn(name = "start_page", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Page startPage;

    public Visit() {
    }

    public Visit(Long id) {
        this.id = id;
    }

    public Visit(Long id, short depth) {
        this.id = id;
        this.depth = depth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
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

    public Page getStartPage() {
        return startPage;
    }

    public void setStartPage(Page startPage) {
        this.startPage = startPage;
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
        if (!(object instanceof Visit)) {
            return false;
        }
        Visit other = (Visit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.orumjek.entity.Visit[ id=" + id + " ]";
    }
    
}
