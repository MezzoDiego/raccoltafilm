package it.prova.raccoltafilm.service;

import java.util.List;

import javax.persistence.EntityManager;

import it.prova.raccoltafilm.dao.RegistaDAO;
import it.prova.raccoltafilm.exceptions.ElementNotFoundException;
import it.prova.raccoltafilm.exceptions.RegistaConFilmAssociatiException;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.web.listener.LocalEntityManagerFactoryListener;

public class RegistaServiceImpl implements RegistaService {

	private RegistaDAO registaDAO;

	@Override
	public void setRegistaDAO(RegistaDAO registaDAO) {
		this.registaDAO = registaDAO;
	}

	@Override
	public List<Regista> listAllElements() throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return registaDAO.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}
	}

	@Override
	public Regista caricaSingoloElemento(Long id) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return registaDAO.findOne(id).get();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}
	}

	@Override
	public Regista caricaSingoloElementoConFilms(Long id) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return registaDAO.findEagerFilms(id).get();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiorna(Regista registaInstance) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			registaDAO.update(registaInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}

	}

	@Override
	public void inserisciNuovo(Regista registaInstance) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			registaDAO.insert(registaInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}
	}

	@Override
	public void rimuovi(Long idRegista) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);
			Regista registaToRemove = registaDAO.findOne(idRegista).orElse(null);
			if (registaToRemove == null)
				throw new ElementNotFoundException("Film con id: " + idRegista + " non trovato.");
			
			Regista registaEager = registaDAO.findEagerFilms(idRegista).orElse(null);
			if (registaEager.getFilms().size() > 0)
				throw new RegistaConFilmAssociatiException(
						"Impossibile rimuovere regista in quanto ha film associati.");
			// eseguo quello che realmente devo fare
			registaDAO.delete(registaToRemove);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}

	}

	@Override
	public List<Regista> findByExample(Regista example) throws Exception {
		// questo è come una connection
		EntityManager entityManager = LocalEntityManagerFactoryListener.getEntityManager();

		try {
			// uso l'injection per il dao
			registaDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return registaDAO.findByExample(example);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			LocalEntityManagerFactoryListener.closeEntityManager(entityManager);
		}
	}

}
