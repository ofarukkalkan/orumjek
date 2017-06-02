/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.sesion_bean;

import com.orumjek.entity.Page;
import com.orumjek.entity.Visit;
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
public class VisitCtr implements VisitCtrLocal {

    @PersistenceContext(unitName = "com.orumjek_arama_motoru-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    @Override
    public void createVisit(Page startPage, short depth) {
        Visit visit = new Visit();
        visit.setId(System.currentTimeMillis());
        visit.setStartPage(startPage);
        visit.setDepth(depth);
        
        persist(visit);
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

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
