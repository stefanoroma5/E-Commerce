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
public class Categoria {

    private Long idCategoria;
    private String nomeCategoria;
    private String descrCategoria;
    private String immCategoria;
    private boolean eliminato;

    private Prodotto[] prodotti;
    /*1:N*/

    private Catalogo[] cataloghi;
    /*M:N*/

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getDescrCategoria() {
        return descrCategoria;
    }

    public void setDescrCategoria(String descrCategoria) {
        this.descrCategoria = descrCategoria;
    }

    public String getImmCategoria() {
        return immCategoria;
    }

    public void setImmCategoria(String immCategoria) {
        this.immCategoria = immCategoria;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Prodotto[] getProdotti() {
        return prodotti;
    }

    public void setProdotti(Prodotto[] prodotti) {
        this.prodotti = prodotti;
    }

    public Prodotto getProdotti(int index) {
        return prodotti[index];
    }

    public void setProdotti(int index, Prodotto prodotti) {
        this.prodotti[index] = prodotti;
    }

    public Catalogo[] getCataloghi() {
        return cataloghi;
    }

    public void setCataloghi(Catalogo[] cataloghi) {
        this.cataloghi = cataloghi;
    }

    public Catalogo getCataloghi(int index) {
        return cataloghi[index];
    }

    public void setCataloghi(int index, Catalogo cataloghi) {
        this.cataloghi[index] = cataloghi;
    }

}
