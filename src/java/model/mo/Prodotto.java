package model.mo;

import java.math.BigDecimal;

public class Prodotto {

    private Long idProdotto;
    private String nomeProdotto;
    private String descrProdotto;
    private String marca;
    private BigDecimal prezzo;
    private String colore;
    private String immAnteprima;
    private boolean eliminato;

    private Vetrina vetrine;
    /*N:1*/

    private Categoria categorie;
    /*N:1*/

    private Taglie[] taglie;
    /*1:N (entita' debole)*/

    private ImmGalleria[] immGalleria;
    /*1:N (multivalore)*/

    private Composizione[] composizione;
    /*M:N*/

    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public String getDescrProdotto() {
        return descrProdotto;
    }

    public void setDescrProdotto(String descrProdotto) {
        this.descrProdotto = descrProdotto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getImmAnteprima() {
        return immAnteprima;
    }

    public void setImmAnteprima(String immAnteprima) {
        this.immAnteprima = immAnteprima;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Vetrina getVetrine() {
        return vetrine;
    }

    public void setVetrine(Vetrina vetrine) {
        this.vetrine = vetrine;
    }

    public Categoria getCategorie() {
        return categorie;
    }

    public void setCategorie(Categoria categorie) {
        this.categorie = categorie;
    }

    public Taglie[] getTaglie() {
        return taglie;
    }

    public void setTaglie(Taglie[] taglie) {
        this.taglie = taglie;
    }

    public Taglie getTaglie(int index) {
        return taglie[index];
    }

    public void setTaglie(int index, Taglie taglie) {
        this.taglie[index] = taglie;
    }

    public ImmGalleria[] getImmGalleria() {
        return immGalleria;
    }

    public void setImmGalleria(ImmGalleria[] immGalleria) {
        this.immGalleria = immGalleria;
    }

    public ImmGalleria getImmGalleria(int index) {
        return immGalleria[index];
    }

    public void setImmGalleria(int index, ImmGalleria immGalleria) {
        this.immGalleria[index] = immGalleria;
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

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

}
