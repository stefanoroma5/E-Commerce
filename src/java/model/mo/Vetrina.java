package model.mo;

public class Vetrina {

    private Long idVetrina;
    private String nomeVetrina;
    private boolean eliminato;

    private Prodotto[] prodotti;

    public Long getIdVetrina() {
        return idVetrina;
    }

    public void setIdVetrina(Long idVetrina) {
        this.idVetrina = idVetrina;
    }

    public String getNomeVetrina() {
        return nomeVetrina;
    }

    public void setNomeVetrina(String nomeVetrina) {
        this.nomeVetrina = nomeVetrina;
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

}
