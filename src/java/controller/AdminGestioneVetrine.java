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
import model.dao.DAOFactory;
import model.dao.VetrinaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Vetrina;
import model.session.dao.AdminLoggatoDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.AdminLoggato;
import services.config.Configuration;
import services.logservice.LogService;

/**
 *
 * @author Stefano
 */
public class AdminGestioneVetrine {

    private AdminGestioneVetrine() {
    }

    public static void vediVetrine(HttpServletRequest request, HttpServletResponse response) {

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

            vistaVetrine(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneVetrine/vetrine");

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

    public static void aggiornaVetrina(HttpServletRequest request, HttpServletResponse response) {

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

            String nomeVetrina = request.getParameter("nomeVetrina");
            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
            Vetrina vetrina = vetrinaDAO.trovaPerId(new Long(request.getParameter("idVetrina")));
            vetrina.setNomeVetrina(nomeVetrina);

            try {
                vetrinaDAO.aggiorna(vetrina);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Vetrina gia' esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento con vetrina gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaVetrine(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneVetrine/vetrine");

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

    public static void bloccaVetrina(HttpServletRequest request, HttpServletResponse response) {

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

            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
            Vetrina vetrina = vetrinaDAO.trovaPerId(new Long(request.getParameter("idVetrina")));

            if (vetrina.isEliminato()) {
                vetrinaDAO.sblocca(vetrina);
            } else {
                vetrinaDAO.elimina(vetrina);
            }

            vistaVetrine(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneVetrine/vetrine");

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

    public static void creaVetrina(HttpServletRequest request, HttpServletResponse response) {

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

            String nomeVetrina = request.getParameter("nuovaVetrina");
            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();

            try {
                vetrinaDAO.inserisci(nomeVetrina);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Vetrina gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di vetrina gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaVetrine(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneVetrine/vetrine");

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

    private static void vistaVetrine(DAOFactory daoFactory, HttpServletRequest request) {

        VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
        List<Vetrina> listaVetrine = vetrinaDAO.trovaNomiVetrina();

        request.setAttribute("listaVetrine", listaVetrine);
    }

}
