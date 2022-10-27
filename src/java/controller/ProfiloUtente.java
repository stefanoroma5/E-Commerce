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
import model.dao.IndirizziDAO;
import model.dao.OrdineDAO;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Indirizzi;
import model.mo.Ordine;
import model.mo.Utente;
import model.session.dao.CarrelloDAO;
import model.session.dao.SessionDAOFactory;
import model.session.dao.UtenteLoggatoDAO;
import model.session.mo.Carrello;
import model.session.mo.UtenteLoggato;
import services.config.Configuration;
import services.logservice.LogService;

/**
 *
 * @author Stefano
 */
public class ProfiloUtente {

    private ProfiloUtente() {
    }

    public static void creaAccount(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = new Utente();
            try {
                utente = utenteDAO.inserisci(
                        request.getParameter("newnome"),
                        request.getParameter("newcognome"),
                        request.getParameter("newemail"),
                        request.getParameter("newpassword"));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Utente gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di utente gia' esistente");
                daoFactory.rollbackTransaction();
            }

            utenteLoggatoDAO.crea(utente.getIdUtente(), utente.getNome(), utente.getCognome());

            vistaIndirizziAccount(daoFactory, utente.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("utente", utente);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void datiAccount(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.cercaPerIdUtente(utenteLoggato.getIdUtente());

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("utente", utente);

            request.setAttribute("viewUrl", "profilo-utente/dati-account");

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

    public static void aggiornaDatiAccount(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.cercaPerIdUtente(utenteLoggato.getIdUtente());
            utente.setNome(request.getParameter("nome"));
            utente.setCognome(request.getParameter("cognome"));
            utente.setEmail(request.getParameter("email"));
            utente.setPassword(request.getParameter("password"));
            try {
                utenteDAO.aggiorna(utente);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Utente gia' esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento con utente gia' esistente");
                daoFactory.rollbackTransaction();
            }

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("utente", utente);

            request.setAttribute("viewUrl", "profilo-utente/dati-account");

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

    public static void indirizziAccount(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            vistaIndirizziAccount(daoFactory, utenteLoggato.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void modificaIndirizzo(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
            Indirizzi indirizzo = indirizziDAO.trovaPerIdIndirizzo(new Long(request.getParameter("idindirizzo")));
            indirizzo.setAlias(request.getParameter("alias"));
            indirizzo.setIndirizzo(request.getParameter("indirizzo"));
            indirizzo.setCitta(request.getParameter("citta"));
            indirizzo.setProvincia(request.getParameter("provincia"));
            indirizzo.setCap(new Long(request.getParameter("cap")));
            indirizzo.setTelefono1(request.getParameter("tel1"));
            indirizzo.setTelefono2(request.getParameter("tel2"));

            try {

                indirizziDAO.aggiorna(indirizzo);

            } catch (DuplicatedObjectException e) {
                applicationMessage = "Indirizzo gia' esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento con indirizzo gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaIndirizziAccount(daoFactory, utenteLoggato.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void eliminaIndirizzo(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
            Indirizzi indirizzo = indirizziDAO.trovaPerIdIndirizzo(new Long(request.getParameter("idindirizzo")));

            indirizziDAO.elimina(indirizzo);

            vistaIndirizziAccount(daoFactory, utenteLoggato.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void selezionaIndirizzo(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();

            indirizziDAO.selezionaIndirizzo(new Long(request.getParameter("idindirizzo")));

            vistaIndirizziAccount(daoFactory, utenteLoggato.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void aggiungiIndirizzo(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.cercaPerIdUtente(utenteLoggato.getIdUtente());

            try {

                indirizziDAO.inserisci(
                        request.getParameter("alias"),
                        request.getParameter("indirizzo"),
                        request.getParameter("citta"),
                        request.getParameter("provincia"),
                        new Long(request.getParameter("cap")),
                        request.getParameter("tel1"),
                        request.getParameter("tel2"),
                        utente);

            } catch (DuplicatedObjectException e) {
                applicationMessage = "Indirizzo gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di un indirizzo gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaIndirizziAccount(daoFactory, utenteLoggato.getIdUtente(), request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "profilo-utente/indirizzi");

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

    public static void vistaIndirizziAccount(DAOFactory daoFactory, Long idUtente, HttpServletRequest request) {

        IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
        List<Indirizzi> indirizzi = indirizziDAO.trovaIndirizziPerUtente(idUtente);

        HomePage.headerCataloghi(daoFactory, request);

        request.setAttribute("indirizzi", indirizzi);
    }

    public static void ordini(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            List<Ordine> listaOrdini = ordineDAO.trovaOrdiniPerUtente(utenteLoggato.getIdUtente(), "null", "tutti");

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("listaOrdini", listaOrdini);

            request.setAttribute("viewUrl", "profilo-utente/ordini");

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

    public static void filtraOrdini(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        Logger logger = LogService.getApplicationLogger();

        try {

            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggato = utenteLoggatoDAO.trova();

            CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
            carrello = carrelloDAO.trova();
            quantitaCarrello = carrelloDAO.quantitaCarrello(carrello);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            String piuRecente = request.getParameter("piuRecente");
            String statoOrdine = request.getParameter("statoOrdine");

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            List<Ordine> listaOrdini = ordineDAO.trovaOrdiniPerUtente(utenteLoggato.getIdUtente(), piuRecente, statoOrdine);

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("listaOrdini", listaOrdini);

            request.setAttribute("viewUrl", "profilo-utente/ordini");

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
