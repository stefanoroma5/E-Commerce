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
import model.dao.SpedizioneDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Ordine;
import model.mo.Spedizione;

/**
 *
 * @author Stefano
 */
public class SpedizioneSQL implements SpedizioneDAO {

    private final String COUNTER_ID = "IDSPEDIZIONE";
    Connection conn;

    public SpedizioneSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(
            String metodoSpediz,
            BigDecimal speseSpediz,
            Date dataSpediz,
            Long idOrdine) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM spedizione"
                    + " WHERE metodospedizione = ? AND"
                    + " spesespedizione = ? AND"
                    + " idordine = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, metodoSpediz);
            ps.setBigDecimal(i++, speseSpediz);
            ps.setLong(i++, idOrdine);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Questa spedizione esiste gia'");
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

            Long idSpedizione = rs.getLong("counter_value");

            rs.close();

            sql
                    = " INSERT INTO spedizione"
                    + "   ( idspedizione,";
            if (dataSpediz != null) {
                sql += "dataspedizione,";
            }
            sql += "metodospedizione,"
                    + "     spesespedizione,"
                    + "     idordine"
                    + "   )"
                    + " VALUES (?,";
            if (dataSpediz != null) {
                sql += "?,";
            }
            sql += "?,?,?)";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idSpedizione);
            if (dataSpediz != null) {
                ps.setDate(i++, dataSpediz);
            }
            ps.setString(i++, metodoSpediz);
            ps.setBigDecimal(i++, speseSpediz);
            ps.setLong(i++, idOrdine);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void aggiorna(Spedizione spediz) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT idspedizione "
                    + " FROM spedizione "
                    + " WHERE "
                    + " dataspedizione = ? AND"
                    + " metodospedizione = ? AND"
                    + " spesespedizione = ? AND"
                    + " idordine = ? AND"
                    + " idspedizione <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, spediz.getDataSpedizione());
            ps.setString(i++, spediz.getMetodoSpedizione());
            ps.setBigDecimal(i++, spediz.getSpeseSpedizione());
            ps.setLong(i++, spediz.getOrdine().getIdOrdine());
            ps.setLong(i++, spediz.getIdSpedizione());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Spedizione gia' esistente");
            }

            sql
                    = " UPDATE spedizione "
                    + " SET "
                    + "   dataspedizione = ?, "
                    + "   metodospedizione = ?, "
                    + "   spesespedizione = ? "
                    + " WHERE "
                    + "   idspedizione = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setDate(i++, spediz.getDataSpedizione());
            ps.setString(i++, spediz.getMetodoSpedizione());
            ps.setBigDecimal(i++, spediz.getSpeseSpedizione());
            ps.setLong(i++, spediz.getIdSpedizione());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Spedizione spediz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Spedizione read(ResultSet rs) {

        Spedizione sped = new Spedizione();
        Ordine ordine = new Ordine();
        sped.setOrdine(ordine);

        try {
            sped.setIdSpedizione(rs.getLong("idspedizione"));
        } catch (SQLException e) {
        }
        try {
            sped.setDataSpedizione(rs.getDate("dataspedizione"));
        } catch (SQLException e) {
        }
        try {
            sped.setMetodoSpedizione(rs.getString("metodospedizione"));
        } catch (SQLException e) {
        }
        try {
            sped.setSpeseSpedizione(rs.getBigDecimal("spesespedizione"));
        } catch (SQLException e) {
        }
        try {
            sped.getOrdine().setIdOrdine(rs.getLong("idordine"));
        } catch (SQLException e) {
        }

        return sped;
    }

}
