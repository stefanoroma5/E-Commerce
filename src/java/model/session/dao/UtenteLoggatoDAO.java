/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao;

import model.session.mo.UtenteLoggato;

/**
 *
 * @author Stefano
 */
public interface UtenteLoggatoDAO {

    public UtenteLoggato crea(
            Long idUtente,
            String nome,
            String cognome);

    public void aggiorna(UtenteLoggato utenteLoggato);

    public void distruggi();

    public UtenteLoggato trova();
}
