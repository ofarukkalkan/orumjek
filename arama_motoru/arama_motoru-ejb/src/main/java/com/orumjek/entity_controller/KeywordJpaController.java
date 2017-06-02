/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.entity_controller;

import com.orumjek.entity.Keyword;
import com.orumjek.entity.KeywordIndex;
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
public class KeywordJpaController implements Serializable {

    public KeywordJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Keyword keyword) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (keyword.getKeywordIndexCollection() == null) {
            keyword.setKeywordIndexCollection(new ArrayList<KeywordIndex>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<KeywordIndex> attachedKeywordIndexCollection = new ArrayList<KeywordIndex>();
            for (KeywordIndex keywordIndexCollectionKeywordIndexToAttach : keyword.getKeywordIndexCollection()) {
                keywordIndexCollectionKeywordIndexToAttach = em.getReference(keywordIndexCollectionKeywordIndexToAttach.getClass(), keywordIndexCollectionKeywordIndexToAttach.getKeywordIndexPK());
                attachedKeywordIndexCollection.add(keywordIndexCollectionKeywordIndexToAttach);
            }
            keyword.setKeywordIndexCollection(attachedKeywordIndexCollection);
            em.persist(keyword);
            for (KeywordIndex keywordIndexCollectionKeywordIndex : keyword.getKeywordIndexCollection()) {
                Keyword oldKeywordOfKeywordIndexCollectionKeywordIndex = keywordIndexCollectionKeywordIndex.getKeyword();
                keywordIndexCollectionKeywordIndex.setKeyword(keyword);
                keywordIndexCollectionKeywordIndex = em.merge(keywordIndexCollectionKeywordIndex);
                if (oldKeywordOfKeywordIndexCollectionKeywordIndex != null) {
                    oldKeywordOfKeywordIndexCollectionKeywordIndex.getKeywordIndexCollection().remove(keywordIndexCollectionKeywordIndex);
                    oldKeywordOfKeywordIndexCollectionKeywordIndex = em.merge(oldKeywordOfKeywordIndexCollectionKeywordIndex);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKeyword(keyword.getId()) != null) {
                throw new PreexistingEntityException("Keyword " + keyword + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Keyword keyword) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Keyword persistentKeyword = em.find(Keyword.class, keyword.getId());
            Collection<KeywordIndex> keywordIndexCollectionOld = persistentKeyword.getKeywordIndexCollection();
            Collection<KeywordIndex> keywordIndexCollectionNew = keyword.getKeywordIndexCollection();
            List<String> illegalOrphanMessages = null;
            for (KeywordIndex keywordIndexCollectionOldKeywordIndex : keywordIndexCollectionOld) {
                if (!keywordIndexCollectionNew.contains(keywordIndexCollectionOldKeywordIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain KeywordIndex " + keywordIndexCollectionOldKeywordIndex + " since its keyword field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<KeywordIndex> attachedKeywordIndexCollectionNew = new ArrayList<KeywordIndex>();
            for (KeywordIndex keywordIndexCollectionNewKeywordIndexToAttach : keywordIndexCollectionNew) {
                keywordIndexCollectionNewKeywordIndexToAttach = em.getReference(keywordIndexCollectionNewKeywordIndexToAttach.getClass(), keywordIndexCollectionNewKeywordIndexToAttach.getKeywordIndexPK());
                attachedKeywordIndexCollectionNew.add(keywordIndexCollectionNewKeywordIndexToAttach);
            }
            keywordIndexCollectionNew = attachedKeywordIndexCollectionNew;
            keyword.setKeywordIndexCollection(keywordIndexCollectionNew);
            keyword = em.merge(keyword);
            for (KeywordIndex keywordIndexCollectionNewKeywordIndex : keywordIndexCollectionNew) {
                if (!keywordIndexCollectionOld.contains(keywordIndexCollectionNewKeywordIndex)) {
                    Keyword oldKeywordOfKeywordIndexCollectionNewKeywordIndex = keywordIndexCollectionNewKeywordIndex.getKeyword();
                    keywordIndexCollectionNewKeywordIndex.setKeyword(keyword);
                    keywordIndexCollectionNewKeywordIndex = em.merge(keywordIndexCollectionNewKeywordIndex);
                    if (oldKeywordOfKeywordIndexCollectionNewKeywordIndex != null && !oldKeywordOfKeywordIndexCollectionNewKeywordIndex.equals(keyword)) {
                        oldKeywordOfKeywordIndexCollectionNewKeywordIndex.getKeywordIndexCollection().remove(keywordIndexCollectionNewKeywordIndex);
                        oldKeywordOfKeywordIndexCollectionNewKeywordIndex = em.merge(oldKeywordOfKeywordIndexCollectionNewKeywordIndex);
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
                String id = keyword.getId();
                if (findKeyword(id) == null) {
                    throw new NonexistentEntityException("The keyword with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Keyword keyword;
            try {
                keyword = em.getReference(Keyword.class, id);
                keyword.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keyword with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<KeywordIndex> keywordIndexCollectionOrphanCheck = keyword.getKeywordIndexCollection();
            for (KeywordIndex keywordIndexCollectionOrphanCheckKeywordIndex : keywordIndexCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Keyword (" + keyword + ") cannot be destroyed since the KeywordIndex " + keywordIndexCollectionOrphanCheckKeywordIndex + " in its keywordIndexCollection field has a non-nullable keyword field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(keyword);
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

    public List<Keyword> findKeywordEntities() {
        return findKeywordEntities(true, -1, -1);
    }

    public List<Keyword> findKeywordEntities(int maxResults, int firstResult) {
        return findKeywordEntities(false, maxResults, firstResult);
    }

    private List<Keyword> findKeywordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Keyword.class));
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

    public Keyword findKeyword(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Keyword.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeywordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Keyword> rt = cq.from(Keyword.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
