/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Categoria;

/**
 *
 * @author Stefano
 */
public interface CategoriaDAO {
    
    public void inserisci (
            Long idCatalogo,
            String nomeCategoria,
            String descrCategoria,
            String immCategoria) throws DuplicatedObjectException;
    
    public void aggiorna (Categoria categoria) throws DuplicatedObjectException;
    
    public void elimina (Categoria categoria);
    
    public void sblocca(Categoria categoria);
    
    public Categoria trovaPerId(Long idCategoria);
}
