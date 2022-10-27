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
import model.dao.CatalogoDAO;
import model.dao.CategoriaDAO;
import model.dao.DAOFactory;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Catalogo;
import model.mo.Categoria;
import model.session.dao.AdminLoggatoDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.AdminLoggato;
import services.config.Configuration;
import services.logservice.LogService;

/**
 *
 * @author Stefano
 */
public class AdminGestioneCataloghi {

    private AdminGestioneCataloghi() {
    }

    public static void vediCataloghi(HttpServletRequest request, HttpServletResponse response) {

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

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void aggiornaCatalogo(HttpServletRequest request, HttpServletResponse response) {

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

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
            Catalogo catalogo = catalogoDAO.trovaPerId(new Long(request.getParameter("idCatalogo")));
            catalogo.setNomeCatalogo(request.getParameter("nomeCatalogo"));
            catalogo.setDescrCatalogo(request.getParameter("descrCatalogo"));
            catalogo.setImmCatalogo(request.getParameter("immCatalogo"));

            try {
                catalogoDAO.aggiorna(catalogo);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Catalogo gia' esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento con catalogo gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void bloccaCatalogo(HttpServletRequest request, HttpServletResponse response) {

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

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
            Catalogo catalogo = catalogoDAO.trovaPerId(new Long(request.getParameter("idCatalogo")));

            if (catalogo.isEliminato()) {
                catalogoDAO.sblocca(catalogo);
            } else {
                catalogoDAO.elimina(catalogo);
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void creaCatalogo(HttpServletRequest request, HttpServletResponse response) {

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

            CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();

            try {
                catalogoDAO.inserisci(
                        request.getParameter("nuovoNomeCatalogo"),
                        request.getParameter("nuovaDescrCatalogo"),
                        request.getParameter("nuovaImmCatalogo"));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Catalogo gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di catalogo gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void aggiornaCategoria(HttpServletRequest request, HttpServletResponse response) {

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

            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
            Categoria categoria = categoriaDAO.trovaPerId(new Long(request.getParameter("idCategoria")));
            categoria.setNomeCategoria(request.getParameter("nomeCategoria"));
            categoria.setDescrCategoria(request.getParameter("descrCategoria"));
            categoria.setImmCategoria(request.getParameter("immCategoria"));

            try {
                categoriaDAO.aggiorna(categoria);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Categoria gia' esistente";
                logger.log(Level.INFO, "Tentativo di aggiornamento con categoria gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void bloccaCategoria(HttpServletRequest request, HttpServletResponse response) {

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

            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();
            Categoria categoria = categoriaDAO.trovaPerId(new Long(request.getParameter("idCategoria")));

            if (categoria.isEliminato()) {
                categoriaDAO.sblocca(categoria);
            } else {
                categoriaDAO.elimina(categoria);
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    public static void creaCategoria(HttpServletRequest request, HttpServletResponse response) {

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

            CategoriaDAO categoriaDAO = daoFactory.getCategoriaDAO();

            try {
                categoriaDAO.inserisci(
                        new Long(request.getParameter("catalogoApp")),
                        request.getParameter("nuovoNomeCategoria"),
                        request.getParameter("nuovaDescrCategoria"),
                        request.getParameter("nuovaImmCategoria"));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Categoria gia' esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di categoria gia' esistente");
                daoFactory.rollbackTransaction();
            }

            vistaCataloghi(daoFactory, request);

            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", adminLoggato != null);

            request.setAttribute("applicationMessage", applicationMessage);

            request.setAttribute("viewUrl", "adminGestioneCataloghi/cataloghi");

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

    private static void vistaCataloghi(DAOFactory daoFactory, HttpServletRequest request) {

        CatalogoDAO catalogoDAO = daoFactory.getCatalogoDAO();
        List<Catalogo> nomiCataloghi = catalogoDAO.trovaNomiCataloghi();

        List<Catalogo> listaCataloghi = catalogoDAO.trovaTuttiICataloghi();

        request.setAttribute("nomiCataloghi", nomiCataloghi);
        request.setAttribute("listaCataloghi", listaCataloghi);
    }

}
