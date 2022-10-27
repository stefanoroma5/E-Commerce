/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao;

import java.util.List;
import model.session.mo.Carrello;

/**
 *
 * @author Stefano
 */
public interface CarrelloDAO {

    public List<Carrello> crea(
            Long idUtente,
            Long idProdotto,
            int quantita,
            String taglia);

    public void aggiorna(List<Carrello> carrello);

    public void distruggi();

    public List<Carrello> trova();

    public int quantitaCarrello(List<Carrello> carrello);
}
