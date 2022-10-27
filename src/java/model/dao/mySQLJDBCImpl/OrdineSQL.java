/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.mySQLJDBCImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.dao.OrdineDAO;
import model.dao.exception.ProdottoNonDisponibileException;
import model.dao.exception.QuantitaNonDisponibileException;
import model.mo.Composizione;
import model.mo.Indirizzi;
import model.mo.Ordine;
import model.mo.Pagamento;
import model.mo.Prodotto;
import model.mo.Spedizione;
import model.mo.Utente;

/**
 *
 * @author Stefano
 */
public class OrdineSQL implements OrdineDAO {

    private final String COUNTER_ID = "IDORDINE";
    Connection conn;

    public OrdineSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisciProdottiOrdine(
            Long idOrdine,
            Long idProdotto,
            int quantita,
            String tagliaScelta,
            BigDecimal prezzoPagato) throws QuantitaNonDisponibileException, ProdottoNonDisponibileException {

        PreparedStatement ps;

        try {
            //controllo se il prodotto e la quantita sono disponibili
            String sql
                    = " SELECT disponibilita"
                    + " FROM taglie"
                    + " WHERE taglia = ? AND"
                    + " idprodotto = ? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tagliaScelta);
            ps.setLong(2, idProdotto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int disponibilita = rs.getInt("disponibilita");
                if (quantita > disponibilita) {
                    throw new QuantitaNonDisponibileException("Quantita non disponibile");
                }
            } else {
                throw new ProdottoNonDisponibileException("Prodotto non disponibile");
            }

            //aggiorno la quantita' perche' ho fatto l'ordine
            sql
                    = " UPDATE taglie"
                    + " SET disponibilita = disponibilita - ? "
                    + " WHERE taglia = ?"
                    + " AND idprodotto = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantita);
            ps.setString(2, tagliaScelta);
            ps.setLong(3, idProdotto);
            ps.executeUpdate();

            rs.close();

            sql
                    = " INSERT INTO composizione"
                    + "   ( idordine,"
                    + "     idprodotto,"
                    + "     quantita,"
                    + "     tagliascelta,"
                    + "     prezzopagato"
                    + "   )"
                    + " VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, idOrdine);
            ps.setLong(i++, idProdotto);
            ps.setInt(i++, quantita);
            ps.setString(i++, tagliaScelta);
            ps.setBigDecimal(i++, prezzoPagato);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ordine inserisci(
            String statoOrdine,
            Date dataIns,
            Utente utente,
            Indirizzi indOrd) {

        PreparedStatement ps;

        Ordine ordine = new Ordine();
        ordine.setStatoOrdine(statoOrdine);
        ordine.setDataInserimento(dataIns);
        ordine.setUtente(utente);
        ordine.setIndirizzo(indOrd);

        try {

            String sql
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
            ResultSet rs = ps.executeQuery();
            rs.next();

            Long idOrdine = rs.getLong("counter_value");
            ordine.setIdOrdine(idOrdine);

            rs.close();

            sql
                    = " INSERT INTO ordine"
                    + "   ( idordine,"
                    + "     datainserimento,"
                    + "     statoordine,"
                    + "     idutente,"
                    + "     idindirizzo,"
                    + "     alias"
                    + "   )"
                    + " VALUES (?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, ordine.getIdOrdine());
            ps.setDate(i++, ordine.getDataInserimento());
            ps.setString(i++, ordine.getStatoOrdine());
            ps.setLong(i++, ordine.getUtente().getIdUtente());
            ps.setLong(i++, ordine.getIndirizzo().getIdIndirizzo());
            ps.setString(i++, ordine.getIndirizzo().getAlias());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordine;
    }

    @Override
    public void aggiornaStatoOrdine(Ordine ordine) {

        PreparedStatement ps;

        try {
            String sql
                    = " UPDATE ordine "
                    + " SET "
                    + "   statoordine = ? "
                    + " WHERE "
                    + "   idordine = ? ";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, ordine.getStatoOrdine());
            ps.setLong(i++, ordine.getIdOrdine());

            ps.executeUpdate();

            if (ordine.getStatoOrdine().equals(("Spedito"))) {
                sql
                        = " UPDATE spedizione "
                        + " SET dataspedizione = ? "
                        + " WHERE idordine = ?";
                Date data = new Date(Calendar.getInstance().getTime().getTime());
                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setDate(i++, data);
                ps.setLong(i++, ordine.getIdOrdine());

                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ordine> trovaOrdiniPerUtente(Long idUtente, String piuRecente, String statoOrdine) {
        PreparedStatement ps;
        List<Ordine> listaOrdini = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM utente NATURAL JOIN ordine "
                    + "     NATURAL JOIN spedizione NATURAL JOIN pagamento "
                    + "     NATURAL JOIN indirizzi NATURAL JOIN composizione "
                    + "     NATURAL JOIN prodotto "
                    + " WHERE idutente = ?";
            if (!statoOrdine.equals("tutti")) {
                sql += " AND statoordine = ? ";
            }
            sql += " ORDER BY datainserimento ";
            if (!piuRecente.equals("null")) {
                sql += "DESC ";
            }
            sql += " , idordine ";*/
            String sql
                    = " SELECT *"
                    + " FROM utente U INNER JOIN ordine O"
                    + "     ON O.idutente = U.idutente"
                    + " INNER JOIN spedizione S"
                    + "     ON S.idordine = O.idordine"
                    + " INNER JOIN pagamento P"
                    + "     ON P.idordine = O.idordine"
                    + " INNER JOIN indirizzi I"
                    + "     ON I.idindirizzo = O.idindirizzo"
                    + " INNER JOIN composizione C"
                    + "     ON C.idordine = O.idordine"
                    + " INNER JOIN prodotto PR"
                    + "     ON PR.idprodotto = C.idprodotto"
                    + " WHERE U.idutente = ?";
            if (!statoOrdine.equals("tutti")) {
                sql += " AND O.statoordine = ? ";
            }
            sql += " ORDER BY O.datainserimento ";
            if (!piuRecente.equals("null")) {
                sql += "DESC ";
            }
            sql += " , O.idordine ";

            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setLong(1, idUtente);
            if (!statoOrdine.equals("tutti")) {
                ps.setString(2, statoOrdine);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Ordine ordine;
                ordine = OrdineSQL.read(rs);

                Indirizzi indirizzo;
                indirizzo = IndirizziSQL.read(rs);

                Spedizione spedizione;
                spedizione = SpedizioneSQL.read(rs);

                Pagamento pagamento;
                pagamento = PagamentoSQL.read(rs);

                rs.previous();

                List<Composizione> listaComposizione = new ArrayList<>();

                while (rs.next()) {
                    if (ordine.getIdOrdine().equals(rs.getLong("idordine"))) {

                        Composizione composizione;
                        composizione = OrdineSQL.readComposizione(rs);

                        Prodotto prodotto;
                        prodotto = ProdottoSQL.read(rs);

                        composizione.setProdotto(prodotto);

                        listaComposizione.add(composizione);
                    } else {
                        ordine.setComposizione(listaComposizione.toArray(new Composizione[listaComposizione.size()]));
                        ordine.setIndirizzo(indirizzo);
                        ordine.setSpedizione(spedizione);
                        ordine.setPagamento(pagamento);

                        listaOrdini.add(ordine);

                        ordine = OrdineSQL.read(rs);
                        indirizzo = IndirizziSQL.read(rs);
                        spedizione = SpedizioneSQL.read(rs);
                        pagamento = PagamentoSQL.read(rs);

                        rs.previous();

                        listaComposizione.clear();

                    }
                }
                /*se esce dal ciclo vuol dire che è andato oltre l'ultima riga, ma se la riga che stava analizzando
                  con lo stesso idordine era l'ultima, quando il while valuta la condizione non esegue i comandi dell'else
                  perchè rs.next() ritorna false, quindi li eseguo io per salvare la listaOrdini*/
                ordine.setComposizione(listaComposizione.toArray(new Composizione[listaComposizione.size()]));
                ordine.setIndirizzo(indirizzo);
                ordine.setSpedizione(spedizione);
                ordine.setPagamento(pagamento);

                listaOrdini.add(ordine);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaOrdini;
    }

    @Override
    public List<Ordine> trovaTuttiGliOrdini(String statoOrdine, String data1, String data2) {
        PreparedStatement ps;
        List<Ordine> listaOrdini = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM utente NATURAL JOIN ordine "
                    + " NATURAL JOIN spedizione NATURAL JOIN pagamento "
                    + " NATURAL JOIN indirizzi";
            if (!statoOrdine.equals("tutti")) {
                sql += " WHERE statoordine = ? ";
            }
            if (!data1.isEmpty() && !data2.isEmpty()) {
                if (!statoOrdine.equals("tutti")) {
                    sql += " AND ";
                } else {
                    sql += " WHERE ";
                }
                sql += " (datainserimento BETWEEN ? AND ?) ";
            }
            sql += " ORDER BY datainserimento DESC ";*/
            String sql
                    = " SELECT *"
                    + " FROM utente U INNER JOIN ordine O"
                    + "     ON U.idutente = O.idutente"
                    + " INNER JOIN spedizione S"
                    + "     ON S.idordine = O.idordine"
                    + " INNER JOIN pagamento P"
                    + "     ON P.idordine = O.idordine"
                    + " INNER JOIN indirizzi I"
                    + "     ON I.idindirizzo = O.idindirizzo";
            if (!statoOrdine.equals("tutti")) {
                sql += " WHERE O.statoordine = ? ";
            }
            if (!data1.isEmpty() && !data2.isEmpty()) {
                if (!statoOrdine.equals("tutti")) {
                    sql += " AND ";
                } else {
                    sql += " WHERE ";
                }
                sql += " (O.datainserimento BETWEEN ? AND ?) ";
            }
            sql += " ORDER BY O.datainserimento DESC ";

            ps = conn.prepareStatement(sql);
            int i = 1;
            if (!statoOrdine.equals("tutti")) {
                ps.setString(i++, statoOrdine);
            }

            if (!data1.isEmpty() && !data2.isEmpty()) {
                ps.setString(i++, data1);
                ps.setString(i++, data2);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Ordine ordine;
                ordine = OrdineSQL.read(rs);

                Utente utente;
                utente = UtenteSQL.read(rs);

                Indirizzi indirizzo;
                indirizzo = IndirizziSQL.read(rs);

                Spedizione spedizione;
                spedizione = SpedizioneSQL.read(rs);

                Pagamento pagamento;
                pagamento = PagamentoSQL.read(rs);

                ordine.setUtente(utente);
                ordine.setIndirizzo(indirizzo);
                ordine.setSpedizione(spedizione);
                ordine.setPagamento(pagamento);

                listaOrdini.add(ordine);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaOrdini;
    }

    @Override
    public Ordine trovaOrdine(Long idOrdine, Long idUtente) {
        PreparedStatement ps;
        Ordine ordine = new Ordine();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM utente NATURAL JOIN ordine "
                    + " NATURAL JOIN spedizione NATURAL JOIN pagamento "
                    + " NATURAL JOIN indirizzi NATURAL JOIN composizione NATURAL JOIN prodotto"
                    + " WHERE idordine = ? AND idutente = ? ";*/
            String sql
                    = " SELECT *"
                    + " FROM utente U INNER JOIN ordine O"
                    + "     ON O.idutente = U.idutente"
                    + " INNER JOIN spedizione S"
                    + "     ON S.idordine = O.idordine"
                    + " INNER JOIN pagamento P"
                    + "     ON P.idordine = O.idordine"
                    + " INNER JOIN indirizzi I"
                    + "     ON I.idindirizzo = O.idindirizzo"
                    + " INNER JOIN composizione C"
                    + "     ON C.idordine = O.idordine"
                    + " INNER JOIN prodotto PR"
                    + "     ON PR.idprodotto = C.idprodotto"
                    + " WHERE O.idordine = ? AND U.idutente = ? ";

            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int i = 1;
            ps.setLong(i++, idOrdine);
            ps.setLong(i++, idUtente);

            ResultSet rs = ps.executeQuery();

            List<Composizione> listaComposizione = new ArrayList<>();

            if (rs.next()) {
                ordine = OrdineSQL.read(rs);

                Utente utente;
                utente = UtenteSQL.read(rs);

                Indirizzi indirizzo;
                indirizzo = IndirizziSQL.read(rs);

                Spedizione spedizione;
                spedizione = SpedizioneSQL.read(rs);

                Pagamento pagamento;
                pagamento = PagamentoSQL.read(rs);

                ordine.setUtente(utente);
                ordine.setIndirizzo(indirizzo);
                ordine.setSpedizione(spedizione);
                ordine.setPagamento(pagamento);

                rs.previous();

                while (rs.next()) {
                    Composizione composizione;
                    composizione = OrdineSQL.readComposizione(rs);

                    Prodotto prodotto;
                    prodotto = ProdottoSQL.read(rs);

                    composizione.setProdotto(prodotto);

                    listaComposizione.add(composizione);
                }
                ordine.setComposizione(listaComposizione.toArray(new Composizione[listaComposizione.size()]));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordine;
    }

    public static Ordine read(ResultSet rs) {

        Ordine ordine = new Ordine();
        Utente utente = new Utente();
        Indirizzi indOrd = new Indirizzi();

        ordine.setUtente(utente);
        ordine.setIndirizzo(indOrd);

        try {
            ordine.setIdOrdine(rs.getLong("idordine"));
        } catch (SQLException e) {
        }
        try {
            ordine.setDataInserimento(rs.getDate("datainserimento"));
        } catch (SQLException e) {
        }
        try {
            ordine.setStatoOrdine(rs.getString("statoordine"));
        } catch (SQLException e) {
        }
        try {
            ordine.getUtente().setIdUtente(rs.getLong("idutente"));
        } catch (SQLException e) {
        }
        try {
            ordine.getIndirizzo().setIdIndirizzo(rs.getLong("idindirizzo"));
        } catch (SQLException e) {
        }
        try {
            ordine.getIndirizzo().setAlias(rs.getString("alias"));
        } catch (SQLException e) {
        }

        return ordine;
    }

    public static Composizione readComposizione(ResultSet rs) {

        Composizione composizione = new Composizione();

        try {
            composizione.setQuantita(rs.getInt("quantita"));
        } catch (SQLException e) {
        }
        try {
            composizione.setTagliaScelta(rs.getString("tagliascelta"));
        } catch (SQLException e) {
        }
        try {
            composizione.setPrezzoPagato(rs.getBigDecimal("prezzopagato"));
        } catch (SQLException e) {
        }

        return composizione;
    }

    @Override
    public void elimina(Ordine ordine) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

}
