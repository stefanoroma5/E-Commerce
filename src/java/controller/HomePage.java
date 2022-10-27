package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import model.dao.CatalogoDAO;

import services.config.Configuration;
import services.logservice.LogService;

import model.dao.DAOFactory;
import model.dao.ProdottoDAO;
import model.dao.UtenteDAO;
import model.dao.VetrinaDAO;
import model.mo.Catalogo;
import model.mo.Categoria;
import model.mo.Prodotto;
import model.mo.Utente;
import model.mo.Vetrina;
import model.session.dao.CarrelloDAO;

import model.session.mo.UtenteLoggato;
import model.session.dao.SessionDAOFactory;
import model.session.dao.UtenteLoggatoDAO;
import model.session.mo.Carrello;

public class HomePage {

    private HomePage() {
    }

    public static void home(HttpServletRequest request, HttpServletResponse response) {

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

            homeView(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

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

    public static void visualizzaCatalogo(HttpServletRequest request, HttpServletResponse response) {

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

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
            List<Catalogo> listaCataloghi;
            listaCataloghi = catalogoDAO.trovaCataloghi();

            Categoria[] categorie = null;

            String nomeCatalogo = request.getParameter("nomeCatalogo");

            if ((nomeCatalogo != null) && (!nomeCatalogo.equals("null"))) {
                for (Catalogo catalogo : listaCataloghi) {/*devo passare dalla jsp il nome del catalogo*/
                    if (catalogo.getNomeCatalogo().equals(nomeCatalogo)) {
                        categorie = catalogo.getCategorie();
                    }
                }
            }
            String categoriaSelezionata = request.getParameter("categoriaSelezionata");

            String prezzoScelto = request.getParameter("prezzoScelto");

            String listaMarcheSelezionate = request.getParameter("listaMarcheSelezionate");
            List<String> listaMarche = new ArrayList<>();
            String[] values;
            if (listaMarcheSelezionate != null && !listaMarcheSelezionate.equals("null")) {
                values = listaMarcheSelezionate.split(",");
                listaMarche = new ArrayList<>(Arrays.asList(values));
            } else {
                listaMarche.clear();
            }

            String marcaSelezionata = request.getParameter("marcaSelezionata");

            if ((marcaSelezionata != null) && (!marcaSelezionata.equals("null"))) {
                if (!listaMarche.isEmpty() && listaMarche.contains(marcaSelezionata)) {
                    listaMarche.remove(marcaSelezionata);
                } else {
                    listaMarche.add(marcaSelezionata);
                }
            }
            if (listaMarche.isEmpty()) {
                listaMarcheSelezionate = "null";
            } else {
                listaMarcheSelezionate = "";
                for (String elemento : listaMarche) {
                    listaMarcheSelezionate += elemento + ",";
                }
            }

            String listaTaglieSelezionate = request.getParameter("listaTaglieSelezionate");
            List<String> listaTaglie = new ArrayList<>();
            if (listaTaglieSelezionate != null && !listaTaglieSelezionate.equals("null")) {
                values = listaTaglieSelezionate.split(",");
                listaTaglie = new ArrayList<>(Arrays.asList(values));
            } else {
                listaTaglie.clear();
            }

            String tagliaSelezionata = request.getParameter("tagliaSelezionata");

            if ((tagliaSelezionata != null) && (!tagliaSelezionata.equals("null"))) {
                if (!listaTaglie.isEmpty() && listaTaglie.contains(tagliaSelezionata)) {
                    listaTaglie.remove(tagliaSelezionata);
                } else {
                    listaTaglie.add(tagliaSelezionata);
                }
            }
            if (listaTaglie.isEmpty()) {
                listaTaglieSelezionate = "null";
            } else {
                listaTaglieSelezionate = "";
                for (String elemento : listaTaglie) {
                    listaTaglieSelezionate += elemento + ",";
                }
            }

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

            List<String> listaMarcheDisponibili;
            listaMarcheDisponibili = prodottoDAO.trovaMarcheProdotti(nomeCatalogo, categoriaSelezionata);

            List<String> listaTaglieProdotti;
            listaTaglieProdotti = prodottoDAO.trovaTaglieProdotti(nomeCatalogo, categoriaSelezionata);

            List<Prodotto> listaProdottiFiltrati;

            listaProdottiFiltrati = prodottoDAO.trovaProdottiFiltrati(
                    nomeCatalogo,
                    categoriaSelezionata,
                    ((listaMarche.isEmpty()) ? null : listaMarche),
                    ((listaTaglie.isEmpty()) ? null : listaTaglie),
                    prezzoScelto);

            headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);
            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("nomeCatalogo", nomeCatalogo);

            request.setAttribute("categorie", categorie);
            request.setAttribute("categoriaSelezionata", categoriaSelezionata);

            request.setAttribute("prezzoScelto", prezzoScelto);

            request.setAttribute("listaMarcheSelezionate", listaMarcheSelezionate);

            request.setAttribute("listaTaglieSelezionate", listaTaglieSelezionate);

            request.setAttribute("listaMarcheDisponibili", listaMarcheDisponibili);

            request.setAttribute("listaTaglieProdotti", listaTaglieProdotti);

            request.setAttribute("listaProdottiFiltrati", listaProdottiFiltrati);

            request.setAttribute("viewUrl", "homePage/catalogo");

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

    public static void visualizzaProdotto(HttpServletRequest request, HttpServletResponse response) {

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

            String nomeCatalogo = request.getParameter("nomeCatalogo");

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

            headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("prodotto", prodotto);

            request.setAttribute("nomeCatalogo", nomeCatalogo);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
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

    public static void paginaRegistrazione(HttpServletRequest request, HttpServletResponse response) {
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

            headerCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("viewUrl", "homePage/accedi");
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

    public static void accediUtente(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;

        UtenteLoggato utenteLoggato;
        List<Carrello> carrello;
        int quantitaCarrello;

        String applicationMessage = null;

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

            String emailUtente = request.getParameter("emailUtente");
            String passwordUtente = request.getParameter("passwordUtente");

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.cercaPerEmail(emailUtente);

            if (utente == null || !utente.getPassword().equals(passwordUtente)) {
                applicationMessage = "Username o password errati!";
            } else if (utente.isEliminato()) {
                applicationMessage = "Utente bloccato!";
            } else {
                utenteLoggato = utenteLoggatoDAO.crea(utente.getIdUtente(), utente.getNome(), utente.getCognome());
            }

            homeView(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", utenteLoggato != null);
            request.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("esisteCarrello", carrello != null);
            request.setAttribute("carrello", carrello);
            request.setAttribute("quantitaCarrello", quantitaCarrello);

            request.setAttribute("applicationMessage", applicationMessage);
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

    public static void logoutUtente(HttpServletRequest request, HttpServletResponse response) {

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

            UtenteLoggatoDAO utenteLoggatoDAO = sessionDAOFactory.getUtenteLoggatoDAO();
            utenteLoggatoDAO.distruggi();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();

            homeView(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", false);
            request.setAttribute("utenteLoggato", null);
            request.setAttribute("esisteCarrello", false);
            request.setAttribute("carrello", null);
            request.setAttribute("quantitaCarrello", quantitaCarrello);
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

    private static void homeView(DAOFactory daoFactory, HttpServletRequest request) {

        VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
        List<Vetrina> listaVetrine;
        listaVetrine = vetrinaDAO.trovaVetrine();

        headerCataloghi(daoFactory, request);

        request.setAttribute("listaVetrine", listaVetrine);

    }

    public static void headerCataloghi(DAOFactory daoFactory, HttpServletRequest request) {
        CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
        List<Catalogo> listaCataloghi;
        listaCataloghi = catalogoDAO.trovaCataloghi();

        request.setAttribute("listaCataloghi", listaCataloghi);
    }

}
