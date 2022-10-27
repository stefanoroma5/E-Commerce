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
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;

/**
 *
 * @author Stefano
 */
public class UtenteSQL implements UtenteDAO {

    private final String COUNTER_ID = "IDUTENTE";
    Connection conn;

    public UtenteSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Utente inserisci(
            String nome,
            String cognome,
            String email,
            String password) throws DuplicatedObjectException {

        PreparedStatement ps;

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPassword(password);

        try {

            String sql
                    = " SELECT *"
                    + " FROM utente"
                    + " WHERE ut_eliminato = 'N' AND"
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
                throw new DuplicatedObjectException("Questo utente esiste gia'");
            }

            sql
                    = "UPDATE counter "
                    + "SET counter_value = counter_value + 1 "
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

            Long idUtente = rs.getLong("counter_value");
            utente.setIdUtente(idUtente);

            rs.close();

            sql
                    = " INSERT INTO utente"
                    + "   ( idutente,"
                    + "     nome,"
                    + "     cognome,"
                    + "     email,"
                    + "     password,"
                    + "     ut_eliminato"
                    + "   )"
                    + " VALUES (?,?,?,?,?, 'N')";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idUtente);
            ps.setString(i++, nome);
            ps.setString(i++, cognome);
            ps.setString(i++, email);
            ps.setString(i++, password);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    @Override
    public void aggiorna(Utente utente) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT idutente "
                    + " FROM utente "
                    + " WHERE "
                    + " ut_eliminato ='N' AND "
                    + " nome = ? AND"
                    + " cognome = ? AND"
                    + " email = ? AND"
                    + " password = ? AND"
                    + " idutente <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, utente.getNome());
            ps.setString(i++, utente.getCognome());
            ps.setString(i++, utente.getEmail());
            ps.setString(i++, utente.getPassword());
            ps.setLong(i++, utente.getIdUtente());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Utente gia' esistente");
            }

            sql
                    = " UPDATE utente "
                    + " SET "
                    + "   nome = ?, "
                    + "   cognome = ?, "
                    + "   email = ?, "
                    + "   password = ? "
                    + " WHERE "
                    + "   idutente = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, utente.getNome());
            ps.setString(i++, utente.getCognome());
            ps.setString(i++, utente.getEmail());
            ps.setString(i++, utente.getPassword());
            ps.setLong(i++, utente.getIdUtente());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Utente utente) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE utente "
                    + " SET ut_eliminato = 'S' "
                    + " WHERE "
                    + " idutente=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, utente.getIdUtente());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sblocca(Utente utente) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE utente "
                    + " SET ut_eliminato = 'N' "
                    + " WHERE "
                    + " idutente=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, utente.getIdUtente());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente cercaPerIdUtente(Long idUtente) {

        PreparedStatement ps;
        Utente utente = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM utente "
                    + " WHERE "
                    + "   idutente = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                utente = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public Utente cercaPerEmail(String email) {

        PreparedStatement ps;
        Utente utente = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM utente "
                    + " WHERE "
                    + "   email = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                utente = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public List<Utente> trovaPerIniziale(String iniziale) {

        PreparedStatement ps;
        List<Utente> listaUtenti = new ArrayList<>();

        try {

            String sql
                    = " SELECT * "
                    + " FROM utente ";
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
                Utente utente = read(rs);
                listaUtenti.add(utente);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaUtenti;
    }

    @Override
    public List<String> trovaIniziali() {

        PreparedStatement ps;
        String iniziale;
        ArrayList<String> iniziali = new ArrayList<String>();

        try {

            String sql
                    = " SELECT DISTINCT UCase(Left(cognome,1)) AS iniziale "
                    + " FROM utente "
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

    public static Utente read(ResultSet rs) {

        Utente utente = new Utente();

        try {
            utente.setIdUtente(rs.getLong("idutente"));
        } catch (SQLException e) {
        }
        try {
            utente.setNome(rs.getString("nome"));
        } catch (SQLException e) {
        }
        try {
            utente.setCognome(rs.getString("cognome"));
        } catch (SQLException e) {
        }
        try {
            utente.setEmail(rs.getString("email"));
        } catch (SQLException e) {
        }
        try {
            utente.setPassword(rs.getString("password"));
        } catch (SQLException e) {
        }
        try {
            utente.setEliminato(rs.getString("ut_eliminato").equals("S"));
        } catch (SQLException e) {
        }

        return utente;
    }

}
