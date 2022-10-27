/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao.CookieImpl;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.session.dao.CarrelloDAO;
import model.session.mo.Carrello;

/**
 *
 * @author Stefano
 */
public class CarrelloCookie implements CarrelloDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public CarrelloCookie(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public List<Carrello> crea(Long idUtente, Long idProdotto, int quantita, String taglia) {
        Carrello elemento = new Carrello();
        List<Carrello> carrello = new ArrayList<>();

        elemento.setIdUtente(idUtente);
        elemento.setQuantita(quantita);
        elemento.setIdProdotto(idProdotto);
        elemento.setTaglia(taglia);

        carrello.add(elemento);

        Cookie cookie;
        cookie = new Cookie("carrello", encode(carrello));
        cookie.setPath("/");
        response.addCookie(cookie);

        return carrello;
    }

    @Override
    public void aggiorna(List<Carrello> carrello) {
        Cookie cookie;
        cookie = new Cookie("carrello", encode(carrello));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void distruggi() {
        Cookie cookie;
        cookie = new Cookie("carrello", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public List<Carrello> trova() {
        Cookie[] cookies = request.getCookies();
        List<Carrello> carrello = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && carrello == null; i++) {
                if (cookies[i].getName().equals("carrello")) {
                    carrello = decode(cookies[i].getValue());
                }
            }
        }
        return carrello;
    }

    @Override
    public int quantitaCarrello(List<Carrello> carrello) {

        int quantita = 0;
        if (carrello != null) {
            for (int i = 0; i < carrello.size(); i++) {
                quantita += carrello.get(i).getQuantita();
            }
        }
        return quantita;
    }

    private String encode(List<Carrello> carrello) {

        String encodedCarrello = "";

        for (Carrello c : carrello) {
            encodedCarrello += c.getIdUtente() + "#" + c.getIdProdotto() + "#" + c.getQuantita() + "#" + c.getTaglia() + "#";
        }

        return encodedCarrello;

    }

    private List<Carrello> decode(String encodedCarrello) {

        List<Carrello> carrello = new ArrayList<>();

        String[] values = encodedCarrello.split("#");

        int i = 0;
        while (i < values.length) {
            Carrello elemento = new Carrello();
            elemento.setIdUtente(Long.parseLong(values[i++]));
            elemento.setIdProdotto(Long.parseLong(values[i++]));
            elemento.setQuantita(Integer.parseInt(values[i++]));
            elemento.setTaglia(values[i++]);

            carrello.add(elemento);
        }

        return carrello;

    }
}
