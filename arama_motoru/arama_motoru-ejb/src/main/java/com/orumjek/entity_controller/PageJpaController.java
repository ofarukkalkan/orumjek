/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.KeywordIndex;
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
public class PageJpaController implements Serializable {

    public PageJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Page page) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (page.getPageCollection() == null) {
            page.setPageCollection(new ArrayList<Page>());
        }
        if (page.getPageCollection1() == null) {
            page.setPageCollection1(new ArrayList<Page>());
        }
        if (page.getKeywordIndexCollection() == null) {
            page.setKeywordIndexCollection(new ArrayList<KeywordIndex>());
        }
        if (page.getPlannedPageCollection() == null) {
            page.setPlannedPageCollection(new ArrayList<PlannedPage>());
        }
        if (page.getVisitedPageCollection() == null) {
            page.setVisitedPageCollection(new ArrayList<VisitedPage>());
        }
        if (page.getVisitCollection() == null) {
            page.setVisitCollection(new ArrayList<Visit>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Page> attachedPageCollection = new ArrayList<Page>();
            for (Page pageCollectionPageToAttach : page.getPageCollection()) {
                pageCollectionPageToAttach = em.getReference(pageCollectionPageToAttach.getClass(), pageCollectionPageToAttach.getId());
                attachedPageCollection.add(pageCollectionPageToAttach);
            }
            page.setPageCollection(attachedPageCollection);
            Collection<Page> attachedPageCollection1 = new ArrayList<Page>();
            for (Page pageCollection1PageToAttach : page.getPageCollection1()) {
                pageCollection1PageToAttach = em.getReference(pageCollection1PageToAttach.getClass(), pageCollection1PageToAttach.getId());
                attachedPageCollection1.add(pageCollection1PageToAttach);
            }
            page.setPageCollection1(attachedPageCollection1);
            Collection<KeywordIndex> attachedKeywordIndexCollection = new ArrayList<KeywordIndex>();
            for (KeywordIndex keywordIndexCollectionKeywordIndexToAttach : page.getKeywordIndexCollection()) {
                keywordIndexCollectionKeywordIndexToAttach = em.getReference(keywordIndexCollectionKeywordIndexToAttach.getClass(), keywordIndexCollectionKeywordIndexToAttach.getKeywordIndexPK());
                attachedKeywordIndexCollection.add(keywordIndexCollectionKeywordIndexToAttach);
            }
            page.setKeywordIndexCollection(attachedKeywordIndexCollection);
            Collection<PlannedPage> attachedPlannedPageCollection = new ArrayList<PlannedPage>();
            for (PlannedPage plannedPageCollectionPlannedPageToAttach : page.getPlannedPageCollection()) {
                plannedPageCollectionPlannedPageToAttach = em.getReference(plannedPageCollectionPlannedPageToAttach.getClass(), plannedPageCollectionPlannedPageToAttach.getId());
                attachedPlannedPageCollection.add(plannedPageCollectionPlannedPageToAttach);
            }
            page.setPlannedPageCollection(attachedPlannedPageCollection);
            Collection<VisitedPage> attachedVisitedPageCollection = new ArrayList<VisitedPage>();
            for (VisitedPage visitedPageCollectionVisitedPageToAttach : page.getVisitedPageCollection()) {
                visitedPageCollectionVisitedPageToAttach = em.getReference(visitedPageCollectionVisitedPageToAttach.getClass(), visitedPageCollectionVisitedPageToAttach.getId());
                attachedVisitedPageCollection.add(visitedPageCollectionVisitedPageToAttach);
            }
            page.setVisitedPageCollection(attachedVisitedPageCollection);
            Collection<Visit> attachedVisitCollection = new ArrayList<Visit>();
            for (Visit visitCollectionVisitToAttach : page.getVisitCollection()) {
                visitCollectionVisitToAttach = em.getReference(visitCollectionVisitToAttach.getClass(), visitCollectionVisitToAttach.getId());
                attachedVisitCollection.add(visitCollectionVisitToAttach);
            }
            page.setVisitCollection(attachedVisitCollection);
            em.persist(page);
            for (Page pageCollectionPage : page.getPageCollection()) {
                pageCollectionPage.getPageCollection().add(page);
                pageCollectionPage = em.merge(pageCollectionPage);
            }
            for (Page pageCollection1Page : page.getPageCollection1()) {
                pageCollection1Page.getPageCollection().add(page);
                pageCollection1Page = em.merge(pageCollection1Page);
            }
            for (KeywordIndex keywordIndexCollectionKeywordIndex : page.getKeywordIndexCollection()) {
                Page oldPageOfKeywordIndexCollectionKeywordIndex = keywordIndexCollectionKeywordIndex.getPage();
                keywordIndexCollectionKeywordIndex.setPage(page);
                keywordIndexCollectionKeywordIndex = em.merge(keywordIndexCollectionKeywordIndex);
                if (oldPageOfKeywordIndexCollectionKeywordIndex != null) {
                    oldPageOfKeywordIndexCollectionKeywordIndex.getKeywordIndexCollection().remove(keywordIndexCollectionKeywordIndex);
                    oldPageOfKeywordIndexCollectionKeywordIndex = em.merge(oldPageOfKeywordIndexCollectionKeywordIndex);
                }
            }
            for (PlannedPage plannedPageCollectionPlannedPage : page.getPlannedPageCollection()) {
                Page oldPageIdOfPlannedPageCollectionPlannedPage = plannedPageCollectionPlannedPage.getPageId();
                plannedPageCollectionPlannedPage.setPageId(page);
                plannedPageCollectionPlannedPage = em.merge(plannedPageCollectionPlannedPage);
                if (oldPageIdOfPlannedPageCollectionPlannedPage != null) {
                    oldPageIdOfPlannedPageCollectionPlannedPage.getPlannedPageCollection().remove(plannedPageCollectionPlannedPage);
                    oldPageIdOfPlannedPageCollectionPlannedPage = em.merge(oldPageIdOfPlannedPageCollectionPlannedPage);
                }
            }
            for (VisitedPage visitedPageCollectionVisitedPage : page.getVisitedPageCollection()) {
                Page oldPageIdOfVisitedPageCollectionVisitedPage = visitedPageCollectionVisitedPage.getPageId();
                visitedPageCollectionVisitedPage.setPageId(page);
                visitedPageCollectionVisitedPage = em.merge(visitedPageCollectionVisitedPage);
                if (oldPageIdOfVisitedPageCollectionVisitedPage != null) {
                    oldPageIdOfVisitedPageCollectionVisitedPage.getVisitedPageCollection().remove(visitedPageCollectionVisitedPage);
                    oldPageIdOfVisitedPageCollectionVisitedPage = em.merge(oldPageIdOfVisitedPageCollectionVisitedPage);
                }
            }
            for (Visit visitCollectionVisit : page.getVisitCollection()) {
                Page oldStartPageOfVisitCollectionVisit = visitCollectionVisit.getStartPage();
                visitCollectionVisit.setStartPage(page);
                visitCollectionVisit = em.merge(visitCollectionVisit);
                if (oldStartPageOfVisitCollectionVisit != null) {
                    oldStartPageOfVisitCollectionVisit.getVisitCollection().remove(visitCollectionVisit);
                    oldStartPageOfVisitCollectionVisit = em.merge(oldStartPageOfVisitCollectionVisit);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPage(page.getId()) != null) {
                throw new PreexistingEntityException("Page " + page + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Page page) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Page persistentPage = em.find(Page.class, page.getId());
            Collection<Page> pageCollectionOld = persistentPage.getPageCollection();
            Collection<Page> pageCollectionNew = page.getPageCollection();
            Collection<Page> pageCollection1Old = persistentPage.getPageCollection1();
            Collection<Page> pageCollection1New = page.getPageCollection1();
            Collection<KeywordIndex> keywordIndexCollectionOld = persistentPage.getKeywordIndexCollection();
            Collection<KeywordIndex> keywordIndexCollectionNew = page.getKeywordIndexCollection();
            Collection<PlannedPage> plannedPageCollectionOld = persistentPage.getPlannedPageCollection();
            Collection<PlannedPage> plannedPageCollectionNew = page.getPlannedPageCollection();
            Collection<VisitedPage> visitedPageCollectionOld = persistentPage.getVisitedPageCollection();
            Collection<VisitedPage> visitedPageCollectionNew = page.getVisitedPageCollection();
            Collection<Visit> visitCollectionOld = persistentPage.getVisitCollection();
            Collection<Visit> visitCollectionNew = page.getVisitCollection();
            List<String> illegalOrphanMessages = null;
            for (KeywordIndex keywordIndexCollectionOldKeywordIndex : keywordIndexCollectionOld) {
                if (!keywordIndexCollectionNew.contains(keywordIndexCollectionOldKeywordIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain KeywordIndex " + keywordIndexCollectionOldKeywordIndex + " since its page field is not nullable.");
                }
            }
            for (PlannedPage plannedPageCollectionOldPlannedPage : plannedPageCollectionOld) {
                if (!plannedPageCollectionNew.contains(plannedPageCollectionOldPlannedPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PlannedPage " + plannedPageCollectionOldPlannedPage + " since its pageId field is not nullable.");
                }
            }
            for (VisitedPage visitedPageCollectionOldVisitedPage : visitedPageCollectionOld) {
                if (!visitedPageCollectionNew.contains(visitedPageCollectionOldVisitedPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VisitedPage " + visitedPageCollectionOldVisitedPage + " since its pageId field is not nullable.");
                }
            }
            for (Visit visitCollectionOldVisit : visitCollectionOld) {
                if (!visitCollectionNew.contains(visitCollectionOldVisit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Visit " + visitCollectionOldVisit + " since its startPage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Page> attachedPageCollectionNew = new ArrayList<Page>();
            for (Page pageCollectionNewPageToAttach : pageCollectionNew) {
                pageCollectionNewPageToAttach = em.getReference(pageCollectionNewPageToAttach.getClass(), pageCollectionNewPageToAttach.getId());
                attachedPageCollectionNew.add(pageCollectionNewPageToAttach);
            }
            pageCollectionNew = attachedPageCollectionNew;
            page.setPageCollection(pageCollectionNew);
            Collection<Page> attachedPageCollection1New = new ArrayList<Page>();
            for (Page pageCollection1NewPageToAttach : pageCollection1New) {
                pageCollection1NewPageToAttach = em.getReference(pageCollection1NewPageToAttach.getClass(), pageCollection1NewPageToAttach.getId());
                attachedPageCollection1New.add(pageCollection1NewPageToAttach);
            }
            pageCollection1New = attachedPageCollection1New;
            page.setPageCollection1(pageCollection1New);
            Collection<KeywordIndex> attachedKeywordIndexCollectionNew = new ArrayList<KeywordIndex>();
            for (KeywordIndex keywordIndexCollectionNewKeywordIndexToAttach : keywordIndexCollectionNew) {
                keywordIndexCollectionNewKeywordIndexToAttach = em.getReference(keywordIndexCollectionNewKeywordIndexToAttach.getClass(), keywordIndexCollectionNewKeywordIndexToAttach.getKeywordIndexPK());
                attachedKeywordIndexCollectionNew.add(keywordIndexCollectionNewKeywordIndexToAttach);
            }
            keywordIndexCollectionNew = attachedKeywordIndexCollectionNew;
            page.setKeywordIndexCollection(keywordIndexCollectionNew);
            Collection<PlannedPage> attachedPlannedPageCollectionNew = new ArrayList<PlannedPage>();
            for (PlannedPage plannedPageCollectionNewPlannedPageToAttach : plannedPageCollectionNew) {
                plannedPageCollectionNewPlannedPageToAttach = em.getReference(plannedPageCollectionNewPlannedPageToAttach.getClass(), plannedPageCollectionNewPlannedPageToAttach.getId());
                attachedPlannedPageCollectionNew.add(plannedPageCollectionNewPlannedPageToAttach);
            }
            plannedPageCollectionNew = attachedPlannedPageCollectionNew;
            page.setPlannedPageCollection(plannedPageCollectionNew);
            Collection<VisitedPage> attachedVisitedPageCollectionNew = new ArrayList<VisitedPage>();
            for (VisitedPage visitedPageCollectionNewVisitedPageToAttach : visitedPageCollectionNew) {
                visitedPageCollectionNewVisitedPageToAttach = em.getReference(visitedPageCollectionNewVisitedPageToAttach.getClass(), visitedPageCollectionNewVisitedPageToAttach.getId());
                attachedVisitedPageCollectionNew.add(visitedPageCollectionNewVisitedPageToAttach);
            }
            visitedPageCollectionNew = attachedVisitedPageCollectionNew;
            page.setVisitedPageCollection(visitedPageCollectionNew);
            Collection<Visit> attachedVisitCollectionNew = new ArrayList<Visit>();
            for (Visit visitCollectionNewVisitToAttach : visitCollectionNew) {
                visitCollectionNewVisitToAttach = em.getReference(visitCollectionNewVisitToAttach.getClass(), visitCollectionNewVisitToAttach.getId());
                attachedVisitCollectionNew.add(visitCollectionNewVisitToAttach);
            }
            visitCollectionNew = attachedVisitCollectionNew;
            page.setVisitCollection(visitCollectionNew);
            page = em.merge(page);
            for (Page pageCollectionOldPage : pageCollectionOld) {
                if (!pageCollectionNew.contains(pageCollectionOldPage)) {
                    pageCollectionOldPage.getPageCollection().remove(page);
                    pageCollectionOldPage = em.merge(pageCollectionOldPage);
                }
            }
            for (Page pageCollectionNewPage : pageCollectionNew) {
                if (!pageCollectionOld.contains(pageCollectionNewPage)) {
                    pageCollectionNewPage.getPageCollection().add(page);
                    pageCollectionNewPage = em.merge(pageCollectionNewPage);
                }
            }
            for (Page pageCollection1OldPage : pageCollection1Old) {
                if (!pageCollection1New.contains(pageCollection1OldPage)) {
                    pageCollection1OldPage.getPageCollection().remove(page);
                    pageCollection1OldPage = em.merge(pageCollection1OldPage);
                }
            }
            for (Page pageCollection1NewPage : pageCollection1New) {
                if (!pageCollection1Old.contains(pageCollection1NewPage)) {
                    pageCollection1NewPage.getPageCollection().add(page);
                    pageCollection1NewPage = em.merge(pageCollection1NewPage);
                }
            }
            for (KeywordIndex keywordIndexCollectionNewKeywordIndex : keywordIndexCollectionNew) {
                if (!keywordIndexCollectionOld.contains(keywordIndexCollectionNewKeywordIndex)) {
                    Page oldPageOfKeywordIndexCollectionNewKeywordIndex = keywordIndexCollectionNewKeywordIndex.getPage();
                    keywordIndexCollectionNewKeywordIndex.setPage(page);
                    keywordIndexCollectionNewKeywordIndex = em.merge(keywordIndexCollectionNewKeywordIndex);
                    if (oldPageOfKeywordIndexCollectionNewKeywordIndex != null && !oldPageOfKeywordIndexCollectionNewKeywordIndex.equals(page)) {
                        oldPageOfKeywordIndexCollectionNewKeywordIndex.getKeywordIndexCollection().remove(keywordIndexCollectionNewKeywordIndex);
                        oldPageOfKeywordIndexCollectionNewKeywordIndex = em.merge(oldPageOfKeywordIndexCollectionNewKeywordIndex);
                    }
                }
            }
            for (PlannedPage plannedPageCollectionNewPlannedPage : plannedPageCollectionNew) {
                if (!plannedPageCollectionOld.contains(plannedPageCollectionNewPlannedPage)) {
                    Page oldPageIdOfPlannedPageCollectionNewPlannedPage = plannedPageCollectionNewPlannedPage.getPageId();
                    plannedPageCollectionNewPlannedPage.setPageId(page);
                    plannedPageCollectionNewPlannedPage = em.merge(plannedPageCollectionNewPlannedPage);
                    if (oldPageIdOfPlannedPageCollectionNewPlannedPage != null && !oldPageIdOfPlannedPageCollectionNewPlannedPage.equals(page)) {
                        oldPageIdOfPlannedPageCollectionNewPlannedPage.getPlannedPageCollection().remove(plannedPageCollectionNewPlannedPage);
                        oldPageIdOfPlannedPageCollectionNewPlannedPage = em.merge(oldPageIdOfPlannedPageCollectionNewPlannedPage);
                    }
                }
            }
            for (VisitedPage visitedPageCollectionNewVisitedPage : visitedPageCollectionNew) {
                if (!visitedPageCollectionOld.contains(visitedPageCollectionNewVisitedPage)) {
                    Page oldPageIdOfVisitedPageCollectionNewVisitedPage = visitedPageCollectionNewVisitedPage.getPageId();
                    visitedPageCollectionNewVisitedPage.setPageId(page);
                    visitedPageCollectionNewVisitedPage = em.merge(visitedPageCollectionNewVisitedPage);
                    if (oldPageIdOfVisitedPageCollectionNewVisitedPage != null && !oldPageIdOfVisitedPageCollectionNewVisitedPage.equals(page)) {
                        oldPageIdOfVisitedPageCollectionNewVisitedPage.getVisitedPageCollection().remove(visitedPageCollectionNewVisitedPage);
                        oldPageIdOfVisitedPageCollectionNewVisitedPage = em.merge(oldPageIdOfVisitedPageCollectionNewVisitedPage);
                    }
                }
            }
            for (Visit visitCollectionNewVisit : visitCollectionNew) {
                if (!visitCollectionOld.contains(visitCollectionNewVisit)) {
                    Page oldStartPageOfVisitCollectionNewVisit = visitCollectionNewVisit.getStartPage();
                    visitCollectionNewVisit.setStartPage(page);
                    visitCollectionNewVisit = em.merge(visitCollectionNewVisit);
                    if (oldStartPageOfVisitCollectionNewVisit != null && !oldStartPageOfVisitCollectionNewVisit.equals(page)) {
                        oldStartPageOfVisitCollectionNewVisit.getVisitCollection().remove(visitCollectionNewVisit);
                        oldStartPageOfVisitCollectionNewVisit = em.merge(oldStartPageOfVisitCollectionNewVisit);
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
                Long id = page.getId();
                if (findPage(id) == null) {
                    throw new NonexistentEntityException("The page with id " + id + " no longer exists.");
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
            Page page;
            try {
                page = em.getReference(Page.class, id);
                page.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The page with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<KeywordIndex> keywordIndexCollectionOrphanCheck = page.getKeywordIndexCollection();
            for (KeywordIndex keywordIndexCollectionOrphanCheckKeywordIndex : keywordIndexCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Page (" + page + ") cannot be destroyed since the KeywordIndex " + keywordIndexCollectionOrphanCheckKeywordIndex + " in its keywordIndexCollection field has a non-nullable page field.");
            }
            Collection<PlannedPage> plannedPageCollectionOrphanCheck = page.getPlannedPageCollection();
            for (PlannedPage plannedPageCollectionOrphanCheckPlannedPage : plannedPageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Page (" + page + ") cannot be destroyed since the PlannedPage " + plannedPageCollectionOrphanCheckPlannedPage + " in its plannedPageCollection field has a non-nullable pageId field.");
            }
            Collection<VisitedPage> visitedPageCollectionOrphanCheck = page.getVisitedPageCollection();
            for (VisitedPage visitedPageCollectionOrphanCheckVisitedPage : visitedPageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Page (" + page + ") cannot be destroyed since the VisitedPage " + visitedPageCollectionOrphanCheckVisitedPage + " in its visitedPageCollection field has a non-nullable pageId field.");
            }
            Collection<Visit> visitCollectionOrphanCheck = page.getVisitCollection();
            for (Visit visitCollectionOrphanCheckVisit : visitCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Page (" + page + ") cannot be destroyed since the Visit " + visitCollectionOrphanCheckVisit + " in its visitCollection field has a non-nullable startPage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Page> pageCollection = page.getPageCollection();
            for (Page pageCollectionPage : pageCollection) {
                pageCollectionPage.getPageCollection().remove(page);
                pageCollectionPage = em.merge(pageCollectionPage);
            }
            Collection<Page> pageCollection1 = page.getPageCollection1();
            for (Page pageCollection1Page : pageCollection1) {
                pageCollection1Page.getPageCollection().remove(page);
                pageCollection1Page = em.merge(pageCollection1Page);
            }
            em.remove(page);
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

    public List<Page> findPageEntities() {
        return findPageEntities(true, -1, -1);
    }

    public List<Page> findPageEntities(int maxResults, int firstResult) {
        return findPageEntities(false, maxResults, firstResult);
    }

    private List<Page> findPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Page.class));
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

    public Page findPage(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Page.class, id);
        } finally {
            em.close();
        }
    }

    public int getPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Page> rt = cq.from(Page.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
