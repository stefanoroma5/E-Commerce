/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import model.dao.exception.ProdottoNonDisponibileException;
import model.dao.exception.QuantitaNonDisponibileException;
import model.mo.Indirizzi;
import model.mo.Ordine;
import model.mo.Utente;

/**
 *
 * @author Stefano
 */
public interface OrdineDAO {
    
    public Ordine inserisci(
            String statoOrdine,
            Date dataIns,
            Utente utente,
            Indirizzi indOrd);
    
    public void inserisciProdottiOrdine(
            Long idOrdine,
            Long idProdotto,
            int quantita,
            String tagliaScelta,
            BigDecimal prezzoPagato) throws QuantitaNonDisponibileException, ProdottoNonDisponibileException;
    
    public void aggiornaStatoOrdine (Ordine ordine);
    
    public List<Ordine> trovaOrdiniPerUtente(Long idUtente, String piuRecente, String statoOrdine);
    
    public List<Ordine> trovaTuttiGliOrdini(String statoOrdine, String data1, String data2);
    
    public Ordine trovaOrdine(Long idOrdine, Long idUtente);
    
    public void elimina (Ordine ordine);
}
