/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Catalogo;

/**
 *
 * @author Stefano
 */
public interface CatalogoDAO {
    
    public void inserisci (
            String nomeCatalogo,
            String descrCatalogo,
            String immCatalogo) throws DuplicatedObjectException;
    
    public void aggiorna (Catalogo catalogo) throws DuplicatedObjectException;
    
    public void elimina (Catalogo catalogo);
    
    public void sblocca(Catalogo catalogo);
    
    public Catalogo trovaPerId(Long idCatalogo);
    
    public List<Catalogo> trovaCataloghi();
    
    public List<Catalogo> trovaNomiCataloghi();
    
    public List<Catalogo> trovaTuttiICataloghi();
    
}
