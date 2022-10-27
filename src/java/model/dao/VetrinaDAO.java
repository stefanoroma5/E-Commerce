/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Vetrina;

/**
 *
 * @author Stefano
 */
public interface VetrinaDAO {

    public void inserisci(String nomeVetrina) throws DuplicatedObjectException;

    public void aggiorna(Vetrina vetrina) throws DuplicatedObjectException;

    public void elimina(Vetrina vetrina);
    
    public void sblocca(Vetrina vetrina);

    public List<Vetrina> trovaVetrine();
    
    public List<Vetrina> trovaNomiVetrina();
    
    public Vetrina trovaPerId(Long idVetrina);

}
