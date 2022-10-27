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
import model.dao.CatalogoDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Catalogo;
import model.mo.Categoria;

/**
 *
 * @author Stefano
 */
public class CatalogoSQL implements CatalogoDAO {

    private final String COUNTER_ID = "IDCATALOGO";
    Connection conn;

    public CatalogoSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(String nomeCatalogo, String descrCatalogo, String immCatalogo) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM CATALOGO"
                    + " WHERE NOMECATALOGO = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeCatalogo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome del catalogo esiste gia'");
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

            Long idCatalogo = rs.getLong("COUNTER_VALUE");
            rs.close();

            sql
                    = " INSERT INTO CATALOGO ("
                    + " IDCATALOGO, "
                    + " NOMECATALOGO, "
                    + " DESCRCATALOGO, "
                    + " IMMCATALOGO, "
                    + " CATALOGO_ELIMINATO)"
                    + " VALUES (?,?,?,?, 'N')";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, idCatalogo);
            ps.setString(i++, nomeCatalogo);
            ps.setString(i++, descrCatalogo);
            ps.setString(i++, immCatalogo);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiorna(Catalogo catalogo) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT IDCATALOGO "
                    + " FROM CATALOGO "
                    + " WHERE NOMECATALOGO = ? AND "
                    + " IDCATALOGO <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, catalogo.getNomeCatalogo());
            ps.setLong(i++, catalogo.getIdCatalogo());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Il nome del catalogo esiste gia'");
            }

            sql
                    = " UPDATE CATALOGO "
                    + " SET NOMECATALOGO = ?, "
                    + " DESCRCATALOGO = ?, "
                    + " IMMCATALOGO = ? "
                    + " WHERE IDCATALOGO = ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, catalogo.getNomeCatalogo());
            ps.setString(i++, catalogo.getDescrCatalogo());
            ps.setString(i++, catalogo.getImmCatalogo());
            ps.setLong(i++, catalogo.getIdCatalogo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Catalogo catalogo) {
        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE CATALOGO "
                    + " SET CATALOGO_ELIMINATO='S' "
                    + " WHERE "
                    + " IDCATALOGO = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, catalogo.getIdCatalogo());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sblocca(Catalogo catalogo) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE CATALOGO "
                    + " SET CATALOGO_ELIMINATO='N' "
                    + " WHERE "
                    + " IDCATALOGO = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, catalogo.getIdCatalogo());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Catalogo trovaPerId(Long idCatalogo) {
        PreparedStatement ps;
        Catalogo catalogo = new Catalogo();

        try {

            String sql
                    = " SELECT * "
                    + "   FROM CATALOGO "
                    + " WHERE "
                    + "   IDCATALOGO = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idCatalogo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                catalogo = read(rs);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return catalogo;
    }

    @Override
    public List<Catalogo> trovaCataloghi() {

        PreparedStatement ps;

        List<Catalogo> listaCataloghi = new ArrayList<>();
        List<Categoria> listaCategorie = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM categoria NATURAL JOIN tipocategoria NATURAL JOIN catalogo"
                    + " WHERE categoria_eliminato = 'N'"
                    + " AND catalogo_eliminato = 'N'"
                    + " ORDER BY idcatalogo, idcategoria";*/
            String sql
                    = " SELECT *"
                    + " FROM CATEGORIA CA INNER JOIN TIPOCATEGORIA T"
                    + " ON CA.IDCATEGORIA = T.IDCATEGORIA"
                    + " INNER JOIN CATALOGO CO"
                    + " ON CO.IDCATALOGO = T.IDCATALOGO"
                    + " WHERE CATEGORIA_ELIMINATO = 'N'"
                    + " AND CATALOGO_ELIMINATO = 'N'"
                    + " ORDER BY T.IDCATALOGO, T.IDCATEGORIA";
            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Catalogo catalogo;
                catalogo = read(rs);

                rs.previous();

                while (rs.next()) {
                    if (rs.isLast()) {
                        Categoria categoria;
                        categoria = CategoriaSQL.read(rs);
                        listaCategorie.add(categoria);
                    }
                    if ((!catalogo.getIdCatalogo().equals(rs.getLong("IDCATALOGO"))) || rs.isLast()) {

                        catalogo.setCategorie(listaCategorie.toArray(new Categoria[listaCategorie.size()]));
                        listaCataloghi.add(catalogo);

                        catalogo = read(rs);
                        listaCategorie.clear();
                    }
                    if (!rs.isLast()) {
                        Categoria categoria;
                        categoria = CategoriaSQL.read(rs);
                        listaCategorie.add(categoria);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCataloghi;
    }

    @Override
    public List<Catalogo> trovaTuttiICataloghi() {

        PreparedStatement ps;

        List<Catalogo> listaCataloghi = new ArrayList<>();
        List<Categoria> listaCategorie = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM CATEGORIA NATURAL JOIN TIPOCATEGORIA NATURAL JOIN CATALOGO"
                    + " ORDER BY IDCATALOGO, IDCATEGORIA";*/
            String sql
                    = " SELECT *"
                    + " FROM CATEGORIA CA INNER JOIN TIPOCATEGORIA T"
                    + " ON CA.IDCATEGORIA = T.IDCATEGORIA"
                    + " INNER JOIN CATALOGO CO"
                    + " ON CO.IDCATALOGO = T.IDCATALOGO"
                    + " ORDER BY T.IDCATALOGO, T.IDCATEGORIA";
            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Catalogo catalogo;
                catalogo = read(rs);

                rs.previous();

                while (rs.next()) {
                    if (rs.isLast()) {
                        Categoria categoria;
                        categoria = CategoriaSQL.read(rs);
                        listaCategorie.add(categoria);
                    }
                    if ((!catalogo.getIdCatalogo().equals(rs.getLong("IDCATALOGO"))) || rs.isLast()) {

                        catalogo.setCategorie(listaCategorie.toArray(new Categoria[listaCategorie.size()]));
                        listaCataloghi.add(catalogo);

                        catalogo = read(rs);
                        listaCategorie.clear();
                    }
                    if (!rs.isLast()) {
                        Categoria categoria;
                        categoria = CategoriaSQL.read(rs);
                        listaCategorie.add(categoria);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCataloghi;
    }

    @Override
    public List<Catalogo> trovaNomiCataloghi() {

        PreparedStatement ps;

        List<Catalogo> listaCataloghi = new ArrayList<>();

        try {
            String sql
                    = " SELECT *"
                    + " FROM CATALOGO";
            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Catalogo catalogo;
                catalogo = read(rs);
                listaCataloghi.add(catalogo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCataloghi;
    }

    public static Catalogo read(ResultSet rs) {
        Catalogo catalogo = new Catalogo();

        try {
            catalogo.setIdCatalogo(rs.getLong("IDCATALOGO"));
        } catch (SQLException e) {
        }
        try {
            catalogo.setNomeCatalogo(rs.getString("NOMECATALOGO"));
        } catch (SQLException e) {
        }
        try {
            catalogo.setDescrCatalogo(rs.getString("DESCRCATALOGO"));
        } catch (SQLException e) {
        }
        try {
            catalogo.setImmCatalogo(rs.getString("IMMCATALOGO"));
        } catch (SQLException e) {
        }
        try {
            catalogo.setEliminato(rs.getString("CATALOGO_ELIMINATO").equals("S"));
        } catch (SQLException e) {
        }

        return catalogo;
    }

}
