/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.math.BigDecimal;
import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.dao.exception.ImmagineGiaEsistenteException;
import model.dao.exception.TagliaGiaEsistenteException;
import model.mo.ImmGalleria;
import model.mo.Prodotto;
import model.mo.Taglie;

/**
 *
 * @author Stefano
 */
public interface ProdottoDAO {
    
    public void inserisci(
            String nomeProdotto,
            String descrProdotto,
            String marca,
            BigDecimal prezzo,
            String colore,
            String immAnteprima,
            Long idCategoria,
            String taglia,
            int disponibilita,
            String immGalleria,
            Long idVetrina) throws DuplicatedObjectException;
    
    public void aggiorna (Prodotto prodotto, int nuovaDisp, String nuovaTaglia, String nuovaImm) throws DuplicatedObjectException, ImmagineGiaEsistenteException, TagliaGiaEsistenteException;
    
    public void elimina (Long idProdotto);
    
    public List<Prodotto> trovaProdottiFiltrati(
            String nomeCatalogo,
            String nomeCategoria,
            List<String> marche,
            List<String> taglie,
            String prezzo); 
    
    public List<Prodotto> trovaProdotti(String nomeCatalogo);
    
    public Prodotto trovaInfoProdotto (Long idProdotto);
    
    public List<String> trovaMarcheProdotti(String nomeCatalogo, String nomeCategoria);
    
    public List<String> trovaTaglieProdotti(String nomeCatalogo, String nomeCategoria);
    
    public void eliminaImmagine(ImmGalleria immagine);
    
    public void eliminaTaglia(Taglie taglia);
    
}
