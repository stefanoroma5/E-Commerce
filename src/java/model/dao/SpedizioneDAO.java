/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.math.BigDecimal;
import java.sql.Date;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Spedizione;

/**
 *
 * @author Stefano
 */
public interface SpedizioneDAO {
    
    public void inserisci (
            String metodoSpediz,
            BigDecimal speseSpediz,
            Date dataSpediz,
            Long idOrdine) throws DuplicatedObjectException;
    
    public void aggiorna (Spedizione spediz) throws DuplicatedObjectException;
    
    public void elimina (Spedizione spediz);
}
