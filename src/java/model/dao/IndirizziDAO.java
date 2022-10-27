/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Indirizzi;
import model.mo.Utente;

/**
 *
 * @author Stefano
 */
public interface IndirizziDAO {
    
    public void inserisci (
            String alias,
            String indirizzo,
            String citta,
            String provincia,
            Long cap,
            String telefono1,
            String telefono2,
            Utente utente) throws DuplicatedObjectException;
    
    public void aggiorna (Indirizzi indirizzi) throws DuplicatedObjectException;
    
    public void elimina (Indirizzi indirizzi);
    
    public Indirizzi trovaIndirizzoSelezionato(Long idUtente);
    
    public List<Indirizzi> trovaIndirizziPerUtente(Long idUtente);
    
    public Indirizzi trovaPerIdIndirizzo(Long idIndirizzo);
    
    public void selezionaIndirizzo(Long idIndirizzo);
    
}
