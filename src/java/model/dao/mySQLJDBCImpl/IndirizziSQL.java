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
import model.dao.IndirizziDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Indirizzi;
import model.mo.Utente;

/**
 *
 * @author Stefano
 */
public class IndirizziSQL implements IndirizziDAO {

    private final String COUNTER_ID = "IDINDIRIZZO";
    Connection conn;

    public IndirizziSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(
            String alias,
            String indirizzo,
            String citta,
            String provincia,
            Long cap,
            String telefono1,
            String telefono2,
            Utente utente) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM indirizzi"
                    + " WHERE ind_eliminato = 'N' AND"
                    + " alias = ? AND"
                    + " indirizzo = ? AND"
                    + " citta = ? AND"
                    + " provincia = ? AND"
                    + " cap = ? AND"
                    + " telefono1 = ? AND"
                    + " idutente = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, alias);
            ps.setString(i++, indirizzo);
            ps.setString(i++, citta);
            ps.setString(i++, provincia);
            ps.setLong(i++, cap);
            ps.setString(i++, telefono1);
            ps.setLong(i++, utente.getIdUtente());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Questo indirizzo esiste gia'");
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

            Long idIndirizzo = rs.getLong("counter_value");

            rs.close();

            sql
                    = " INSERT INTO indirizzi"
                    + "   ( idindirizzo,"
                    + "     alias,"
                    + "     indirizzo,"
                    + "     citta,"
                    + "     provincia,"
                    + "     cap,"
                    + "     telefono1,"
                    + "     telefono2,"
                    + "     idutente,"
                    + "     ind_eliminato,"
                    + "     selezionato"
                    + "   )"
                    + " VALUES (?,?,?,?,?,?,?,?,?, 'N', 'N')";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idIndirizzo);
            ps.setString(i++, alias);
            ps.setString(i++, indirizzo);
            ps.setString(i++, citta);
            ps.setString(i++, provincia);
            ps.setLong(i++, cap);
            ps.setString(i++, telefono1);
            ps.setString(i++, telefono2);
            ps.setLong(i++, utente.getIdUtente());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiorna(Indirizzi indirizzi) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT idindirizzo "
                    + " FROM indirizzi "
                    + " WHERE "
                    + " ind_eliminato ='N' AND "
                    + " alias = ? AND"
                    + " indirizzo = ? AND"
                    + " citta = ? AND"
                    + " provincia = ? AND"
                    + " cap = ? AND"
                    + " telefono1 = ? AND"
                    + " idutente = ? AND"
                    + " idindirizzo <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, indirizzi.getAlias());
            ps.setString(i++, indirizzi.getIndirizzo());
            ps.setString(i++, indirizzi.getCitta());
            ps.setString(i++, indirizzi.getProvincia());
            ps.setLong(i++, indirizzi.getCap());
            ps.setString(i++, indirizzi.getTelefono1());
            ps.setLong(i++, indirizzi.getUtenti().getIdUtente());
            ps.setLong(i++, indirizzi.getIdIndirizzo());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Indirizzo gia' esistente");
            }

            sql
                    = " UPDATE indirizzi "
                    + " SET "
                    + "   alias = ?, "
                    + "   indirizzo = ?, "
                    + "   citta = ?, "
                    + "   provincia = ?, "
                    + "   cap = ?, "
                    + "   telefono1 = ?, "
                    + "   telefono2 = ?  "
                    + " WHERE "
                    + "   idindirizzo = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, indirizzi.getAlias());
            ps.setString(i++, indirizzi.getIndirizzo());
            ps.setString(i++, indirizzi.getCitta());
            ps.setString(i++, indirizzi.getProvincia());
            ps.setLong(i++, indirizzi.getCap());
            ps.setString(i++, indirizzi.getTelefono1());
            ps.setString(i++, indirizzi.getTelefono2());
            ps.setLong(i++, indirizzi.getIdIndirizzo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void elimina(Indirizzi indirizzi) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE indirizzi "
                    + " SET ind_eliminato = 'S' "
                    + " WHERE "
                    + " idindirizzo=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, indirizzi.getIdIndirizzo());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Indirizzi trovaIndirizzoSelezionato(Long idUtente) {
        PreparedStatement ps;
        Indirizzi indirizzo = new Indirizzi();

        try {

            String sql
                    = " SELECT *"
                    + " FROM indirizzi "
                    + " WHERE idutente = ? "
                    + " AND ind_eliminato = 'N'"
                    + " AND selezionato = 'S'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                indirizzo = read(rs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indirizzo;
    }

    @Override
    public List<Indirizzi> trovaIndirizziPerUtente(Long idUtente) {

        PreparedStatement ps;
        List<Indirizzi> listaIndirizzi = new ArrayList<>();
        try {

            /*String sql
                    = " SELECT * "
                    + "   FROM utente NATURAL JOIN indirizzi "
                    + " WHERE "
                    + "   idutente = ? AND"
                    + "   ind_eliminato = 'N'";*/
            String sql
                    = " SELECT * "
                    + " FROM utente U INNER JOIN indirizzi I"
                    + " ON U.idutente = I.idutente"
                    + " WHERE "
                    + "   U.idutente = ? AND"
                    + "   ind_eliminato = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idUtente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Indirizzi indirizzo;
                indirizzo = read(rs);
                listaIndirizzi.add(indirizzo);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaIndirizzi;
    }

    @Override
    public Indirizzi trovaPerIdIndirizzo(Long idIndirizzo) {
        PreparedStatement ps;
        Indirizzi indirizzo = new Indirizzi();

        try {

            String sql
                    = " SELECT *"
                    + " FROM indirizzi "
                    + " WHERE idindirizzo = ? "
                    + " AND ind_eliminato = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idIndirizzo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                indirizzo = read(rs);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indirizzo;
    }

    @Override
    public void selezionaIndirizzo(Long idIndirizzo) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE indirizzi"
                    + " SET selezionato='N'"
                    + " WHERE selezionato='S'";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql
                    = " UPDATE indirizzi "
                    + " SET selezionato = 'S' "
                    + " WHERE "
                    + " idindirizzo=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idIndirizzo);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Indirizzi read(ResultSet rs) {

        Indirizzi indirizzo = new Indirizzi();
        Utente utente = new Utente();
        indirizzo.setUtenti(utente);

        try {
            indirizzo.setIdIndirizzo(rs.getLong("idindirizzo"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setAlias(rs.getString("alias"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setIndirizzo(rs.getString("indirizzo"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setCitta(rs.getString("citta"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setProvincia(rs.getString("provincia"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setCap(rs.getLong("cap"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setTelefono1(rs.getString("telefono1"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setTelefono2(rs.getString("telefono2"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.getUtenti().setIdUtente(rs.getLong("idutente"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setEliminato(rs.getString("ind_eliminato").equals("S"));
        } catch (SQLException e) {
        }
        try {
            indirizzo.setSelezionato(rs.getString("selezionato").equals("S"));
        } catch (SQLException e) {
        }

        return indirizzo;
    }

}
