/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.mo;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Stefano
 */
public class Spedizione {

    private Long idSpedizione;
    private Date dataSpedizione;
    private String metodoSpedizione;
    private BigDecimal speseSpedizione;

    private Ordine ordine;
    /*1:1*/

    public Long getIdSpedizione() {
        return idSpedizione;
    }

    public void setIdSpedizione(Long idSpedizione) {
        this.idSpedizione = idSpedizione;
    }

    public Date getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public String getMetodoSpedizione() {
        return metodoSpedizione;
    }

    public void setMetodoSpedizione(String metodoSpedizione) {
        this.metodoSpedizione = metodoSpedizione;
    }

    public BigDecimal getSpeseSpedizione() {
        return speseSpedizione;
    }

    public void setSpeseSpedizione(BigDecimal speseSpedizione) {
        this.speseSpedizione = speseSpedizione;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

}
