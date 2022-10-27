package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.VetrinaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Prodotto;
import model.mo.Vetrina;

public class VetrinaSQL implements VetrinaDAO {

    private final String COUNTER_ID = "IDVETRINA";
    Connection conn;

    public VetrinaSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(String nomeVetrina) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM VETRINA"
                    + " WHERE NOMEVETRINA = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeVetrina);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome della vetrina esiste gia'");
            }

            sql
                    = "UPDATE COUNTER "
                    + "SET COUNTER_VALUE=COUNTER_VALUE+1 "
                    + "WHERE COUNTER_ID='" + COUNTER_ID + "'";

            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql
                    = "SELECT COUNTER_VALUE "
                    + "FROM COUNTER "
                    + "WHERE COUNTER_ID='" + COUNTER_ID + "'";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();

            Vetrina vetrina = new Vetrina();
            vetrina.setIdVetrina(rs.getLong("COUNTER_VALUE"));
            vetrina.setNomeVetrina(nomeVetrina);
            rs.close();

            sql
                    = " INSERT INTO VETRINA ( IDVETRINA, NOMEVETRINA, VETR_ELIMINATO)"
                    + " VALUES (?,?, 'N')";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, vetrina.getIdVetrina());
            ps.setString(i++, vetrina.getNomeVetrina());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void aggiorna(Vetrina vetrina) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT IDVETRINA "
                    + " FROM VETRINA "
                    + " WHERE NOMEVETRINA = ? AND "
                    + " IDVETRINA <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, vetrina.getNomeVetrina());
            ps.setLong(i++, vetrina.getIdVetrina());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome della vetrina esiste gia'");
            }

            sql
                    = " UPDATE VETRINA "
                    + " SET NOMEVETRINA = ? "
                    + " WHERE IDVETRINA = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, vetrina.getNomeVetrina());
            ps.setLong(i++, vetrina.getIdVetrina());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Vetrina vetrina) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE VETRINA "
                    + " SET VETR_ELIMINATO='S' "
                    + " WHERE "
                    + " IDVETRINA=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, vetrina.getIdVetrina());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sblocca(Vetrina vetrina) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE VETRINA "
                    + " SET VETR_ELIMINATO='N' "
                    + " WHERE "
                    + " IDVETRINA=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, vetrina.getIdVetrina());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Vetrina trovaPerId(Long idVetrina) {
        PreparedStatement ps;
        Vetrina vetrina = new Vetrina();

        try {

            String sql
                    = " SELECT * "
                    + "   FROM VETRINA "
                    + " WHERE "
                    + "   IDVETRINA = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idVetrina);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vetrina = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vetrina;
    }

    @Override
    public List<Vetrina> trovaVetrine() {

        PreparedStatement ps;
        List<Vetrina> listaVetrine = new ArrayList<>();
        try {
            String sql
                    = " SELECT *"
                    + " FROM VETRINA V INNER JOIN PRODOTTO P"
                    + " ON V.IDVETRINA = P.IDVETRINA"
                    + " WHERE VETR_ELIMINATO = 'N'"
                    + " AND PROD_ELIMINATO = 'N'"
                    + " AND NOMEVETRINA <> '-'"
                    + " ORDER BY V.IDVETRINA";

            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vetrina vetrina;
                vetrina = read(rs);

                rs.previous();

                List<Prodotto> listaProdotti = new ArrayList<>();
                while (rs.next()) {
                    if (vetrina.getNomeVetrina().equals(rs.getString("NOMEVETRINA"))) {
                        Prodotto prodotto;
                        prodotto = ProdottoSQL.read(rs);
                        listaProdotti.add(prodotto);
                    } else {
                        vetrina.setProdotti(listaProdotti.toArray(new Prodotto[listaProdotti.size()]));
                        /*[listaProdotti.size()] oppure 0*/

                        listaVetrine.add(vetrina);

                        vetrina = read(rs);
                        rs.previous();
                        listaProdotti.clear();
                    }
                }
                /*inserisco manualmente l'ultima vetrina perchè facendo next non valuta l'else*/
                vetrina.setProdotti(listaProdotti.toArray(new Prodotto[listaProdotti.size()]));

                listaVetrine.add(vetrina);

            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaVetrine;
    }

    @Override
    public List<Vetrina> trovaNomiVetrina() {
        PreparedStatement ps;
        List<Vetrina> listaVetrine = new ArrayList<>();

        try {

            String sql
                    = " SELECT * "
                    + "   FROM VETRINA ";

            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vetrina vetrina = new Vetrina();
                vetrina = read(rs);
                listaVetrine.add(vetrina);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaVetrine;
    }

    public static Vetrina read(ResultSet rs) {
        Vetrina vetrina = new Vetrina();

        try {
            vetrina.setIdVetrina(rs.getLong("IDVETRINA"));
        } catch (SQLException e) {
        }
        try {
            vetrina.setNomeVetrina(rs.getString("NOMEVETRINA"));
        } catch (SQLException e) {
        }
        try {
            vetrina.setEliminato(rs.getString("VETR_ELIMINATO").equals("S"));
        } catch (SQLException e) {
        }

        return vetrina;
    }
}
