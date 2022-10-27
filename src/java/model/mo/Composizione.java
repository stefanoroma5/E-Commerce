/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.mo;

import java.math.BigDecimal;

/**
 *
 * @author Stefano
 */
public class Composizione {

    private Ordine ordine;
    private Prodotto prodotto;

    private int quantita;
    private String tagliaScelta;
    private BigDecimal prezzoPagato;

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getTagliaScelta() {
        return tagliaScelta;
    }

    public void setTagliaScelta(String tagliaScelta) {
        this.tagliaScelta = tagliaScelta;
    }

    public BigDecimal getPrezzoPagato() {
        return prezzoPagato;
    }

    public void setPrezzoPagato(BigDecimal prezzoPagato) {
        this.prezzoPagato = prezzoPagato;
    }

}
