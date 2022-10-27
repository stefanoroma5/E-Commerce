/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.mo;

/**
 *
 * @author Stefano
 */
public class Indirizzi {

    private Long idIndirizzo;
    private String alias;
    private String indirizzo;
    private String citta;
    private String provincia;
    private Long cap;
    private String telefono1;
    private String telefono2;
    private boolean eliminato;
    private boolean selezionato;

    private Utente utenti;
    /*N:1*/

    private Ordine[] ordini;
    /*1:N*/

    public Long getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(Long idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Long getCap() {
        return cap;
    }

    public void setCap(Long cap) {
        this.cap = cap;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public boolean isSelezionato() {
        return selezionato;
    }

    public void setSelezionato(boolean selezionato) {
        this.selezionato = selezionato;
    }

    public Utente getUtenti() {
        return utenti;
    }

    public void setUtenti(Utente utenti) {
        this.utenti = utenti;
    }

    public Ordine[] getOrdini() {
        return ordini;
    }

    public void setOrdini(Ordine[] ordini) {
        this.ordini = ordini;
    }

    public Ordine getOrdini(int index) {
        return ordini[index];
    }

    public void setOrdini(int index, Ordine ordini) {
        this.ordini[index] = ordini;
    }

}
