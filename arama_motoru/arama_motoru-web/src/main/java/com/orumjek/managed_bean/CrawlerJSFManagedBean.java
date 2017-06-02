/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.managed_bean;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author omerfaruk
 */
@Named(value = "crawlerJSFManagedBean")
@Dependent
public class CrawlerJSFManagedBean {

    /**
     * Creates a new instance of CrawlerJSFManagedBean
     */
    public CrawlerJSFManagedBean() {
    }

    public void newPage(String url, String htmlData, String textData, double score) {
        PageCtrLocal pageCtr = (PageCtrLocal)
        pageCtr.create(url, htmlData, textData, score);
    }
}
