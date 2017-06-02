/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.sesion_bean;

import com.orumjek.entity.Page;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omerfaruk
 */
@Stateless
public class PageCtr implements PageCtrLocal {

    @PersistenceContext(unitName = "com.orumjek_arama_motoru-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void create(String url, String htmlData, String textData, double score) {
        Page page = new Page(System.currentTimeMillis(), url, htmlData, textData, 0);
        persist(page);
    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
