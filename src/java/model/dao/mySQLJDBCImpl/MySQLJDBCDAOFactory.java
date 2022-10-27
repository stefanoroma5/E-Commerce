package model.dao.mySQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import model.dao.AmministratoreDAO;
import model.dao.CatalogoDAO;
import model.dao.CategoriaDAO;
import model.dao.DAOFactory;
import model.dao.IndirizziDAO;
import model.dao.OrdineDAO;
import model.dao.PagamentoDAO;
import model.dao.ProdottoDAO;
import model.dao.SpedizioneDAO;
import model.dao.UtenteDAO;
import model.dao.VetrinaDAO;

import services.config.Configuration;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Connection connection;

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL, "DB2INST1", "PROGETTO"); //MySQL togliere ultimi 2 parametri
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VetrinaDAO getVetrinaDAO() {
        return new VetrinaSQL(connection);
    }

    @Override
    public ProdottoDAO getProdottoDAO() {
        return new ProdottoSQL(connection);
    }

    @Override
    public AmministratoreDAO getAmministratoreDAO() {
        return new AmministratoreSQL(connection);
    }

    @Override
    public CatalogoDAO getCatalogoDAO() {
        return new CatalogoSQL(connection);
    }

    @Override
    public CategoriaDAO getCategoriaDAO() {
        return new CategoriaSQL(connection);
    }

    @Override
    public IndirizziDAO getIndirizziDAO() {
        return new IndirizziSQL(connection);
    }

    @Override
    public OrdineDAO getOrdineDAO() {
        return new OrdineSQL(connection);
    }

    @Override
    public PagamentoDAO getPagamentoDAO() {
        return new PagamentoSQL(connection);
    }

    @Override
    public SpedizioneDAO getSpedizioneDAO() {
        return new SpedizioneSQL(connection);
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteSQL(connection);
    }
}
