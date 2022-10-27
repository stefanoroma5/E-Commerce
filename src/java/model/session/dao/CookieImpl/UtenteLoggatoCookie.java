/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao.CookieImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.session.dao.UtenteLoggatoDAO;
import model.session.mo.UtenteLoggato;

/**
 *
 * @author Stefano
 */
public class UtenteLoggatoCookie implements UtenteLoggatoDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UtenteLoggatoCookie(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public UtenteLoggato crea(Long idUtente, String nome, String cognome) {
        UtenteLoggato utenteLoggato = new UtenteLoggato();
        utenteLoggato.setIdUtente(idUtente);
        utenteLoggato.setNomeUtente(nome);
        utenteLoggato.setCognomeUtente(cognome);

        Cookie cookie;
        cookie = new Cookie("utenteLoggato", encode(utenteLoggato));
        cookie.setPath("/");
        response.addCookie(cookie);

        return utenteLoggato;
    }

    @Override
    public void aggiorna(UtenteLoggato utenteLoggato) {
        Cookie cookie;
        cookie = new Cookie("utenteLoggato", encode(utenteLoggato));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void distruggi() {
        Cookie cookie;
        cookie = new Cookie("utenteLoggato", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public UtenteLoggato trova() {
        Cookie[] cookies = request.getCookies();
        UtenteLoggato utenteLoggato = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && utenteLoggato == null; i++) {
                if (cookies[i].getName().equals("utenteLoggato")) {
                    utenteLoggato = decode(cookies[i].getValue());
                }
            }
        }
        return utenteLoggato;
    }

    private String encode(UtenteLoggato utenteLoggato) {

        String encodedUtenteLoggato;
        encodedUtenteLoggato = utenteLoggato.getIdUtente() + "#" + utenteLoggato.getNomeUtente() + "#" + utenteLoggato.getCognomeUtente();
        return encodedUtenteLoggato;

    }

    private UtenteLoggato decode(String encodedUtenteLoggato) {

        UtenteLoggato utenteLoggato = new UtenteLoggato();

        String[] values = encodedUtenteLoggato.split("#");

        utenteLoggato.setIdUtente(Long.parseLong(values[0]));
        utenteLoggato.setNomeUtente(values[1]);
        utenteLoggato.setCognomeUtente(values[2]);

        return utenteLoggato;

    }

}
