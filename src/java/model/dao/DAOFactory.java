package model.dao;

import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

public abstract class DAOFactory {

  // List of DAO types supported by the factory
  public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";

  public abstract void beginTransaction();
  public abstract void commitTransaction();
  public abstract void rollbackTransaction();
  public abstract void closeTransaction();
  
  public abstract AmministratoreDAO getAmministratoreDAO();  
  public abstract CatalogoDAO getCatalogoDAO();  
  public abstract CategoriaDAO getCategoriaDAO();  
  public abstract IndirizziDAO getIndirizziDAO(); 
  public abstract OrdineDAO getOrdineDAO();  
  public abstract PagamentoDAO getPagamentoDAO();    
  public abstract ProdottoDAO getProdottoDAO();
  public abstract SpedizioneDAO getSpedizioneDAO();
  public abstract UtenteDAO getUtenteDAO();
  public abstract VetrinaDAO getVetrinaDAO();
  
  public static DAOFactory getDAOFactory(String whichFactory) {

    if (whichFactory.equals(MYSQLJDBCIMPL)) {
      return new MySQLJDBCDAOFactory();
    } else {
      return null;
    }
  }
}
