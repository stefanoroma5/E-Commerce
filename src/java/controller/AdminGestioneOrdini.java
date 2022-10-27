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
import model.dao.OrdineDAO;
import model.dao.UtenteDAO;
import model.mo.Ordine;
import model.mo.Utente;
import model.session.dao.AdminLoggatoDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.AdminLoggato;
import services.config.Configuration;
import services.logservice.LogService;

/**
 *
 * @author Stefano
 */
public class AdminGestioneOrdini {

    private AdminGestioneOrdini() {
    }

    public static void ordiniUtente(HttpServletRequest request, HttpServletResponse response) {

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

            vistaOrdiniUtente(daoFactory, request, "null", "tutti");

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordiniUtente");

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

    public static void filtraOrdiniUtente(HttpServletRequest request, HttpServletResponse response) {

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

            String piuRecente = request.getParameter("piuRecente");
            String statoOrdine = request.getParameter("statoOrdine");

            vistaOrdiniUtente(daoFactory, request, piuRecente, statoOrdine);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordiniUtente");

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

    public static void aggiornaStatoOrdineUtente(HttpServletRequest request, HttpServletResponse response) {

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

            String piuRecente = request.getParameter("piuRecente");
            String statoOrdine = request.getParameter("statoOrdine");

            String nuovoStatoOrdine = request.getParameter("nuovoStatoOrdine");
            Long idOrdine = new Long(request.getParameter("idOrdine"));

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            Ordine ordine = new Ordine();
            ordine.setIdOrdine(idOrdine);
            ordine.setStatoOrdine(nuovoStatoOrdine);
            ordineDAO.aggiornaStatoOrdine(ordine);

            vistaOrdiniUtente(daoFactory, request, piuRecente, statoOrdine);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordiniUtente");

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

    private static void vistaOrdiniUtente(DAOFactory daoFactory, HttpServletRequest request, String piuRecente, String statoOrdine) {

        Long idUtente = new Long(request.getParameter("idUtente"));
        UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
        Utente utente = utenteDAO.cercaPerIdUtente(idUtente);

        OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
        List<Ordine> listaOrdini = ordineDAO.trovaOrdiniPerUtente(idUtente, piuRecente, statoOrdine);

        request.setAttribute("utente", utente);

        request.setAttribute("listaOrdini", listaOrdini);
    }

    public static void tuttiGliOrdini(HttpServletRequest request, HttpServletResponse response) {

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

            vistaTuttiGliOrdini(daoFactory, request, "tutti", "", "");

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordini");

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

    public static void filtraTuttiGliOrdini(HttpServletRequest request, HttpServletResponse response) {

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

            String statoOrdine = request.getParameter("statoOrdine");
            String data1 = request.getParameter("data1");
            String data2 = request.getParameter("data2");

            vistaTuttiGliOrdini(daoFactory, request, statoOrdine, data1, data2);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordini");

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

    public static void vediOrdine(HttpServletRequest request, HttpServletResponse response) {

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

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            Ordine ordine = ordineDAO.trovaOrdine(
                    new Long(request.getParameter("idOrdine")),
                    new Long(request.getParameter("idUtente")));

            String statoOrdine = request.getParameter("statoOrdine");
            String data1 = request.getParameter("data1");
            String data2 = request.getParameter("data2");

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("ordine", ordine);

            request.setAttribute("statoOrdine", statoOrdine);
            request.setAttribute("data1", data1);
            request.setAttribute("data2", data2);

            request.setAttribute("viewUrl", "adminGestioneOrdini/vediOrdine");

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

    public static void aggiornaStatoOrdine(HttpServletRequest request, HttpServletResponse response) {

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

            String statoOrdine = request.getParameter("statoOrdine");
            String data1 = request.getParameter("data1");
            String data2 = request.getParameter("data2");

            String nuovoStatoOrdine = request.getParameter("nuovoStatoOrdine");
            Long idOrdine = new Long(request.getParameter("idOrdine"));

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            Ordine ordine = new Ordine();
            ordine.setIdOrdine(idOrdine);
            ordine.setStatoOrdine(nuovoStatoOrdine);
            ordineDAO.aggiornaStatoOrdine(ordine);

            vistaTuttiGliOrdini(daoFactory, request, statoOrdine, data1, data2);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneOrdini/ordini");

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

    private static void vistaTuttiGliOrdini(DAOFactory daoFactory, HttpServletRequest request, String statoOrdine, String data1, String data2) {

        OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
        List<Ordine> listaOrdini = ordineDAO.trovaTuttiGliOrdini(statoOrdine, data1, data2);

        request.setAttribute("listaOrdini", listaOrdini);
    }

}
