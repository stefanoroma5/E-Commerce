package services.config;

import java.util.Calendar;
import java.util.logging.Level;

import model.dao.DAOFactory;
import model.session.dao.SessionDAOFactory;

public class Configuration {

    /* Database Configruation */
    public static final String DAO_IMPL = DAOFactory.MYSQLJDBCIMPL;
    //public static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DATABASE_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String SERVER_TIMEZONE = Calendar.getInstance().getTimeZone().getID();
    //public static final String DATABASE_URL = "jdbc:mysql://localhost/ecommerce?user=root&password=&serverTimezone=" + SERVER_TIMEZONE;
    public static final String DATABASE_URL = "jdbc:db2://localhost:50000/PROGETTO:retrieveMessagesFromServerOnGetMessage=true;";

    /* Session Configuration */
    public static final String SESSION_IMPL = SessionDAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME = "e-commerce";
    public static final String GLOBAL_LOGGER_FILE = "C:\\Users\\Stefano\\Documents\\logs\\ecommerce_log.%g.%u.txt";
    public static final Level GLOBAL_LOGGER_LEVEL = Level.ALL;

}
