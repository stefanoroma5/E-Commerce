/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.mySQLJDBCImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.ProdottoDAO;
import model.dao.exception.DuplicatedObjectException;
import model.dao.exception.ImmagineGiaEsistenteException;
import model.dao.exception.TagliaGiaEsistenteException;
import model.mo.Categoria;
import model.mo.ImmGalleria;
import model.mo.Prodotto;
import model.mo.Taglie;
import model.mo.Vetrina;

/**
 *
 * @author Stefano
 */
public class ProdottoSQL implements ProdottoDAO {

    private final String COUNTER_ID = "IDPRODOTTO";
    Connection conn;

    public ProdottoSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisci(
            String nomeProdotto,
            String descrProdotto,
            String marca,
            BigDecimal prezzo,
            String colore,
            String immAnteprima,
            Long idCategoria,
            String taglia,
            int disponibilita,
            String immGalleria,
            Long idVetrina) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT *"
                    + " FROM prodotto"
                    + " WHERE prod_eliminato = 'N' AND"
                    + " nomeprodotto = ? AND"
                    + " marca = ? AND"
                    + " colore = ? AND"
                    + " immanteprima = ? AND"
                    + " idcategoria = ? AND"
                    + " idvetrina = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, nomeProdotto);
            ps.setString(i++, marca);
            ps.setString(i++, colore);
            ps.setString(i++, immAnteprima);
            ps.setLong(i++, idCategoria);
            ps.setLong(i++, idVetrina);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Questo prodotto esiste gia'");
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
                    + "WHERE counter_id ='" + COUNTER_ID + "'";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();

            Long idProdotto = rs.getLong("counter_value");

            rs.close();

