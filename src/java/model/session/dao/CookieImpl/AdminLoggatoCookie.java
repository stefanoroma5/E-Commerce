/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.session.dao.CookieImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.session.dao.AdminLoggatoDAO;
import model.session.mo.AdminLoggato;

/**
 *
 * @author Stefano
 */
public class AdminLoggatoCookie implements AdminLoggatoDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public AdminLoggatoCookie(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public AdminLoggato crea(Long idAdmin, String nome, String cognome) {
        AdminLoggato adminLoggato = new AdminLoggato();
        adminLoggato.setIdAdmin(idAdmin);
        adminLoggato.setNomeAdmin(nome);
        adminLoggato.setCognomeAdmin(cognome);

        Cookie cookie;
        cookie = new Cookie("adminLoggato", encode(adminLoggato));
        cookie.setPath("/");
        response.addCookie(cookie);

        return adminLoggato;
    }

    @Override
    public void aggiorna(AdminLoggato adminLoggato) {
        Cookie cookie;
        cookie = new Cookie("adminLoggato", encode(adminLoggato));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void distruggi() {
        Cookie cookie;
        cookie = new Cookie("adminLoggato", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public AdminLoggato trova() {
        Cookie[] cookies = request.getCookies();
        AdminLoggato adminLoggato = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && adminLoggato == null; i++) {
                if (cookies[i].getName().equals("adminLoggato")) {
                    adminLoggato = decode(cookies[i].getValue());
                }
            }
        }
        return adminLoggato;
    }

    private String encode(AdminLoggato adminLoggato) {

        String encodedAdminLoggato;
        encodedAdminLoggato = adminLoggato.getIdAdmin() + "#" + adminLoggato.getNomeAdmin() + "#" + adminLoggato.getCognomeAdmin();
        return encodedAdminLoggato;

    }

    private AdminLoggato decode(String encodedAdminLoggato) {

        AdminLoggato adminLoggato = new AdminLoggato();

        String[] values = encodedAdminLoggato.split("#");

        adminLoggato.setIdAdmin(Long.parseLong(values[0]));
        adminLoggato.setNomeAdmin(values[1]);
        adminLoggato.setCognomeAdmin(values[2]);

        return adminLoggato;

    }
}
