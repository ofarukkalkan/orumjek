/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.Page;
import com.orumjek.entity.Visit;
import com.orumjek.entity.VisitedPage;
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
public class VisitedPageJpaController implements Serializable {

    public VisitedPageJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VisitedPage visitedPage) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Page pageId = visitedPage.getPageId();
            if (pageId != null) {
                pageId = em.getReference(pageId.getClass(), pageId.getId());
                visitedPage.setPageId(pageId);
            }
            Visit visitId = visitedPage.getVisitId();
            if (visitId != null) {
                visitId = em.getReference(visitId.getClass(), visitId.getId());
                visitedPage.setVisitId(visitId);
            }
            em.persist(visitedPage);
            if (pageId != null) {
                pageId.getVisitedPageCollection().add(visitedPage);
                pageId = em.merge(pageId);
            }
            if (visitId != null) {
                visitId.getVisitedPageCollection().add(visitedPage);
                visitId = em.merge(visitId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVisitedPage(visitedPage.getId()) != null) {
                throw new PreexistingEntityException("VisitedPage " + visitedPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VisitedPage visitedPage) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VisitedPage persistentVisitedPage = em.find(VisitedPage.class, visitedPage.getId());
            Page pageIdOld = persistentVisitedPage.getPageId();
            Page pageIdNew = visitedPage.getPageId();
            Visit visitIdOld = persistentVisitedPage.getVisitId();
            Visit visitIdNew = visitedPage.getVisitId();
            if (pageIdNew != null) {
                pageIdNew = em.getReference(pageIdNew.getClass(), pageIdNew.getId());
                visitedPage.setPageId(pageIdNew);
            }
            if (visitIdNew != null) {
                visitIdNew = em.getReference(visitIdNew.getClass(), visitIdNew.getId());
                visitedPage.setVisitId(visitIdNew);
            }
            visitedPage = em.merge(visitedPage);
            if (pageIdOld != null && !pageIdOld.equals(pageIdNew)) {
                pageIdOld.getVisitedPageCollection().remove(visitedPage);
                pageIdOld = em.merge(pageIdOld);
            }
            if (pageIdNew != null && !pageIdNew.equals(pageIdOld)) {
                pageIdNew.getVisitedPageCollection().add(visitedPage);
                pageIdNew = em.merge(pageIdNew);
            }
            if (visitIdOld != null && !visitIdOld.equals(visitIdNew)) {
                visitIdOld.getVisitedPageCollection().remove(visitedPage);
                visitIdOld = em.merge(visitIdOld);
            }
            if (visitIdNew != null && !visitIdNew.equals(visitIdOld)) {
                visitIdNew.getVisitedPageCollection().add(visitedPage);
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
                Long id = visitedPage.getId();
                if (findVisitedPage(id) == null) {
                    throw new NonexistentEntityException("The visitedPage with id " + id + " no longer exists.");
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
            VisitedPage visitedPage;
            try {
                visitedPage = em.getReference(VisitedPage.class, id);
                visitedPage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The visitedPage with id " + id + " no longer exists.", enfe);
            }
            Page pageId = visitedPage.getPageId();
            if (pageId != null) {
                pageId.getVisitedPageCollection().remove(visitedPage);
                pageId = em.merge(pageId);
            }
            Visit visitId = visitedPage.getVisitId();
            if (visitId != null) {
                visitId.getVisitedPageCollection().remove(visitedPage);
                visitId = em.merge(visitId);
            }
            em.remove(visitedPage);
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

    public List<VisitedPage> findVisitedPageEntities() {
        return findVisitedPageEntities(true, -1, -1);
    }

    public List<VisitedPage> findVisitedPageEntities(int maxResults, int firstResult) {
        return findVisitedPageEntities(false, maxResults, firstResult);
    }

    private List<VisitedPage> findVisitedPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VisitedPage.class));
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

    public VisitedPage findVisitedPage(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VisitedPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getVisitedPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VisitedPage> rt = cq.from(VisitedPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