            sql
                    = " INSERT INTO prodotto"
                    + "   ( idprodotto,"
                    + "     nomeprodotto,"
                    + "     descrprod,"
                    + "     marca,"
                    + "     prezzo,"
                    + "     colore,"
                    + "     immanteprima,"
                    + "     idcategoria,"
                    + "     idvetrina,"
                    + "     prod_eliminato"
                    + "   )"
                    + " VALUES (?,?,?,?,?,?,?,?,?, 'N')";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idProdotto);
            ps.setString(i++, nomeProdotto);
            ps.setString(i++, descrProdotto);
            ps.setString(i++, marca);
            ps.setBigDecimal(i++, prezzo);
            ps.setString(i++, colore);
            ps.setString(i++, immAnteprima);
            ps.setLong(i++, idCategoria);
            ps.setLong(i++, idVetrina);

            ps.executeUpdate();

            sql
                    = " INSERT INTO taglie"
                    + "   ( idprodotto,"
                    + "     taglia,"
                    + "     disponibilita"
                    + "   )"
                    + " VALUES (?,?,?)";
            ps = conn.prepareStatement(sql);

            i = 1;
            ps.setLong(i++, idProdotto);
            ps.setString(i++, taglia);
            ps.setInt(i++, disponibilita);

            ps.executeUpdate();

            sql
                    = " INSERT INTO immgalleria"
                    + "     ( idprodotto,"
                    + "       immagini"
                    + "     )"
                    + " VALUES (?,?)";

            ps = conn.prepareStatement(sql);

            int j = 1;
            ps.setLong(j++, idProdotto);
            ps.setString(j++, immGalleria);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void aggiorna(Prodotto prodotto, int nuovaDisp, String nuovaTaglia, String nuovaImm) throws DuplicatedObjectException, ImmagineGiaEsistenteException, TagliaGiaEsistenteException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT idprodotto "
                    + " FROM prodotto "
                    + " WHERE "
                    + " prod_eliminato ='N' AND "
                    + " nomeprodotto = ? AND"
                    + " marca = ? AND"
                    + " prezzo = ? AND"
                    + " colore = ? AND"
                    + " immanteprima = ? AND"
                    + " idcategoria = ? AND"
                    + " idprodotto <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, prodotto.getNomeProdotto());
            ps.setString(i++, prodotto.getMarca());
            ps.setBigDecimal(i++, prodotto.getPrezzo());
            ps.setString(i++, prodotto.getColore());
            ps.setString(i++, prodotto.getImmAnteprima());
            ps.setLong(i++, prodotto.getCategorie().getIdCategoria());
            ps.setLong(i++, prodotto.getIdProdotto());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatedObjectException("Prodotto gia' esistente");
            }

            sql
                    = " UPDATE prodotto "
                    + " SET "
                    + "   nomeprodotto = ?, "
                    + "   descrprod = ?, "
                    + "   marca = ?, "
                    + "   prezzo = ?, "
                    + "   colore = ?, "
                    + "   immanteprima = ?,"
                    + "   idcategoria = ?,"
                    + "   idvetrina = ? "
                    + " WHERE "
                    + "   idprodotto = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, prodotto.getNomeProdotto());
            ps.setString(i++, prodotto.getDescrProdotto());
            ps.setString(i++, prodotto.getMarca());
            ps.setBigDecimal(i++, prodotto.getPrezzo());
            ps.setString(i++, prodotto.getColore());
            ps.setString(i++, prodotto.getImmAnteprima());
            ps.setLong(i++, prodotto.getCategorie().getIdCategoria());
            ps.setLong(i++, prodotto.getVetrine().getIdVetrina());
            ps.setLong(i++, prodotto.getIdProdotto());
            ps.executeUpdate();

            /*aggiornamento taglie*/
            for (i = 0; i < prodotto.getTaglie().length; i++) {
                sql
                        = " UPDATE taglie"
                        + " SET "
                        + "  disponibilita = ?"
                        + " WHERE "
                        + "  taglia = ? AND"
                        + "  idprodotto = ?";
                ps = conn.prepareStatement(sql);
                int j = 1;
                ps.setInt(j++, prodotto.getTaglie(i).getDisponibilita());
                ps.setString(j++, prodotto.getTaglie(i).getTaglia());
                ps.setLong(j++, prodotto.getIdProdotto());

                ps.executeUpdate();
            }

            if (nuovaDisp != -1 && nuovaTaglia != null) {
                sql
                        = " SELECT taglia"
                        + " FROM taglie"
                        + " WHERE idprodotto = ? AND"
                        + " taglia = ?";
                ps = conn.prepareStatement(sql);
                int j = 1;
                ps.setLong(j++, prodotto.getIdProdotto());
                ps.setString(j++, nuovaTaglia);

                rs = ps.executeQuery();
                if (rs.next()) {
                    throw new TagliaGiaEsistenteException("Taglia gia' esistente");
                }

                sql
                        = " INSERT INTO taglie"
                        + " ( idprodotto, taglia, disponibilita)"
                        + " VALUES (?,?,?)";
                ps = conn.prepareStatement(sql);
                j = 1;
                ps.setLong(j++, prodotto.getIdProdotto());
                ps.setString(j++, nuovaTaglia);
                ps.setInt(j++, nuovaDisp);

                ps.executeUpdate();
            }

            sql
                    = " DELETE FROM immgalleria "
                    + " WHERE "
                    + " idprodotto= ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, prodotto.getIdProdotto());
            ps.executeUpdate();

            /*aggiornamento immagini galleria*/
            for (i = 0; i < prodotto.getImmGalleria().length; i++) {

                sql
                        = " INSERT INTO immgalleria"
                        + "     ( immagini,"
                        + "       idprodotto"
                        + "     )"
                        + " VALUES (?,?)";
                ps = conn.prepareStatement(sql);
                int j = 1;
                ps.setString(j++, prodotto.getImmGalleria(i).getImmagine());
                ps.setLong(j++, prodotto.getIdProdotto());
                ps.executeUpdate();
            }

            if (nuovaImm != null) {

                sql
                        = " SELECT immagini"
                        + " FROM immgalleria"
                        + " WHERE idprodotto = ? AND"
                        + " immagini = ?";
                ps = conn.prepareStatement(sql);
                int j = 1;
                ps.setLong(j++, prodotto.getIdProdotto());
                ps.setString(j++, nuovaImm);

                rs = ps.executeQuery();
                if (rs.next()) {
                    throw new ImmagineGiaEsistenteException("Immagine gia' esistente");
                }

                sql
                        = " INSERT INTO immgalleria"
                        + "     ( immagini,"
                        + "       idprodotto"
                        + "     )"
                        + " VALUES (?,?)";
                ps = conn.prepareStatement(sql);
                j = 1;
                ps.setString(j++, nuovaImm);
                ps.setLong(j++, prodotto.getIdProdotto());
                ps.executeUpdate();
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void elimina(Long idProdotto) {

        PreparedStatement ps;

        try {

            String sql
                    = " UPDATE prodotto "
                    + " SET prod_eliminato = 'S' "
                    + " WHERE "
                    + " idprodotto = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, idProdotto);
            ps.executeUpdate();

            sql
                    = " DELETE FROM immgalleria "
                    + " WHERE "
                    + " idprodotto= ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idProdotto);
            ps.executeUpdate();

            sql
                    = " DELETE FROM taglie "
                    + " WHERE "
                    + " idprodotto= ?";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, idProdotto);
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Prodotto> trovaProdottiFiltrati(
            String nomeCatalogo,
            String nomeCategoria,
            List<String> marche,
            List<String> taglie,
            String prezzo) {

        PreparedStatement ps;

        List<Prodotto> listaProdotti = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM  prodotto"
                    + " WHERE  prod_eliminato ='N'";

            if ((nomeCatalogo != null) && (!nomeCatalogo.equals("null"))) {
                sql += " AND idcategoria IN "
                        + "(SELECT idcategoria"
                        + "   FROM categoria NATURAL JOIN catalogo NATURAL JOIN tipocategoria"
                        + "   WHERE nomecatalogo = ? "
                        + "   AND catalogo_eliminato = 'N' "
                        + "   AND categoria_eliminato = 'N' ";

                if ((nomeCategoria != null) && (!nomeCategoria.equals("null"))) {
                    sql += " AND nomecategoria = ?)";
                } else {
                    sql += ")";
                }
            }

            if (marche != null && !marche.isEmpty()) {
                sql += " AND (marca = ?";
                //se marche non è vuoto c'è almeno un elemento quindi scelgo il primo
                if (marche.size() > 1) {
                    //se ne contiene più di 1 faccio un for partendo dal secondo elemento(i=1)
                    for (int i = 1; i < marche.size(); i++) {
                        //in cui aggiungo condizioni
                        sql += " OR marca = ?";
                    }
                }
                sql += ")";
            }

            sql += " AND idprodotto IN("
                    + "    SELECT"
                    + "        idprodotto"
                    + "    FROM"
                    + "        taglie"
                    + "    WHERE disponibilita > 0 ";
            if (taglie != null && !taglie.isEmpty()) {
                sql += "AND   (taglia = ?";
                //se taglie non è vuoto c'è almeno un elemento quindi scelgo il primo
                if (taglie.size() > 1) {
                    //se ne contiene più di 1 faccio un for partendo dal secondo elemento(i=1)
                    for (int i = 1; i < taglie.size(); i++) {
                        //in cui aggiungo condizioni
                        sql += " OR taglia = ?";
                    }
                }
                sql += ")";
            }
            sql += ")";

            if ((prezzo != null) && (!prezzo.equals("null"))) {
                sql += " AND (prezzo";
                if (prezzo.equals("meno50")) {
                    sql += " < 50) ";
                } else if (prezzo.equals("tra50e100")) {
                    sql += " BETWEEN 50 AND 100) ";
                } else if (prezzo.equals("tra100e150")) {
                    sql += " BETWEEN 100 AND 150) ";
                } else if (prezzo.equals("piu150")) {
                    sql += " > 150) ";
                }
            }

            sql += " ORDER BY idprodotto";*/
            String sql
                    = " SELECT *"
                    + " FROM  prodotto P"
                    + " WHERE  prod_eliminato ='N'";

            if ((nomeCatalogo != null) && (!nomeCatalogo.equals("null"))) {
                sql += " AND P.idcategoria IN "
                        + "(SELECT CA.idcategoria"
                        + "   FROM categoria CA INNER JOIN tipocategoria T"
                        + "     ON CA.idcategoria = T.idcategoria"
                        + "   INNER JOIN catalogo CO"
                        + "     ON CO.idcatalogo = T.idcatalogo"
                        + "   WHERE CO.nomecatalogo = ? "
                        + "   AND catalogo_eliminato = 'N' "
                        + "   AND categoria_eliminato = 'N' ";

                if ((nomeCategoria != null) && (!nomeCategoria.equals("null"))) {
                    sql += " AND nomecategoria = ?)";
                } else {
                    sql += ")";
                }
            }

            if (marche != null && !marche.isEmpty()) {
                sql += " AND (marca = ?";
                //se marche non è vuoto c'è almeno un elemento quindi scelgo il primo
                if (marche.size() > 1) {
                    //se ne contiene più di 1 faccio un for partendo dal secondo elemento(i=1)
                    for (int i = 1; i < marche.size(); i++) {
                        //in cui aggiungo condizioni
                        sql += " OR marca = ?";
                    }
                }
                sql += ")";
            }

            sql += " AND idprodotto IN("
                    + "    SELECT"
                    + "        idprodotto"
                    + "    FROM"
                    + "        taglie"
                    + "    WHERE disponibilita > 0 ";
            if (taglie != null && !taglie.isEmpty()) {
                sql += "AND   (taglia = ?";
                //se taglie non è vuoto c'è almeno un elemento quindi scelgo il primo
                if (taglie.size() > 1) {
                    //se ne contiene più di 1 faccio un for partendo dal secondo elemento(i=1)
                    for (int i = 1; i < taglie.size(); i++) {
                        //in cui aggiungo condizioni
                        sql += " OR taglia = ?";
                    }
                }
                sql += ")";
            }
            sql += ")";

            if ((prezzo != null) && (!prezzo.equals("null"))) {
                sql += " AND (prezzo";
                if (prezzo.equals("meno50")) {
                    sql += " < 50) ";
                } else if (prezzo.equals("tra50e100")) {
                    sql += " BETWEEN 50 AND 100) ";
                } else if (prezzo.equals("tra100e150")) {
                    sql += " BETWEEN 100 AND 150) ";
                } else if (prezzo.equals("piu150")) {
                    sql += " > 150) ";
                }
            }

            sql += " ORDER BY idprodotto";
            

            ps = conn.prepareStatement(sql);
            int i = 1;

            if ((nomeCatalogo != null) && (!nomeCatalogo.equals("null"))) {
                ps.setString(i++, nomeCatalogo);
            }

            if ((nomeCategoria != null) && (!nomeCategoria.equals("null"))) {
                ps.setString(i++, nomeCategoria);
            }

            if (marche != null && !marche.isEmpty()) {
                ps.setString(i++, marche.get(0));
                if (marche.size() > 1) {
                    for (int j = 1; j < marche.size(); j++) {
                        ps.setString(i++, marche.get(j));
                    }
                }
            }

            if (taglie != null && !taglie.isEmpty()) {
                ps.setString(i++, taglie.get(0));
                if (taglie.size() > 1) {
                    for (int j = 1; j < taglie.size(); j++) {
                        ps.setString(i++, taglie.get(j));
                    }
                }
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Prodotto prodotto;
                prodotto = ProdottoSQL.read(rs);
                listaProdotti.add(prodotto);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaProdotti;
    }

    @Override
    public List<Prodotto> trovaProdotti(String nomeCatalogo) {
        PreparedStatement ps;

        List<Prodotto> listaProdotti = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM  prodotto"
                    + " WHERE prod_eliminato = 'N'";

            if (nomeCatalogo != null) {
                sql += " AND idcategoria IN "
                        + "(SELECT idcategoria";
                sql += "   FROM categoria NATURAL JOIN catalogo NATURAL JOIN tipocategoria";
                sql += "   WHERE nomecatalogo = ?)";
            }

            sql += " ORDER BY idprodotto";*/
            String sql
                    = " SELECT *"
                    + " FROM  prodotto P"
                    + " WHERE prod_eliminato = 'N'";

            if (nomeCatalogo != null) {
                sql += " AND P.idcategoria IN "
                        + "(SELECT CA.idcategoria"
                        + " FROM categoria CA INNER JOIN tipocategoria T"
                        + "     ON CA.idcategoria = T.idcategoria"
                        + " INNER JOIN catalogo CO"
                        + "     ON CO.idcatalogo = T.idcatalogo"
                        + " WHERE nomecatalogo = ?)";
            }

            sql += " ORDER BY idprodotto";

            ps = conn.prepareStatement(sql);
            int i = 1;

            if (nomeCatalogo != null) {
                ps.setString(i++, nomeCatalogo);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prodotto prodotto;
                prodotto = ProdottoSQL.read(rs);
                listaProdotti.add(prodotto);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaProdotti;
    }

    @Override
    public Prodotto trovaInfoProdotto(Long idProdotto) {

        PreparedStatement ps;

        Prodotto prodotto = null;

        try {
            /*String sql
                    = " SELECT *"
                    + " FROM  prodotto NATURAL JOIN taglie NATURAL JOIN immgalleria"
                    + " WHERE  prod_eliminato ='N' AND idprodotto = ? "
                    + " ORDER BY taglia ";*/
            String sql
                    = " SELECT *"
                    + " FROM  prodotto P INNER JOIN taglie T"
                    + "     ON P.idprodotto = T.idprodotto"
                    + " INNER JOIN immgalleria I"
                    + "     ON I.idprodotto = P.idprodotto"
                    + " WHERE  prod_eliminato ='N' AND P.idprodotto = ? "
                    + " ORDER BY taglia ";

            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setLong(1, idProdotto);

            ResultSet rs = ps.executeQuery();

            List<Taglie> listaTaglie = new ArrayList<>();
            List<ImmGalleria> listaImmagini = new ArrayList<>();

            if (rs.next()) {
                prodotto = read(rs);

                Taglie taglia;
                taglia = readTaglia(rs);
                listaTaglie.add(taglia);

                rs.previous();

                boolean changed = false;

                while (rs.next()) {
                    if (taglia.getTaglia().equals(rs.getString("taglia"))) {
                        if (!changed) {
                            ImmGalleria immGalleria;
                            immGalleria = readImmGalleria(rs);
                            listaImmagini.add(immGalleria);
                        }
                    } else {
                        taglia = readTaglia(rs);
                        listaTaglie.add(taglia);

                        changed = true;
                    }
                }
                prodotto.setTaglie(listaTaglie.toArray(new Taglie[listaTaglie.size()]));
                prodotto.setImmGalleria(listaImmagini.toArray(new ImmGalleria[listaImmagini.size()]));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prodotto;
    }

    @Override
    public List<String> trovaMarcheProdotti(String nomeCatalogo, String nomeCategoria
    ) {
        PreparedStatement ps;

        List<String> listaMarche = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT DISTINCT marca"
                    + " FROM  prodotto NATURAL JOIN taglie NATURAL JOIN catalogo"
                    + " NATURAL JOIN categoria NATURAL JOIN tipocategoria"
                    + " WHERE  prod_eliminato ='N'"
                    + " AND catalogo_eliminato = 'N'"
                    + " AND categoria_eliminato = 'N'"
                    + " AND disponibilita > 0"
                    + " AND nomecatalogo = ? ";
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                sql += " AND nomecategoria = ?";
            }
            sql += " ORDER BY marca";*/
            String sql
                    = " SELECT DISTINCT marca"
                    + " FROM prodotto P INNER JOIN taglie T"
                    + "     ON P.idprodotto = T.idprodotto"
                    + " INNER JOIN categoria CA"
                    + "     ON CA.idcategoria = P.idcategoria"
                    + " INNER JOIN tipocategoria TC"
                    + "     ON CA.idcategoria = TC.idcategoria"
                    + " INNER JOIN catalogo CO"
                    + "     ON CO.idcatalogo = TC.idcatalogo"
                    + " WHERE  prod_eliminato ='N'"
                    + " AND catalogo_eliminato = 'N'"
                    + " AND categoria_eliminato = 'N'"
                    + " AND disponibilita > 0"
                    + " AND nomecatalogo = ? ";
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                sql += " AND nomecategoria = ?";
            }
            sql += " ORDER BY marca";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeCatalogo);
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                ps.setString(2, nomeCategoria);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaMarche.add(rs.getString("marca"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaMarche;
    }

    @Override
    public List<String> trovaTaglieProdotti(String nomeCatalogo, String nomeCategoria
    ) {
        PreparedStatement ps;

        List<String> listaTaglie = new ArrayList<>();

        try {
            /*String sql
                    = " SELECT DISTINCT taglia"
                    + " FROM  prodotto NATURAL JOIN taglie NATURAL JOIN catalogo"
                    + " NATURAL JOIN categoria NATURAL JOIN tipocategoria"
                    + " WHERE  prod_eliminato ='N'"
                    + " AND catalogo_eliminato = 'N'"
                    + " AND categoria_eliminato = 'N'"
                    + " AND disponibilita > 0"
                    + " AND nomecatalogo = ? ";
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                sql += " AND nomecategoria = ?";
            }
            sql += " ORDER BY taglia";*/
            String sql
                    = " SELECT DISTINCT taglia"
                    + " FROM prodotto P INNER JOIN taglie T"
                    + "     ON P.idprodotto = T.idprodotto"
                    + " INNER JOIN categoria CA"
                    + "     ON CA.idcategoria = P.idcategoria"
                    + " INNER JOIN tipocategoria TC"
                    + "     ON CA.idcategoria = TC.idcategoria"
                    + " INNER JOIN catalogo CO"
                    + "     ON CO.idcatalogo = TC.idcatalogo"
                    + " WHERE  prod_eliminato ='N'"
                    + " AND catalogo_eliminato = 'N'"
                    + " AND categoria_eliminato = 'N'"
                    + " AND disponibilita > 0"
                    + " AND nomecatalogo = ? ";
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                sql += " AND nomecategoria = ?";
            }
            sql += " ORDER BY taglia";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeCatalogo);
            if ((nomeCategoria != null) && !(nomeCategoria.equals("null"))) {
                ps.setString(2, nomeCategoria);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaTaglie.add(rs.getString("taglia"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaTaglie;
    }

    @Override
    public void eliminaImmagine(ImmGalleria immagine) {
        PreparedStatement ps;

        try {

            String sql
                    = " DELETE FROM immgalleria "
                    + " WHERE "
                    + " idprodotto= ? AND"
                    + " immagini = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, immagine.getProdotto().getIdProdotto());
            ps.setString(i++, immagine.getImmagine());
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminaTaglia(Taglie taglia) {
        PreparedStatement ps;

        try {

            String sql
                    = " DELETE FROM taglie "
                    + " WHERE "
                    + " idprodotto= ? AND"
                    + " taglia = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, taglia.getProdotto().getIdProdotto());
            ps.setString(i++, taglia.getTaglia());
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Prodotto read(ResultSet rs) {

        Prodotto prodotto = new Prodotto();
        Categoria categoria = new Categoria();
        Vetrina vetrina = new Vetrina();

        prodotto.setCategorie(categoria);
        prodotto.setVetrine(vetrina);

        try {
            prodotto.setIdProdotto(rs.getLong("idprodotto"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setNomeProdotto(rs.getString("nomeprodotto"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setDescrProdotto(rs.getString("descrprod"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setMarca(rs.getString("marca"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setPrezzo(rs.getBigDecimal("prezzo"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setColore(rs.getString("colore"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setImmAnteprima(rs.getString("immanteprima"));
        } catch (SQLException e) {
        }
        try {
            prodotto.getCategorie().setIdCategoria(rs.getLong("idcategoria"));
        } catch (SQLException e) {
        }
        try {
            prodotto.getVetrine().setIdVetrina(rs.getLong("idvetrina"));
        } catch (SQLException e) {
        }
        try {
            prodotto.setEliminato(rs.getString("prod_eliminato").equals("S"));
        } catch (SQLException e) {
        }

        return prodotto;
    }

    public static Taglie readTaglia(ResultSet rs) {
        Taglie taglia = new Taglie();
        Prodotto prodotto = new Prodotto();

        taglia.setProdotto(prodotto);

        try {
            taglia.getProdotto().setIdProdotto(rs.getLong("idprodotto"));
        } catch (SQLException e) {
        }
        try {
            taglia.setTaglia(rs.getString("taglia"));
        } catch (SQLException e) {
        }
        try {
            taglia.setDisponibilita(rs.getInt("disponibilita"));
        } catch (SQLException e) {
        }

        return taglia;
    }

    public static ImmGalleria readImmGalleria(ResultSet rs) {
        ImmGalleria immGalleria = new ImmGalleria();
        Prodotto prodotto = new Prodotto();

        immGalleria.setProdotto(prodotto);

        try {
            immGalleria.getProdotto().setIdProdotto(rs.getLong("idprodotto"));
        } catch (SQLException e) {
        }
        try {
            immGalleria.setImmagini(rs.getString("immagini"));
        } catch (SQLException e) {
        }

        return immGalleria;
    }

}
