package model.session.dao.CookieImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.SessionDAOFactory;
import model.session.dao.AdminLoggatoDAO;
import model.session.dao.CarrelloDAO;
import model.session.dao.UtenteLoggatoDAO;

public class CookieSessionDAOFactory extends SessionDAOFactory {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public void initSession(HttpServletRequest request, HttpServletResponse response) {

        try {
            this.request = request;
            this.response = response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public AdminLoggatoDAO getAdminLoggatoDAO() {
        return new AdminLoggatoCookie(request, response);
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {
        return new CarrelloCookie(request, response);
    }

    @Override
    public UtenteLoggatoDAO getUtenteLoggatoDAO() {
        return new UtenteLoggatoCookie(request, response);
    }

}
