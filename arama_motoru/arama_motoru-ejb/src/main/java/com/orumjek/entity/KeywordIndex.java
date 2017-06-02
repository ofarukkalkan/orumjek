/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "keyword_index")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KeywordIndex.findAll", query = "SELECT k FROM KeywordIndex k")
    , @NamedQuery(name = "KeywordIndex.findByPageId", query = "SELECT k FROM KeywordIndex k WHERE k.keywordIndexPK.pageId = :pageId")
    , @NamedQuery(name = "KeywordIndex.findByKeywordId", query = "SELECT k FROM KeywordIndex k WHERE k.keywordIndexPK.keywordId = :keywordId")
    , @NamedQuery(name = "KeywordIndex.findByKeywordPos", query = "SELECT k FROM KeywordIndex k WHERE k.keywordIndexPK.keywordPos = :keywordPos")})
public class KeywordIndex implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KeywordIndexPK keywordIndexPK;
    @JoinColumn(name = "keyword_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Keyword keyword;
    @JoinColumn(name = "page_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Page page;

    public KeywordIndex() {
    }

    public KeywordIndex(KeywordIndexPK keywordIndexPK) {
        this.keywordIndexPK = keywordIndexPK;
    }

    public KeywordIndex(long pageId, String keywordId, int keywordPos) {
        this.keywordIndexPK = new KeywordIndexPK(pageId, keywordId, keywordPos);
    }

    public KeywordIndexPK getKeywordIndexPK() {
        return keywordIndexPK;
    }

    public void setKeywordIndexPK(KeywordIndexPK keywordIndexPK) {
        this.keywordIndexPK = keywordIndexPK;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (keywordIndexPK != null ? keywordIndexPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KeywordIndex)) {
            return false;
        }
        KeywordIndex other = (KeywordIndex) object;
        if ((this.keywordIndexPK == null && other.keywordIndexPK != null) || (this.keywordIndexPK != null && !this.keywordIndexPK.equals(other.keywordIndexPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.orumjek.entity.KeywordIndex[ keywordIndexPK=" + keywordIndexPK + " ]";
    }
    
}
