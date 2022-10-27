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
import model.dao.CategoriaDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Categoria;

/**
 *
 * @author Stefano
 */
public class CategoriaSQL implements CategoriaDAO {

    private final String COUNTER_ID = "IDCATEGORIA";
    Connection conn;

    public CategoriaSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(
            Long idCatalogo,
            String nomeCategoria,
            String descrCategoria,
            String immCategoria) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM categoria"
                    + " WHERE nomecategoria = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeCategoria);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome della categoria esiste gia'");
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

            Long idCategoria = rs.getLong("counter_value");
            rs.close();

            sql
                    = " INSERT INTO categoria ("
                    + " idcategoria, "
                    + " nomecategoria, "
                    + " descrcategoria, "
                    + " immcategoria, "
                    + " categoria_eliminato)"
                    + " VALUES (?,?,?,?, 'N')";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, idCategoria);
            ps.setString(i++, nomeCategoria);
            ps.setString(i++, descrCategoria);
            ps.setString(i++, immCategoria);

            ps.executeUpdate();

            sql
                    = " INSERT INTO tipocategoria ("
                    + " idcategoria, "
                    + " idcatalogo)"
                    + " VALUES (?,?)";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idCategoria);
            ps.setLong(i++, idCatalogo);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiorna(Categoria categoria) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT idcategoria "
                    + " FROM categoria "
                    + " WHERE nomecategoria = ? AND "
                    + " idcategoria <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, categoria.getNomeCategoria());
            ps.setLong(i++, categoria.getIdCategoria());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome della categoria esiste gia'");
            }

            sql
                    = " UPDATE categoria "
                    + " SET nomecategoria = ?, "
                    + " descrcategoria = ?, "
                    + " immcategoria = ? "
                    + " WHERE idcategoria = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, categoria.getNomeCategoria());
            ps.setString(i++, categoria.getDescrCategoria());
            ps.setString(i++, categoria.getImmCategoria());
            ps.setLong(i++, categoria.getIdCategoria());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Categoria categoria) {
        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE categoria "
                    + " SET categoria_eliminato='S' "
                    + " WHERE "
                    + " idcategoria = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, categoria.getIdCategoria());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sblocca(Categoria categoria) {
        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE categoria "
                    + " SET categoria_eliminato='N' "
                    + " WHERE "
                    + " idcategoria = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, categoria.getIdCategoria());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria trovaPerId(Long idCategoria) {
        PreparedStatement ps;
        Categoria categoria = new Categoria();

        try {

            String sql
                    = " SELECT * "
                    + "   FROM categoria "
                    + " WHERE "
                    + "   idcategoria = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idCategoria);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoria = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categoria;
    }

    public static Categoria read(ResultSet rs) {
        Categoria categoria = new Categoria();

        try {
            categoria.setIdCategoria(rs.getLong("idcategoria"));
        } catch (SQLException e) {
        }
        try {
            categoria.setNomeCategoria(rs.getString("nomecategoria"));
        } catch (SQLException e) {
        }
        try {
            categoria.setDescrCategoria(rs.getString("descrcategoria"));
        } catch (SQLException e) {
        }
        try {
            categoria.setImmCategoria(rs.getString("immcategoria"));
        } catch (SQLException e) {
        }
        try {
            categoria.setEliminato(rs.getString("categoria_eliminato").equals("S"));
        } catch (SQLException e) {
        }

        return categoria;
    }

}
