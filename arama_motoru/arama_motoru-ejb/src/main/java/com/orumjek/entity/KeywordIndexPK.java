/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author omerfaruk
 */
@Embeddable
public class KeywordIndexPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "page_id")
    private long pageId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "keyword_id")
    private String keywordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "keyword_pos")
    private int keywordPos;

    public KeywordIndexPK() {
    }

    public KeywordIndexPK(long pageId, String keywordId, int keywordPos) {
        this.pageId = pageId;
        this.keywordId = keywordId;
        this.keywordPos = keywordPos;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public int getKeywordPos() {
        return keywordPos;
    }

    public void setKeywordPos(int keywordPos) {
        this.keywordPos = keywordPos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pageId;
        hash += (keywordId != null ? keywordId.hashCode() : 0);
        hash += (int) keywordPos;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KeywordIndexPK)) {
            return false;
        }
        KeywordIndexPK other = (KeywordIndexPK) object;
        if (this.pageId != other.pageId) {
            return false;
        }
        if ((this.keywordId == null && other.keywordId != null) || (this.keywordId != null && !this.keywordId.equals(other.keywordId))) {
            return false;
        }
        if (this.keywordPos != other.keywordPos) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.orumjek.entity.KeywordIndexPK[ pageId=" + pageId + ", keywordId=" + keywordId + ", keywordPos=" + keywordPos + " ]";
    }
    
}
