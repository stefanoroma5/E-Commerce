package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.dao.ProdottoDAO;
import model.mo.Prodotto;
import model.mo.Taglie;
import model.session.dao.CarrelloDAO;
import model.session.dao.SessionDAOFactory;
import model.session.dao.UtenteLoggatoDAO;
import model.session.mo.Carrello;
import model.session.mo.UtenteLoggato;
import services.config.Configuration;
import services.logservice.LogService;

public class GestioneCarrello {

    private GestioneCarrello() {
    }

    public static void vediCarrello(HttpServletRequest request, HttpServletResponse response) {

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

            carrelloView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);
            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
            request.setAttribute("viewUrl", "gestioneCarrello/carrello");

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

    public static void aggiungiAlCarrello(HttpServletRequest request, HttpServletResponse response) {

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

            String nomeCatalogo = request.getParameter("nomeCatalogo");

            /*trovo il prodotto da inserire passato da jsp*/
            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

            /*cerco le taglie disponibili per quel prodotto*/
            Taglie[] taglieDisponibili = prodottoDAO.trovaInfoProdotto(prodotto.getIdProdotto()).getTaglie();

            /*prendo la taglia scelta dall'utente*/
            String tagliaScelta = request.getParameter("tagliaScelta");

            /*prendo la quantita scelta dall'utente*/
            int quantitaScelta = new Integer(request.getParameter("quantitaScelta"));

            boolean tagliaDisponibile = false;

            /*guardo se la taglia che l'utente ha scelto è disponibile*/
            for (Taglie taglia : taglieDisponibili) {
                if ((taglia.getTaglia().equals(tagliaScelta)) && taglia.getDisponibilita() >= quantitaScelta) {
                    tagliaDisponibile = true;
                }
            }
            if (utenteLoggato != null) {
                /*se è disponibile guardo se il carrello è vuoto*/
                if (tagliaDisponibile) {
                    if (carrello == null) {

                        /*se è vuoto creo un nuovo cookie carrello*/
                        carrello = carrelloDAO.crea(utenteLoggato.getIdUtente(), prodotto.getIdProdotto(), quantitaScelta, tagliaScelta);
                        quantitaCarrello = quantitaScelta;
                    } else {
                        /*altrimenti aggiorno il cookie*/
                        boolean stessoProdotto = false;
                        for (Carrello c : carrello) {
                            if (c.getIdProdotto().equals(prodotto.getIdProdotto()) && c.getTaglia().equals(tagliaScelta)) {
                                c.setQuantita(quantitaScelta + c.getQuantita());
                                stessoProdotto = true;
                            }
                        }

                        if (!stessoProdotto) {
                            Carrello nuovoElemento = new Carrello();
                            nuovoElemento.setIdUtente(utenteLoggato.getIdUtente());
                            nuovoElemento.setIdProdotto(prodotto.getIdProdotto());
                            nuovoElemento.setQuantita(quantitaScelta);
                            nuovoElemento.setTaglia(tagliaScelta);

                            carrello.add(nuovoElemento);
                        }

                        carrelloDAO.aggiorna(carrello);

                        quantitaCarrello += quantitaScelta;
                    }
                    applicationMessage = "Prodotto aggiunto al carrello";
                } else {
                    applicationMessage = "Taglia o quantita' non disponibile";
                }
            } else {
                applicationMessage = "Devi prima accedere al tuo profilo o registrarti";
            }

            HomePage.headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("nomeCatalogo", nomeCatalogo);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
            request.setAttribute("viewUrl", "homePage/prodotto");

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

    public static void rimuoviDalCarrello(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        String applicationMessage = null;
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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

            boolean trovato = false;
            int i = 0;

            while (!trovato) {
                if (carrello.get(i).getIdProdotto().equals(prodotto.getIdProdotto())) {
                    quantitaCarrello -= carrello.get(i).getQuantita();
                    trovato = true;
                } else {
                    i++;
                }
            }
            carrello.remove(carrello.get(i));

            if (carrello.isEmpty()) {
                carrelloDAO.distruggi();
                carrello.clear();
                carrello = null;
            } else {
                carrelloDAO.aggiorna(carrello);
            }

            carrelloView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);
            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "gestioneCarrello/carrello");

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

    public static void modificaCarrello(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        UtenteLoggato utenteLoggato;
        String applicationMessage = null;
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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

            int nuovaQuantita = Integer.parseInt(request.getParameter("nuovaQuantita"));
            Taglie[] taglieDisp = prodotto.getTaglie();
            boolean prodottoTrovato = false;
            boolean tagliaTrovata = false;
            int i = 0;

            while (!prodottoTrovato) {
                if (carrello.get(i).getIdProdotto().equals(prodotto.getIdProdotto())) {
                    int j = 0;
                    /*cerco la taglia tra quelle del prodotto*/
                    while (!tagliaTrovata) {
                        if (taglieDisp[j].getTaglia().equals(carrello.get(i).getTaglia())) {
                            /*se la nuova quantita non e' valida la setto al valore attuale*/
                            if (nuovaQuantita > taglieDisp[j].getDisponibilita() || nuovaQuantita <= 0) {
                                nuovaQuantita = carrello.get(i).getQuantita();
                                applicationMessage = "Quantita' non disponibile";
                            } else {
                                /*altrimenti aggiungo o tolgo 1 dal carrello e lascio invariata la nuova quantita'*/
                                if (nuovaQuantita > carrello.get(i).getQuantita()) {
                                    quantitaCarrello++;
                                } else if (nuovaQuantita < carrello.get(i).getQuantita()) {
                                    quantitaCarrello--;
                                }
                            }
                            tagliaTrovata = true;
                        } else {
                            j++;
                        }
                    }

                    prodottoTrovato = true;
                } else {
                    i++;
                }
            }
            carrello.get(i).setQuantita(nuovaQuantita);

            carrelloDAO.aggiorna(carrello);

            carrelloView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);
            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "gestioneCarrello/carrello");

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

    private static void carrelloView(DAOFactory daoFactory, SessionDAOFactory sessionDAOFactory, HttpServletRequest request) {
        ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

        CarrelloDAO carrelloDAO = sessionDAOFactory.getCarrelloDAO();
        List<Carrello> carrello = carrelloDAO.trova();

        List<Prodotto> listaProdotti = new ArrayList<>();

        if (carrello != null) {
            for (Carrello elemento : carrello) {
                Prodotto prodotto = prodottoDAO.trovaInfoProdotto(elemento.getIdProdotto());
                listaProdotti.add(prodotto);
            }
        } else {
            listaProdotti = null;
        }

        HomePage.headerCataloghi(daoFactory, request);

        request.setAttribute("listaProdotti", listaProdotti);
    }

}
