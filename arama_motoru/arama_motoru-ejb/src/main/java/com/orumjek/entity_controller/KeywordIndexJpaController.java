/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.Keyword;
import com.orumjek.entity.KeywordIndex;
import com.orumjek.entity.KeywordIndexPK;
import com.orumjek.entity.Page;
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
public class KeywordIndexJpaController implements Serializable {

    public KeywordIndexJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KeywordIndex keywordIndex) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (keywordIndex.getKeywordIndexPK() == null) {
            keywordIndex.setKeywordIndexPK(new KeywordIndexPK());
        }
        keywordIndex.getKeywordIndexPK().setPageId(keywordIndex.getPage().getId());
        keywordIndex.getKeywordIndexPK().setKeywordId(keywordIndex.getKeyword().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Keyword keyword = keywordIndex.getKeyword();
            if (keyword != null) {
                keyword = em.getReference(keyword.getClass(), keyword.getId());
                keywordIndex.setKeyword(keyword);
            }
            Page page = keywordIndex.getPage();
            if (page != null) {
                page = em.getReference(page.getClass(), page.getId());
                keywordIndex.setPage(page);
            }
            em.persist(keywordIndex);
            if (keyword != null) {
                keyword.getKeywordIndexCollection().add(keywordIndex);
                keyword = em.merge(keyword);
            }
            if (page != null) {
                page.getKeywordIndexCollection().add(keywordIndex);
                page = em.merge(page);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKeywordIndex(keywordIndex.getKeywordIndexPK()) != null) {
                throw new PreexistingEntityException("KeywordIndex " + keywordIndex + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KeywordIndex keywordIndex) throws NonexistentEntityException, RollbackFailureException, Exception {
        keywordIndex.getKeywordIndexPK().setPageId(keywordIndex.getPage().getId());
        keywordIndex.getKeywordIndexPK().setKeywordId(keywordIndex.getKeyword().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            KeywordIndex persistentKeywordIndex = em.find(KeywordIndex.class, keywordIndex.getKeywordIndexPK());
            Keyword keywordOld = persistentKeywordIndex.getKeyword();
            Keyword keywordNew = keywordIndex.getKeyword();
            Page pageOld = persistentKeywordIndex.getPage();
            Page pageNew = keywordIndex.getPage();
            if (keywordNew != null) {
                keywordNew = em.getReference(keywordNew.getClass(), keywordNew.getId());
                keywordIndex.setKeyword(keywordNew);
            }
            if (pageNew != null) {
                pageNew = em.getReference(pageNew.getClass(), pageNew.getId());
                keywordIndex.setPage(pageNew);
            }
            keywordIndex = em.merge(keywordIndex);
            if (keywordOld != null && !keywordOld.equals(keywordNew)) {
                keywordOld.getKeywordIndexCollection().remove(keywordIndex);
                keywordOld = em.merge(keywordOld);
            }
            if (keywordNew != null && !keywordNew.equals(keywordOld)) {
                keywordNew.getKeywordIndexCollection().add(keywordIndex);
                keywordNew = em.merge(keywordNew);
            }
            if (pageOld != null && !pageOld.equals(pageNew)) {
                pageOld.getKeywordIndexCollection().remove(keywordIndex);
                pageOld = em.merge(pageOld);
            }
            if (pageNew != null && !pageNew.equals(pageOld)) {
                pageNew.getKeywordIndexCollection().add(keywordIndex);
                pageNew = em.merge(pageNew);
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
                KeywordIndexPK id = keywordIndex.getKeywordIndexPK();
                if (findKeywordIndex(id) == null) {
                    throw new NonexistentEntityException("The keywordIndex with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(KeywordIndexPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            KeywordIndex keywordIndex;
            try {
                keywordIndex = em.getReference(KeywordIndex.class, id);
                keywordIndex.getKeywordIndexPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keywordIndex with id " + id + " no longer exists.", enfe);
            }
            Keyword keyword = keywordIndex.getKeyword();
            if (keyword != null) {
                keyword.getKeywordIndexCollection().remove(keywordIndex);
                keyword = em.merge(keyword);
            }
            Page page = keywordIndex.getPage();
            if (page != null) {
                page.getKeywordIndexCollection().remove(keywordIndex);
                page = em.merge(page);
            }
            em.remove(keywordIndex);
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

    public List<KeywordIndex> findKeywordIndexEntities() {
        return findKeywordIndexEntities(true, -1, -1);
    }

    public List<KeywordIndex> findKeywordIndexEntities(int maxResults, int firstResult) {
        return findKeywordIndexEntities(false, maxResults, firstResult);
    }

    private List<KeywordIndex> findKeywordIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KeywordIndex.class));
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

    public KeywordIndex findKeywordIndex(KeywordIndexPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KeywordIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeywordIndexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KeywordIndex> rt = cq.from(KeywordIndex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
