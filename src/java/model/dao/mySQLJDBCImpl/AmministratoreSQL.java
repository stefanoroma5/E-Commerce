/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.AmministratoreDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Amministratore;

/**
 *
 * @author Stefano
 */
public class AmministratoreSQL implements AmministratoreDAO {

    private final String COUNTER_ID = "IDADMIN";
    Connection conn;

    public AmministratoreSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(String nome, String cognome, String email, String password) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM amministratore"
                    + " WHERE admin_eliminato = 'N' AND"
                    + " nome = ? AND"
                    + " cognome = ? AND"
                    + " email = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, nome);
            ps.setString(i++, cognome);
            ps.setString(i++, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Questo amministratore esiste gia'");
            }

            sql
                    = "UPDATE counter "
                    + "SET counter_value=counter_value+1 "
                    + "WHERE counter_id='" + COUNTER_ID + "'";

            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql
                    = "SELECT counter_value "
                    + "FROM counter "
                    + "WHERE counter_id='" + COUNTER_ID + "'";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();

            Long idAdmin = rs.getLong("counter_value");

            rs.close();

            sql
                    = " INSERT INTO amministratore"
                    + "   ( idadmin,"
                    + "     nome,"
                    + "     cognome,"
                    + "     email,"
                    + "     password,"
                    + "     admin_eliminato"
                    + "   )"
                    + " VALUES (?,?,?,?,?, 'N')";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idAdmin);
            ps.setString(i++, nome);
            ps.setString(i++, cognome);
            ps.setString(i++, email);
            ps.setString(i++, password);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiorna(Amministratore admin) throws DuplicatedObjectException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void elimina(Amministratore admin) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE amministratore "
                    + " SET admin_eliminato = 'S' "
                    + " WHERE "
                    + " idadmin=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, admin.getIdAdmin());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sblocca(Amministratore admin) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE amministratore "
                    + " SET admin_eliminato = 'N' "
                    + " WHERE "
                    + " idadmin=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, admin.getIdAdmin());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Amministratore cercaPerEmail(String email) {

        PreparedStatement ps;
        Amministratore admin = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM amministratore "
                    + " WHERE "
                    + "   email = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                admin = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return admin;
    }

    @Override
    public Amministratore cercaPerIdAdmin(Long idAdmin) {

        PreparedStatement ps;
        Amministratore amministratore = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM amministratore "
                    + " WHERE "
                    + "   idadmin = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idAdmin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                amministratore = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return amministratore;
    }

    @Override
    public List<Amministratore> trovaPerIniziale(String iniziale) {

        PreparedStatement ps;
        List<Amministratore> listaAdmin = new ArrayList<>();

        try {

            String sql
                    = " SELECT * "
                    + " FROM amministratore ";
            if (iniziale != null) {
                sql += "WHERE UCASE(LEFT(cognome,1)) = ?";
            }
            sql += "ORDER BY cognome, nome";

            ps = conn.prepareStatement(sql);
            if (iniziale != null) {
                ps.setString(1, iniziale);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Amministratore admin = read(rs);
                listaAdmin.add(admin);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaAdmin;
    }

    @Override
    public List<String> trovaIniziali() {

        PreparedStatement ps;
        String iniziale;
        ArrayList<String> iniziali = new ArrayList<String>();

        try {

            String sql
                    = " SELECT DISTINCT UCase(Left(cognome,1)) AS iniziale "
                    + " FROM amministratore "
                    + " ORDER BY UCase(Left(cognome,1))";

            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                iniziale = resultSet.getString("iniziale");
                iniziali.add(iniziale);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return iniziali;
    }

    public static Amministratore read(ResultSet rs) {

        Amministratore admin = new Amministratore();

        try {
            admin.setIdAdmin(rs.getLong("idadmin"));
        } catch (SQLException e) {
        }
        try {
            admin.setNome(rs.getString("nome"));
        } catch (SQLException e) {
        }
        try {
            admin.setCognome(rs.getString("cognome"));
        } catch (SQLException e) {
        }
        try {
            admin.setEmail(rs.getString("email"));
        } catch (SQLException e) {
        }
        try {
            admin.setPassword(rs.getString("password"));
        } catch (SQLException e) {
        }
        try {
            admin.setEliminato(rs.getString("admin_eliminato").equals("S"));
        } catch (SQLException e) {
        }

        return admin;
    }

}
