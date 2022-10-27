/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.mo;

/**
 *
 * @author Stefano
 */
public class AdminLoggato {

    private Long idAdmin;
    private String nomeAdmin;
    private String cognomeAdmin;

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNomeAdmin() {
        return nomeAdmin;
    }

    public void setNomeAdmin(String nomeAdmin) {
        this.nomeAdmin = nomeAdmin;
    }

    public String getCognomeAdmin() {
        return cognomeAdmin;
    }

    public void setCognomeAdmin(String cognomeAdmin) {
        this.cognomeAdmin = cognomeAdmin;
    }

}
