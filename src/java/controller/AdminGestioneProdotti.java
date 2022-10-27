/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.CatalogoDAO;
import model.dao.CategoriaDAO;
import model.dao.DAOFactory;
import model.dao.ProdottoDAO;
import model.dao.VetrinaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.dao.exception.ImmagineGiaEsistenteException;
import model.dao.exception.TagliaGiaEsistenteException;
import model.mo.Catalogo;
import model.mo.Categoria;
import model.mo.ImmGalleria;
import model.mo.Prodotto;
import model.mo.Taglie;
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
public class AdminGestioneProdotti {

    private AdminGestioneProdotti() {
    }

    public static void vediProdotti(HttpServletRequest request, HttpServletResponse response) {

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

            vistaProdotti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneProdotti/gest-prodotti");

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

    public static void eliminaProdotto(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            prodottoDAO.elimina(new Long(request.getParameter("idProdotto")));
            vistaProdotti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneProdotti/gest-prodotti");

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

    public static void paginaModifica(HttpServletRequest request, HttpServletResponse response) {

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

            vistaModificaProdotto(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("azione", "mod");

            request.setAttribute("viewUrl", "adminGestioneProdotti/insModProd");

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

    public static void modificaProdotto(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
            Categoria categoria = categoriaDAO.trovaPerId(new Long(request.getParameter("categoria")));

            int i = 0;
            for (ImmGalleria imm : prodotto.getImmGalleria()) {
                imm.setImmagini(request.getParameter("imm" + i));
                i++;
            }

            String nuovaImm = request.getParameter("nuovaImm");
            if (nuovaImm.isEmpty()) {
                nuovaImm = null;
            }

            i = 0;
            for (Taglie t : prodotto.getTaglie()) {
                t.setDisponibilita(Integer.parseInt(request.getParameter("disp" + i)));
                t.setTaglia(request.getParameter("taglia" + i));
                i++;
            }

            String nuovaDisponibilita = request.getParameter("nuovaDisp");
            int nuovaDisp;
            if (nuovaDisponibilita.isEmpty()) {
                nuovaDisp = -1;
            } else {
                nuovaDisp = Integer.parseInt(nuovaDisponibilita);
            }

            String nuovaTaglia = request.getParameter("nuovaTaglia");
            if (nuovaTaglia.isEmpty()) {
                nuovaTaglia = null;
            }

            VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
            Vetrina vetrina = vetrinaDAO.trovaPerId(new Long(request.getParameter("vetrina")));

            prodotto.setCategorie(categoria);
            prodotto.setColore(request.getParameter("colore"));
            prodotto.setDescrProdotto(request.getParameter("descrizione"));
            prodotto.setIdProdotto(new Long(request.getParameter("idProdotto")));
            prodotto.setImmAnteprima(request.getParameter("immAnteprima"));
            prodotto.setMarca(request.getParameter("marca"));
            prodotto.setNomeProdotto(request.getParameter("nomeProd"));
            prodotto.setPrezzo(new BigDecimal(request.getParameter("prezzo")));
            prodotto.setVetrine(vetrina);

            try {
                prodottoDAO.aggiorna(prodotto, nuovaDisp, nuovaTaglia, nuovaImm);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Prodotto già esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento di prodotto già esistente");
            } catch (TagliaGiaEsistenteException e) {
                applicationMessage = "Taglia già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di taglia già esistente");
            } catch (ImmagineGiaEsistenteException e) {
                applicationMessage = "Immagine già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di immagine già esistente");
            }

            vistaProdotti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneProdotti/gest-prodotti");

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

    public static void eliminaImmagine(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));
            if (prodotto.getImmGalleria().length > 1) {
                ImmGalleria imm = new ImmGalleria();
                imm.setProdotto(prodotto);
                imm.setImmagini(request.getParameter("immagine"));
                prodottoDAO.eliminaImmagine(imm);
            } else {
                applicationMessage = "Ci deve essere almeno un' immagine";
            }

            vistaModificaProdotto(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("azione", "mod");

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneProdotti/insModProd");

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

    public static void eliminaTaglia(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));
            if (prodotto.getTaglie().length > 1) {
                Taglie taglia = new Taglie();
                taglia.setProdotto(prodotto);
                taglia.setTaglia(request.getParameter("tagliaE"));
                prodottoDAO.eliminaTaglia(taglia);
            } else {
                applicationMessage = "Ci deve essere almeno una taglia";
            }

            vistaModificaProdotto(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("azione", "mod");

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneProdotti/insModProd");

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

    private static void vistaProdotti(DAOFactory daoFactory, HttpServletRequest request) {
        String catalogoSelezionato = request.getParameter("catalogoSelezionato");

        ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
        List<Prodotto> listaProdotti = prodottoDAO.trovaProdotti(catalogoSelezionato);

        CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
        List<Catalogo> listaCataloghi = catalogoDAO.trovaTuttiICataloghi();

        request.setAttribute("listaProdotti", listaProdotti);

        request.setAttribute("listaCataloghi", listaCataloghi);
    }

    private static void vistaModificaProdotto(DAOFactory daoFactory, HttpServletRequest request) {
        ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();
        Prodotto prodotto = prodottoDAO.trovaInfoProdotto(new Long(request.getParameter("idProdotto")));

        CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
        List<Catalogo> listaCataloghi = catalogoDAO.trovaTuttiICataloghi();

        VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
        List<Vetrina> listaVetrine = vetrinaDAO.trovaNomiVetrina();

        request.setAttribute("prodotto", prodotto);

        request.setAttribute("listaCataloghi", listaCataloghi);

        request.setAttribute("listaVetrine", listaVetrine);
    }

    public static void paginaInserimento(HttpServletRequest request, HttpServletResponse response) {

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

            vistaInserimentoProdotto(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("azione", "ins");

            request.setAttribute("viewUrl", "adminGestioneProdotti/insModProd");

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

    public static void inserisciProdotto(HttpServletRequest request, HttpServletResponse response) {

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

            ProdottoDAO prodottoDAO = daoFactory.getProdottoDAO();

            try {
                prodottoDAO.inserisci(
                        request.getParameter("nomeProd"),
                        request.getParameter("descrizione"),
                        request.getParameter("marca"),
                        new BigDecimal(request.getParameter("prezzo")),
                        request.getParameter("colore"),
                        request.getParameter("immAnteprima"),
                        new Long(request.getParameter("categoria")),
                        request.getParameter("nuovaTaglia"),
                        Integer.parseInt(request.getParameter("nuovaDisp")),
                        request.getParameter("nuovaImm"),
                        new Long(request.getParameter("vetrina")));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Prodotto già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di prodotto già esistente");
            }

            vistaProdotti(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneProdotti/gest-prodotti");

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

    private static void vistaInserimentoProdotto(DAOFactory daoFactory, HttpServletRequest request) {
        CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
        List<Catalogo> listaCataloghi = catalogoDAO.trovaCataloghi();

        VetrinaDAO vetrinaDAO = daoFactory.getVetrinaDAO();
        List<Vetrina> listaVetrine = vetrinaDAO.trovaNomiVetrina();

        request.setAttribute("listaCataloghi", listaCataloghi);

        request.setAttribute("listaVetrine", listaVetrine);
    }

}
