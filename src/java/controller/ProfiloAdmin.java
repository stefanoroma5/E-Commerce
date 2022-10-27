/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.AmministratoreDAO;
import model.dao.CatalogoDAO;
import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.dao.VetrinaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Amministratore;
import model.mo.Catalogo;
import model.mo.Utente;
import model.mo.Vetrina;
import model.session.dao.AdminLoggatoDAO;
import model.session.dao.CarrelloDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.AdminLoggato;
import services.config.Configuration;
import services.logservice.LogService;

/**
 *
 * @author Stefano
 */
public class ProfiloAdmin {

    private ProfiloAdmin() {
    }

    public static void paginaAccesso(HttpServletRequest request, HttpServletResponse response) {

        Logger logger = LogService.getApplicationLogger();

        try {
            request.setAttribute("loggedOn", false);
            request.setAttribute("viewUrl", "admin/accediAdmin");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }

    public static void accediAdmin(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            String emailAdmin = request.getParameter("emailAdmin");
            String passwordAdmin = request.getParameter("passwordAdmin");

            AmministratoreDAO amministratoreDAO = daoFactory.getAmministratoreDAO();
            Amministratore admin = amministratoreDAO.cercaPerEmail(emailAdmin);

            if (admin == null || !admin.getPassword().equals(passwordAdmin)) {
                applicationMessage = "Username o password errati!";

                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "admin/accediAdmin");
            } else if (admin.isEliminato()) {
                applicationMessage = "Admin bloccato!";

                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "admin/accediAdmin");
            } else {
                adminLoggato = adminLoggatoDAO.crea(admin.getIdAdmin(), admin.getNome(), admin.getCognome());

                request.setAttribute("adminLoggato", adminLoggato);
                request.setAttribute("viewUrl", "admin/home");
            }

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    public static void homeAdmin(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            request.setAttribute("loggedOn", adminLoggato != null);
            request.setAttribute("adminLoggato", adminLoggato);
            request.setAttribute("viewUrl", "admin/home");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }

    public static void vediUtenti(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            vistaUtenti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "admin/gest-utenti");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    public static void bloccaUtente(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.cercaPerIdUtente(new Long(request.getParameter("idUtente")));
            if (utente.isEliminato()) {
                utenteDAO.sblocca(utente);
            } else {
                utenteDAO.elimina(utente);
            }

            vistaUtenti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "admin/gest-utenti");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    private static void vistaUtenti(DAOFactory daoFactory, HttpServletRequest request) {

        UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

        List<String> listaIniziali = utenteDAO.trovaIniziali();

        String iniziale = request.getParameter("iniziale");

        if (iniziale == null || (!iniziale.equals("*") && !listaIniziali.contains(iniziale))) {
            iniziale = "*";
        }

        List<Utente> listaUtenti = utenteDAO.trovaPerIniziale((iniziale.equals("*")) ? null : iniziale);

        request.setAttribute("listaIniziali", listaIniziali);
        request.setAttribute("iniziale", iniziale);
        request.setAttribute("listaUtenti", listaUtenti);
    }

    public static void aggiungiAdmin(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            AmministratoreDAO adminDAO = daoFactory.getAmministratoreDAO();
            try {
                adminDAO.inserisci(
                        request.getParameter("nome"),
                        request.getParameter("cognome"),
                        request.getParameter("email"),
                        request.getParameter("password"));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Amministratore gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di amministratore gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaAdmin(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "admin/gest-admin");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    public static void vediAdmin(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            vistaAdmin(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "admin/gest-admin");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    public static void bloccaAdmin(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            AdminLoggato adminLoggato = adminLoggatoDAO.trova();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            AmministratoreDAO adminDAO = daoFactory.getAmministratoreDAO();
            Amministratore admin = adminDAO.cercaPerIdAdmin(new Long(request.getParameter("idAdmin")));
            if (admin.isEliminato()) {
                adminDAO.sblocca(admin);
            } else {
                adminDAO.elimina(admin);
            }

            vistaAdmin(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "admin/gest-admin");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

    private static void vistaAdmin(DAOFactory daoFactory, HttpServletRequest request) {

        AmministratoreDAO adminDAO = daoFactory.getAmministratoreDAO();

        List<String> listaIniziali = adminDAO.trovaIniziali();

        String iniziale = request.getParameter("iniziale");

        if (iniziale == null || (!iniziale.equals("*") && !listaIniziali.contains(iniziale))) {
            iniziale = "*";
        }

        List<Amministratore> listaAdmin = adminDAO.trovaPerIniziale((iniziale.equals("*")) ? null : iniziale);

        request.setAttribute("listaIniziali", listaIniziali);
        request.setAttribute("iniziale", iniziale);
        request.setAttribute("listaAdmin", listaAdmin);
    }

    public static void logoutAdmin(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrelloDAO.distruggi();
            quantitaCarrello = 0;

            AdminLoggatoDAO adminLoggatoDAO = sessionDAOFactory.getAdminLoggatoDAO();
            adminLoggatoDAO.distruggi();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
            List<Vetrina> listaVetrine;
            listaVetrine = vetrinaDAO.trovaVetrine();

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
            List<Catalogo> listaCataloghi;
            listaCataloghi = catalogoDAO.trovaCataloghi();

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", false);
            request.setAttribute("utenteLoggato", null);
            request.setAttribute("esisteCarrello", false);
            request.setAttribute("carrello", null);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
            request.setAttribute("viewUrl", "homePage/home");
            request.setAttribute("listaVetrine", listaVetrine);
            request.setAttribute("listaCataloghi", listaCataloghi);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
    }

}
