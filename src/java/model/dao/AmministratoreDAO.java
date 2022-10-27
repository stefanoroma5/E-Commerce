/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Amministratore;

/**
 *
 * @author Stefano
 */
public interface AmministratoreDAO {
    
    public void inserisci(
          String nome,
          String cognome,
          String email,
          String password) throws DuplicatedObjectException;
    
    public void aggiorna(Amministratore admin) throws DuplicatedObjectException;
    
    public void elimina(Amministratore admin);
    
    public void sblocca(Amministratore admin);
    
    public Amministratore cercaPerEmail(String email);
    
    public Amministratore cercaPerIdAdmin(Long idAdmin);
    
    public List<Amministratore> trovaPerIniziale(String iniziale);
    
    public List<String> trovaIniziali();
}
