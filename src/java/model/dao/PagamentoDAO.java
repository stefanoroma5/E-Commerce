/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.math.BigDecimal;
import java.sql.Date;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Ordine;
import model.mo.Pagamento;
/**
 *
 * @author Stefano
 */
public interface PagamentoDAO {
    
    public void inserisci (
            String metodoPagam,
            BigDecimal importo,
            Date dataPagam,
            Ordine ordine) throws DuplicatedObjectException;
    
    public void aggiorna (Pagamento pagam) throws DuplicatedObjectException;
    
    public void elimina (Pagamento pagam);
}
