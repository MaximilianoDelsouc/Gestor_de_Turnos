/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.clases.TipoConsulta;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author TT
 */
public class TipoConsultaJpaController implements Serializable {

    public TipoConsultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoConsulta tipoConsulta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoConsulta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoConsulta tipoConsulta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoConsulta = em.merge(tipoConsulta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = tipoConsulta.getIdTipoConsulta();
                if (findTipoConsulta(id) == null) {
                    throw new NonexistentEntityException("The tipoConsulta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoConsulta tipoConsulta;
            try {
                tipoConsulta = em.getReference(TipoConsulta.class, id);
                tipoConsulta.getIdTipoConsulta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoConsulta with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoConsulta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoConsulta> findTipoConsultaEntities() {
        return findTipoConsultaEntities(true, -1, -1);
    }

    public List<TipoConsulta> findTipoConsultaEntities(int maxResults, int firstResult) {
        return findTipoConsultaEntities(false, maxResults, firstResult);
    }

    private List<TipoConsulta> findTipoConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoConsulta.class));
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

    public TipoConsulta findTipoConsulta(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoConsulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoConsulta> rt = cq.from(TipoConsulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
