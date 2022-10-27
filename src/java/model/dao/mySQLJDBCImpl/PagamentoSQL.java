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
import model.dao.PagamentoDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Ordine;
import model.mo.Pagamento;

/**
 *
 * @author Stefano
 */
public class PagamentoSQL implements PagamentoDAO {

    private final String COUNTER_ID = "IDPAGAMENTO";
    Connection conn;

    public PagamentoSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(
            String metodoPagam,
            BigDecimal importo,
            Date dataPagam,
            Ordine ordine) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM pagamento"
                    + " WHERE metodopagamento = ? AND"
                    + " datapagamento = ? AND"
                    + " importo = ? AND"
                    + " idordine = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, metodoPagam);
            ps.setDate(i++, dataPagam);
            ps.setBigDecimal(i++, importo);
            ps.setLong(i++, ordine.getIdOrdine());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Questo pagamento esiste gia'");
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

            Long idPagamento = rs.getLong("counter_value");

            rs.close();

            sql
                    = " INSERT INTO pagamento"
                    + "   ( idpagamento,"
                    + "     datapagamento,"
                    + "     metodopagamento,"
                    + "     importo,"
                    + "     idordine"
                    + "   )"
                    + " VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idPagamento);
            ps.setDate(i++, dataPagam);
            ps.setString(i++, metodoPagam);
            ps.setBigDecimal(i++, importo);
            ps.setLong(i++, ordine.getIdOrdine());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiorna(Pagamento pagam) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT idpagamento "
                    + " FROM pagamento "
                    + " WHERE "
                    + " datapagamento = ? AND"
                    + " metodopagamento = ? AND"
                    + " importo = ? AND"
                    + " idordine = ? AND"
                    + " idpagamento <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, pagam.getDataPagamento());
            ps.setString(i++, pagam.getMetodoPagamento());
            ps.setBigDecimal(i++, pagam.getImporto());
            ps.setLong(i++, pagam.getOrdine().getIdOrdine());
            ps.setLong(i++, pagam.getIdPagamento());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Pagamento gia' esistente");
            }

            sql
                    = " UPDATE pagamento "
                    + " SET "
                    + "   datapagamento = ?, "
                    + "   metodopagamento = ?, "
                    + "   importo = ? "
                    + " WHERE "
                    + "   idpagamento = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setDate(i++, pagam.getDataPagamento());
            ps.setString(i++, pagam.getMetodoPagamento());
            ps.setBigDecimal(i++, pagam.getImporto());
            ps.setLong(i++, pagam.getIdPagamento());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Pagamento pagam) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Pagamento read(ResultSet rs) {

        Pagamento pagam = new Pagamento();
        Ordine ordine = new Ordine();
        pagam.setOrdine(ordine);

        try {
            pagam.setIdPagamento(rs.getLong("idpagamento"));
        } catch (SQLException e) {
        }
        try {
            pagam.setDataPagamento(rs.getDate("datapagamento"));
        } catch (SQLException e) {
        }
        try {
            pagam.setMetodoPagamento(rs.getString("metodopagamento"));
        } catch (SQLException e) {
        }
        try {
            pagam.setImporto(rs.getBigDecimal("importo"));
        } catch (SQLException e) {
        }
        try {
            pagam.getOrdine().setIdOrdine(rs.getLong("idordine"));
        } catch (SQLException e) {
        }

        return pagam;
    }

}
