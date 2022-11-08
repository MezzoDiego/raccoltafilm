package it.prova.raccoltafilm.dao;

import java.util.List;
import java.util.Optional;

import it.prova.raccoltafilm.model.Regista;

public interface RegistaDAO extends IBaseDAO<Regista> {
	public List<Regista> findByExample(Regista example) throws Exception;
	public Optional<Regista> findEagerFilms(Long id) throws Exception;
}
