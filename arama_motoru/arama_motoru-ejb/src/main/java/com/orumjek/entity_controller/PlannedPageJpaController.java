/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.Page;
import com.orumjek.entity.PlannedPage;
import com.orumjek.entity.Visit;
import com.orumjek.entity.exceptions.NonexistentEntityException;
import com.orumjek.entity.exceptions.PreexistingEntityException;
import com.orumjek.entity.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author omerfaruk
 */
public class PlannedPageJpaController implements Serializable {

    public PlannedPageJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlannedPage plannedPage) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Page pageId = plannedPage.getPageId();
            if (pageId != null) {
                pageId = em.getReference(pageId.getClass(), pageId.getId());
                plannedPage.setPageId(pageId);
            }
            Visit visitId = plannedPage.getVisitId();
            if (visitId != null) {
                visitId = em.getReference(visitId.getClass(), visitId.getId());
                plannedPage.setVisitId(visitId);
            }
            em.persist(plannedPage);
            if (pageId != null) {
                pageId.getPlannedPageCollection().add(plannedPage);
                pageId = em.merge(pageId);
            }
            if (visitId != null) {
                visitId.getPlannedPageCollection().add(plannedPage);
                visitId = em.merge(visitId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlannedPage(plannedPage.getId()) != null) {
                throw new PreexistingEntityException("PlannedPage " + plannedPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlannedPage plannedPage) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlannedPage persistentPlannedPage = em.find(PlannedPage.class, plannedPage.getId());
            Page pageIdOld = persistentPlannedPage.getPageId();
            Page pageIdNew = plannedPage.getPageId();
            Visit visitIdOld = persistentPlannedPage.getVisitId();
            Visit visitIdNew = plannedPage.getVisitId();
            if (pageIdNew != null) {
                pageIdNew = em.getReference(pageIdNew.getClass(), pageIdNew.getId());
                plannedPage.setPageId(pageIdNew);
            }
            if (visitIdNew != null) {
                visitIdNew = em.getReference(visitIdNew.getClass(), visitIdNew.getId());
                plannedPage.setVisitId(visitIdNew);
            }
            plannedPage = em.merge(plannedPage);
            if (pageIdOld != null && !pageIdOld.equals(pageIdNew)) {
                pageIdOld.getPlannedPageCollection().remove(plannedPage);
                pageIdOld = em.merge(pageIdOld);
            }
            if (pageIdNew != null && !pageIdNew.equals(pageIdOld)) {
                pageIdNew.getPlannedPageCollection().add(plannedPage);
                pageIdNew = em.merge(pageIdNew);
            }
            if (visitIdOld != null && !visitIdOld.equals(visitIdNew)) {
                visitIdOld.getPlannedPageCollection().remove(plannedPage);
                visitIdOld = em.merge(visitIdOld);
            }
            if (visitIdNew != null && !visitIdNew.equals(visitIdOld)) {
                visitIdNew.getPlannedPageCollection().add(plannedPage);
                visitIdNew = em.merge(visitIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = plannedPage.getId();
                if (findPlannedPage(id) == null) {
                    throw new NonexistentEntityException("The plannedPage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlannedPage plannedPage;
            try {
                plannedPage = em.getReference(PlannedPage.class, id);
                plannedPage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plannedPage with id " + id + " no longer exists.", enfe);
            }
            Page pageId = plannedPage.getPageId();
            if (pageId != null) {
                pageId.getPlannedPageCollection().remove(plannedPage);
                pageId = em.merge(pageId);
            }
            Visit visitId = plannedPage.getVisitId();
            if (visitId != null) {
                visitId.getPlannedPageCollection().remove(plannedPage);
                visitId = em.merge(visitId);
            }
            em.remove(plannedPage);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlannedPage> findPlannedPageEntities() {
        return findPlannedPageEntities(true, -1, -1);
    }

    public List<PlannedPage> findPlannedPageEntities(int maxResults, int firstResult) {
        return findPlannedPageEntities(false, maxResults, firstResult);
    }

    private List<PlannedPage> findPlannedPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlannedPage.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PlannedPage findPlannedPage(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlannedPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlannedPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlannedPage> rt = cq.from(PlannedPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
