/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao;

import model.session.mo.AdminLoggato;

/**
 *
 * @author Stefano
 */
public interface AdminLoggatoDAO {

    public AdminLoggato crea(
            Long idAdmin,
            String nome,
            String cognome);

    public void aggiorna(AdminLoggato adminLoggato);

    public void distruggi();

    public AdminLoggato trova();
}
