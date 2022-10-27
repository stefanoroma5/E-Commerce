package model.mo;

public class Utente {

    private Long idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private boolean eliminato;

    private Indirizzi[] indirizzi;
    /*1:N*/

    private Ordine[] ordini;
    /*1:N*/

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Indirizzi[] getIndirizzi() {
        return indirizzi;
    }

    public void setIndirizzi(Indirizzi[] indirizzi) {
        this.indirizzi = indirizzi;
    }

    public Indirizzi getIndirizzi(int index) {
        return indirizzi[index];
    }

    public void setIndirizzi(int index, Indirizzi indirizzi) {
        this.indirizzi[index] = indirizzi;
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
