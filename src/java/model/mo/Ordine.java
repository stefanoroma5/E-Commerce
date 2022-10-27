/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.mo;

import java.sql.Date;

/**
 *
 * @author Stefano
 */
public class Ordine {

    private Long idOrdine;
    private Date dataInserimento;
    private String statoOrdine;

    private Utente utente;
    /*N:1*/

    private Indirizzi indirizzo;
    /*N:1*/

    private Spedizione spedizione;
    /*1:1*/

    private Pagamento pagamento;
    /*1:1*/

    private Composizione[] composizione;
    /*M:N*/

    public Long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Long idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public String getStatoOrdine() {
        return statoOrdine;
    }

    public void setStatoOrdine(String statoOrdine) {
        this.statoOrdine = statoOrdine;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Indirizzi getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzi indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Spedizione getSpedizione() {
        return spedizione;
    }

    public void setSpedizione(Spedizione spedizione) {
        this.spedizione = spedizione;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Composizione[] getComposizione() {
        return composizione;
    }

    public void setComposizione(Composizione[] composizione) {
        this.composizione = composizione;
    }

    public Composizione getComposizione(int index) {
        return composizione[index];
    }

    public void setComposizione(Composizione composizione, int index) {
        this.composizione[index] = composizione;
    }

}
