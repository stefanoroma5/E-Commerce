/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.CatalogoDAO;
import model.dao.DAOFactory;
import model.dao.IndirizziDAO;
import model.dao.OrdineDAO;
import model.dao.PagamentoDAO;
import model.dao.ProdottoDAO;
import model.dao.SpedizioneDAO;
import model.dao.UtenteDAO;
import model.dao.VetrinaDAO;
import model.dao.exception.ProdottoNonDisponibileException;
import model.dao.exception.QuantitaNonDisponibileException;
import model.mo.Catalogo;
import model.mo.Indirizzi;
import model.mo.Ordine;
import model.mo.Prodotto;
import model.mo.Utente;
import model.mo.Vetrina;
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
public class Acquisto {

    private Acquisto() {
    }

    public static void indSped(HttpServletRequest request, HttpServletResponse response) {

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

            BigDecimal totaleProdotti = new BigDecimal(request.getParameter("totaleProdotti"));

            String tipoSped = request.getParameter("tipoSped");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
            Indirizzi indirizzo = indirizziDAO.trovaIndirizzoSelezionato(utenteLoggato.getIdUtente());

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("tipoSped", tipoSped);
            request.setAttribute("totaleProdotti", totaleProdotti);

            request.setAttribute("indirizzo", indirizzo);

            request.setAttribute("viewUrl", "acquisto/spedizione");

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

    public static void modPagamento(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        UtenteLoggato utenteLoggato;
        DAOFactory daoFactory = null;
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

            BigDecimal totaleProdotti = new BigDecimal(request.getParameter("totaleProdotti"));
            String tipoSped = request.getParameter("tipoSped");

            String metodoPagam = request.getParameter("metodoPagam");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("tipoSped", tipoSped);
            request.setAttribute("totaleProdotti", totaleProdotti);

            request.setAttribute("metodoPagam", metodoPagam);

            request.setAttribute("viewUrl", "acquisto/pagamento");

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

    public static void confermaModPagam(HttpServletRequest request, HttpServletResponse response) {

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

            BigDecimal totaleProdotti = new BigDecimal(request.getParameter("totaleProdotti"));
            String tipoSped = request.getParameter("tipoSped");
            String metodoPagam = request.getParameter("metodoPagam");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
            Indirizzi indirizzo = indirizziDAO.trovaIndirizzoSelezionato(utenteLoggato.getIdUtente());

            List<Prodotto> listaProdotti = new ArrayList<>();
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

            if (carrello != null) {
                for (Carrello elemento : carrello) {
                    Prodotto prodotto = prodottoDAO.trovaInfoProdotto(elemento.getIdProdotto());
                    listaProdotti.add(prodotto);
                }
            } else {
                listaProdotti = null;
            }

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("listaProdotti", listaProdotti);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("tipoSped", tipoSped);
            request.setAttribute("totaleProdotti", totaleProdotti);

            request.setAttribute("metodoPagam", metodoPagam);

            request.setAttribute("indirizzo", indirizzo);

            request.setAttribute("viewUrl", "acquisto/conferma-ordine");

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

    public static void confermaOrdine(HttpServletRequest request, HttpServletResponse response) {

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

            String tipoSped = request.getParameter("tipoSped");
            BigDecimal speseSped = new BigDecimal(0);
            if (tipoSped.equals("Veloce")) {
                speseSped = new BigDecimal(5);
            } else if (tipoSped.equals("Standard")) {
                speseSped = new BigDecimal(3);
            }
            String metodoPagam = request.getParameter("metodoPagam");

            BigDecimal totaleProdotti = new BigDecimal(request.getParameter("totaleProdotti"));

            BigDecimal totaleOrdine = totaleProdotti.add(speseSped);
            if (metodoPagam.equals("Alla Consegna")) {
                totaleOrdine.add(new BigDecimal(3));
            }

            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            SpedizioneDAO spedizioneDAO = daoFactory.getSpedizioneDAO();
            PagamentoDAO pagamentoDAO = daoFactory.getPagamentoDAO();

            IndirizziDAO indirizziDAO = daoFactory.getIndirizziDAO();
            Indirizzi indirizzo = indirizziDAO.trovaIndirizzoSelezionato(utenteLoggato.getIdUtente());

            List<Prodotto> listaProdotti = new ArrayList<>();
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

            if (carrello != null) {
                for (Carrello elemento : carrello) {
                    Prodotto prodotto = prodottoDAO.trovaInfoProdotto(elemento.getIdProdotto());
                    listaProdotti.add(prodotto);
                }
            } else {
                listaProdotti = null;
            }
            int i = 0;

            Date data = new Date(Calendar.getInstance().getTime().getTime());
            Ordine ordine = new Ordine();

            try {

                ordine = ordineDAO.inserisci("Confermato", data, utente, indirizzo);

                for (Carrello elemento : carrello) {
                    ordineDAO.inserisciProdottiOrdine(
                            ordine.getIdOrdine(),
                            elemento.getIdProdotto(),
                            elemento.getQuantita(),
                            elemento.getTaglia(),
                            listaProdotti.get(i).getPrezzo());
                    i++;
                }

                spedizioneDAO.inserisci(tipoSped, speseSped, null, ordine.getIdOrdine());

                pagamentoDAO.inserisci(metodoPagam, totaleOrdine, data, ordine);

                applicationMessage = "Ordine inserito con successo";

                carrelloDAO.distruggi();
                carrello.clear();
                quantitaCarrello = 0;

            } catch (QuantitaNonDisponibileException e) {
                applicationMessage = "Quantita' non disponibile";
                logger.log(Level.INFO, "Tentativo di acquisto di un prodotto di quantita' non disponibile");
                daoFactory.rollbackTransaction();
            } catch (ProdottoNonDisponibileException e) {
                applicationMessage = "Prodotto non disponibile";
                logger.log(Level.INFO, "Tentativo di acquisto di un prodotto non esistente");
                daoFactory.rollbackTransaction();
            }

            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
            List<Vetrina> listaVetrine;
            listaVetrine = vetrinaDAO.trovaVetrine();

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
            List<Catalogo> listaCataloghi;
            listaCataloghi = catalogoDAO.trovaCataloghi();

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("listaVetrine", listaVetrine);
            request.setAttribute("listaCataloghi", listaCataloghi);

            request.setAttribute("viewUrl", "homePage/home");

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
