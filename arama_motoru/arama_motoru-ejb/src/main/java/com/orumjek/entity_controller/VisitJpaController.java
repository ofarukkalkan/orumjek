/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.Page;
import com.orumjek.entity.PlannedPage;
import com.orumjek.entity.Visit;
import com.orumjek.entity.VisitedPage;
import com.orumjek.entity.exceptions.IllegalOrphanException;
import com.orumjek.entity.exceptions.NonexistentEntityException;
import com.orumjek.entity.exceptions.PreexistingEntityException;
import com.orumjek.entity.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author omerfaruk
 */
public class VisitJpaController implements Serializable {

    public VisitJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Visit visit) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (visit.getPlannedPageCollection() == null) {
            visit.setPlannedPageCollection(new ArrayList<PlannedPage>());
        }
        if (visit.getVisitedPageCollection() == null) {
            visit.setVisitedPageCollection(new ArrayList<VisitedPage>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Page startPage = visit.getStartPage();
            if (startPage != null) {
                startPage = em.getReference(startPage.getClass(), startPage.getId());
                visit.setStartPage(startPage);
            }
            Collection<PlannedPage> attachedPlannedPageCollection = new ArrayList<PlannedPage>();
            for (PlannedPage plannedPageCollectionPlannedPageToAttach : visit.getPlannedPageCollection()) {
                plannedPageCollectionPlannedPageToAttach = em.getReference(plannedPageCollectionPlannedPageToAttach.getClass(), plannedPageCollectionPlannedPageToAttach.getId());
                attachedPlannedPageCollection.add(plannedPageCollectionPlannedPageToAttach);
            }
            visit.setPlannedPageCollection(attachedPlannedPageCollection);
            Collection<VisitedPage> attachedVisitedPageCollection = new ArrayList<VisitedPage>();
            for (VisitedPage visitedPageCollectionVisitedPageToAttach : visit.getVisitedPageCollection()) {
                visitedPageCollectionVisitedPageToAttach = em.getReference(visitedPageCollectionVisitedPageToAttach.getClass(), visitedPageCollectionVisitedPageToAttach.getId());
                attachedVisitedPageCollection.add(visitedPageCollectionVisitedPageToAttach);
            }
            visit.setVisitedPageCollection(attachedVisitedPageCollection);
            em.persist(visit);
            if (startPage != null) {
                startPage.getVisitCollection().add(visit);
                startPage = em.merge(startPage);
            }
            for (PlannedPage plannedPageCollectionPlannedPage : visit.getPlannedPageCollection()) {
                Visit oldVisitIdOfPlannedPageCollectionPlannedPage = plannedPageCollectionPlannedPage.getVisitId();
                plannedPageCollectionPlannedPage.setVisitId(visit);
                plannedPageCollectionPlannedPage = em.merge(plannedPageCollectionPlannedPage);
                if (oldVisitIdOfPlannedPageCollectionPlannedPage != null) {
                    oldVisitIdOfPlannedPageCollectionPlannedPage.getPlannedPageCollection().remove(plannedPageCollectionPlannedPage);
                    oldVisitIdOfPlannedPageCollectionPlannedPage = em.merge(oldVisitIdOfPlannedPageCollectionPlannedPage);
                }
            }
            for (VisitedPage visitedPageCollectionVisitedPage : visit.getVisitedPageCollection()) {
                Visit oldVisitIdOfVisitedPageCollectionVisitedPage = visitedPageCollectionVisitedPage.getVisitId();
                visitedPageCollectionVisitedPage.setVisitId(visit);
                visitedPageCollectionVisitedPage = em.merge(visitedPageCollectionVisitedPage);
                if (oldVisitIdOfVisitedPageCollectionVisitedPage != null) {
                    oldVisitIdOfVisitedPageCollectionVisitedPage.getVisitedPageCollection().remove(visitedPageCollectionVisitedPage);
                    oldVisitIdOfVisitedPageCollectionVisitedPage = em.merge(oldVisitIdOfVisitedPageCollectionVisitedPage);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVisit(visit.getId()) != null) {
                throw new PreexistingEntityException("Visit " + visit + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Visit visit) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Visit persistentVisit = em.find(Visit.class, visit.getId());
            Page startPageOld = persistentVisit.getStartPage();
            Page startPageNew = visit.getStartPage();
            Collection<PlannedPage> plannedPageCollectionOld = persistentVisit.getPlannedPageCollection();
            Collection<PlannedPage> plannedPageCollectionNew = visit.getPlannedPageCollection();
            Collection<VisitedPage> visitedPageCollectionOld = persistentVisit.getVisitedPageCollection();
            Collection<VisitedPage> visitedPageCollectionNew = visit.getVisitedPageCollection();
            List<String> illegalOrphanMessages = null;
            for (PlannedPage plannedPageCollectionOldPlannedPage : plannedPageCollectionOld) {
                if (!plannedPageCollectionNew.contains(plannedPageCollectionOldPlannedPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PlannedPage " + plannedPageCollectionOldPlannedPage + " since its visitId field is not nullable.");
                }
            }
            for (VisitedPage visitedPageCollectionOldVisitedPage : visitedPageCollectionOld) {
                if (!visitedPageCollectionNew.contains(visitedPageCollectionOldVisitedPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VisitedPage " + visitedPageCollectionOldVisitedPage + " since its visitId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (startPageNew != null) {
                startPageNew = em.getReference(startPageNew.getClass(), startPageNew.getId());
                visit.setStartPage(startPageNew);
            }
            Collection<PlannedPage> attachedPlannedPageCollectionNew = new ArrayList<PlannedPage>();
            for (PlannedPage plannedPageCollectionNewPlannedPageToAttach : plannedPageCollectionNew) {
                plannedPageCollectionNewPlannedPageToAttach = em.getReference(plannedPageCollectionNewPlannedPageToAttach.getClass(), plannedPageCollectionNewPlannedPageToAttach.getId());
                attachedPlannedPageCollectionNew.add(plannedPageCollectionNewPlannedPageToAttach);
            }
            plannedPageCollectionNew = attachedPlannedPageCollectionNew;
            visit.setPlannedPageCollection(plannedPageCollectionNew);
            Collection<VisitedPage> attachedVisitedPageCollectionNew = new ArrayList<VisitedPage>();
            for (VisitedPage visitedPageCollectionNewVisitedPageToAttach : visitedPageCollectionNew) {
                visitedPageCollectionNewVisitedPageToAttach = em.getReference(visitedPageCollectionNewVisitedPageToAttach.getClass(), visitedPageCollectionNewVisitedPageToAttach.getId());
                attachedVisitedPageCollectionNew.add(visitedPageCollectionNewVisitedPageToAttach);
            }
            visitedPageCollectionNew = attachedVisitedPageCollectionNew;
            visit.setVisitedPageCollection(visitedPageCollectionNew);
            visit = em.merge(visit);
            if (startPageOld != null && !startPageOld.equals(startPageNew)) {
                startPageOld.getVisitCollection().remove(visit);
                startPageOld = em.merge(startPageOld);
            }
            if (startPageNew != null && !startPageNew.equals(startPageOld)) {
                startPageNew.getVisitCollection().add(visit);
                startPageNew = em.merge(startPageNew);
            }
            for (PlannedPage plannedPageCollectionNewPlannedPage : plannedPageCollectionNew) {
                if (!plannedPageCollectionOld.contains(plannedPageCollectionNewPlannedPage)) {
                    Visit oldVisitIdOfPlannedPageCollectionNewPlannedPage = plannedPageCollectionNewPlannedPage.getVisitId();
                    plannedPageCollectionNewPlannedPage.setVisitId(visit);
                    plannedPageCollectionNewPlannedPage = em.merge(plannedPageCollectionNewPlannedPage);
                    if (oldVisitIdOfPlannedPageCollectionNewPlannedPage != null && !oldVisitIdOfPlannedPageCollectionNewPlannedPage.equals(visit)) {
                        oldVisitIdOfPlannedPageCollectionNewPlannedPage.getPlannedPageCollection().remove(plannedPageCollectionNewPlannedPage);
                        oldVisitIdOfPlannedPageCollectionNewPlannedPage = em.merge(oldVisitIdOfPlannedPageCollectionNewPlannedPage);
                    }
                }
            }
            for (VisitedPage visitedPageCollectionNewVisitedPage : visitedPageCollectionNew) {
                if (!visitedPageCollectionOld.contains(visitedPageCollectionNewVisitedPage)) {
                    Visit oldVisitIdOfVisitedPageCollectionNewVisitedPage = visitedPageCollectionNewVisitedPage.getVisitId();
                    visitedPageCollectionNewVisitedPage.setVisitId(visit);
                    visitedPageCollectionNewVisitedPage = em.merge(visitedPageCollectionNewVisitedPage);
                    if (oldVisitIdOfVisitedPageCollectionNewVisitedPage != null && !oldVisitIdOfVisitedPageCollectionNewVisitedPage.equals(visit)) {
                        oldVisitIdOfVisitedPageCollectionNewVisitedPage.getVisitedPageCollection().remove(visitedPageCollectionNewVisitedPage);
                        oldVisitIdOfVisitedPageCollectionNewVisitedPage = em.merge(oldVisitIdOfVisitedPageCollectionNewVisitedPage);
                    }
                }
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
                Long id = visit.getId();
                if (findVisit(id) == null) {
                    throw new NonexistentEntityException("The visit with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Visit visit;
            try {
                visit = em.getReference(Visit.class, id);
                visit.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The visit with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PlannedPage> plannedPageCollectionOrphanCheck = visit.getPlannedPageCollection();
            for (PlannedPage plannedPageCollectionOrphanCheckPlannedPage : plannedPageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Visit (" + visit + ") cannot be destroyed since the PlannedPage " + plannedPageCollectionOrphanCheckPlannedPage + " in its plannedPageCollection field has a non-nullable visitId field.");
            }
            Collection<VisitedPage> visitedPageCollectionOrphanCheck = visit.getVisitedPageCollection();
            for (VisitedPage visitedPageCollectionOrphanCheckVisitedPage : visitedPageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Visit (" + visit + ") cannot be destroyed since the VisitedPage " + visitedPageCollectionOrphanCheckVisitedPage + " in its visitedPageCollection field has a non-nullable visitId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Page startPage = visit.getStartPage();
            if (startPage != null) {
                startPage.getVisitCollection().remove(visit);
                startPage = em.merge(startPage);
            }
            em.remove(visit);
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

    public List<Visit> findVisitEntities() {
        return findVisitEntities(true, -1, -1);
    }

    public List<Visit> findVisitEntities(int maxResults, int firstResult) {
        return findVisitEntities(false, maxResults, firstResult);
    }

    private List<Visit> findVisitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Visit.class));
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

    public Visit findVisit(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Visit.class, id);
        } finally {
            em.close();
        }
    }

    public int getVisitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Visit> rt = cq.from(Visit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
