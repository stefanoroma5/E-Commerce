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
public class Catalogo {

    private Long idCatalogo;
    private String nomeCatalogo;
    private String descrCatalogo;
    private String immCatalogo;
    private boolean eliminato;

    private Categoria[] categorie;
    /*M:N*/

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getNomeCatalogo() {
        return nomeCatalogo;
    }

    public void setNomeCatalogo(String nomeCatalogo) {
        this.nomeCatalogo = nomeCatalogo;
    }

    public String getDescrCatalogo() {
        return descrCatalogo;
    }

    public void setDescrCatalogo(String descrCatalogo) {
        this.descrCatalogo = descrCatalogo;
    }

    public String getImmCatalogo() {
        return immCatalogo;
    }

    public void setImmCatalogo(String immCatalogo) {
        this.immCatalogo = immCatalogo;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Categoria[] getCategorie() {
        return categorie;
    }

    public void setCategorie(Categoria[] categorie) {
        this.categorie = categorie;
    }

    public Categoria getCategorie(int index) {
        return categorie[index];
    }

    public void setCategorie(int index, Categoria categorie) {
        this.categorie[index] = categorie;
    }

}
