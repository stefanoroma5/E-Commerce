package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;

public interface UtenteDAO {

  public Utente inserisci(
          String nome,
          String cognome,
          String email,
          String password) throws DuplicatedObjectException;

  public void aggiorna(Utente utente) throws DuplicatedObjectException;

  public void elimina(Utente utente);
  
  public void sblocca(Utente utente);
  
  public Utente cercaPerIdUtente(Long idUtente);
  
  public Utente cercaPerEmail(String email);
  
  public List<String> trovaIniziali();
  
  public List<Utente> trovaPerIniziale(String iniziale);

}