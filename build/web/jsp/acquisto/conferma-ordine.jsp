<%@page import="model.mo.Catalogo"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.mo.Indirizzi"%>
<%@page session="false"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="java.util.List"%>
<%@page import="model.session.mo.UtenteLoggato"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    UtenteLoggato utenteLoggato = (UtenteLoggato) request.getAttribute("utenteLoggato");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    boolean esisteCarrello = (Boolean) request.getAttribute("esisteCarrello");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    int quantitaCarrello = (int) request.getAttribute("quantitaCarrello");

    BigDecimal totaleProdotti = (BigDecimal) (request.getAttribute("totaleProdotti"));
    String tipoSped = (String) (request.getAttribute("tipoSped"));
    BigDecimal speseSped = new BigDecimal(0);
    if (tipoSped.equals("Standard")) {
        speseSped = new BigDecimal(3.00);
    } else if (tipoSped.equals("Veloce")) {
        speseSped = new BigDecimal(5.00);
    }
    BigDecimal TotaleOrdine = totaleProdotti.add(speseSped);

    String metodoPagam = (String) request.getAttribute("metodoPagam");

    if (metodoPagam.equals("Alla Consegna")) {
        TotaleOrdine = TotaleOrdine.add(new BigDecimal(3));
    }

    Indirizzi indirizzo = (Indirizzi) request.getAttribute("indirizzo");

    List<Prodotto> listaProdotti = (List<Prodotto>) request.getAttribute("listaProdotti");

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    int i = 0;

    String menuActiveLink = "Conferma Ordine";
    String action = "conferma";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .container {
                margin: 20px 0;
            }
            .prodContainer {
                border: solid 1px #e7e7e7;
                margin: 10px 0;
                padding: 5px;
                width: fit-content;
            }

            .imgholder {
                width: 100px;
                height: 100px;
                float: left;
            }

            th, td {
                padding: 5px 10px 15px;
                vertical-align: top;
            }

            .container > table {
                text-align: left;
                border-spacing: 0;
            }

            .container th, .container td {
                padding: 5px 40px 5px 0;
            }

            .riepilogo {
                position: relative;
            }

            .tab-riepilogo {
                float: left;
            }

            .conferma {
                position: absolute;
                left: 400px;
                bottom: 0px;
            }

            .conferma .button {
                background-color: #00bb17;
                font-size: 1.1em;
                font-weight: bold;
                border-radius: 6px;
                padding: 20px 10px;
                margin: 20px 0;   
            }

            td > a:hover {
                text-decoration: underline;
            }
        </style>

        <script language="javascript">
            function visualizzaProdotto(idProdotto) {
                document.vediProdottoForm.idProdotto.value = idProdotto;
                document.vediProdottoForm.submit();
            }
        </script>

    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main>
            <%@include file="/include/avanzamento.inc" %>
            <section class="clearfix">
                <div class="container">
                    <table>
                        <tr>
                            <th>INDIRIZZO SPEDIZIONE</th>
                            <td>
                                <p><%=indirizzo.getCitta()%></p>
                                <p><%=indirizzo.getIndirizzo()%></p>
                                <p><%=indirizzo.getProvincia()%></p>
                                <p><%=indirizzo.getCap().toString()%></p>
                            </td>
                        </tr>
                        <tr>
                            <th>MODALIT&Agrave; SPEDIZIONE</th>
                            <td><%=tipoSped%></td>
                        </tr>
                        <tr>
                            <th>MODALIT&Agrave; PAGAMENTO</th>
                            <td><%=metodoPagam%></td>
                        </tr>
                    </table>
                </div>

                <%for (Carrello elemento : carrello) {%>
                <div class="prodContainer clearfix">
                    <div class="imgholder">
                        <a href="javascript:visualizzaProdotto('<%=elemento.getIdProdotto()%>')">
                            <img class="centraImmagine" src="immagini/<%=listaProdotti.get(i).getImmAnteprima()%>" width="100" height="100"/>
                        </a>
                    </div>
                    <table>
                        <tr>
                            <th>Nome prodotto</th>
                            <th>Taglia</th>
                            <th>Quantit&agrave;</th>
                            <th>Prezzo unitario</th>
                            <th>Totale</th>
                        </tr>
                        <tr>
                            <td rowspan="2">
                                <a href="javascript:visualizzaProdotto('<%=elemento.getIdProdotto()%>')">
                                    <p><%=listaProdotti.get(i).getNomeProdotto()%></p>
                                </a>
                                <p>Colore: <%=listaProdotti.get(i).getColore()%></p>
                            </td>
                            <td><%=elemento.getTaglia()%></td>
                            <td><%=elemento.getQuantita()%></td>
                            <td><%=listaProdotti.get(i).getPrezzo()%>&euro;</td>
                            <td><%= listaProdotti.get(i).getPrezzo().multiply(new BigDecimal(elemento.getQuantita()))%>&euro;</td>
                        </tr>
                    </table>
                </div>
                <% i++;
                    }%>
                <section class="riepilogo clearfix">
                    <div class="tab-riepilogo">
                        <h2>RIEPILOGO ORDINE</h2>
                        <table>
                            <tr>
                                <th>Totale prodotti</th>
                                <td><%=totaleProdotti%>&euro;</td>
                            </tr>
                            <tr>
                                <th>Spese di spedizione</th>
                                <td><%=speseSped%>&euro;</td>
                            </tr>
                            <%if (metodoPagam.equals("Alla Consegna")) {%>
                            <tr>
                                <th>Pagamento alla consegna</th>
                                <td>3&euro;</td>
                            </tr>
                            <%}%>
                            <tr class="totale">
                                <th>Totale ordine</th>
                                <th><%=TotaleOrdine%>&euro;</th>
                            </tr>
                        </table>
                    </div>
                    <div class="conferma">
                        <input type="button" class="button normal-button" name="conferma-ordine" value="CONFERMA ORDINE"
                               onclick="javascript:confermaOrdineForm.submit()"/>
                    </div>
                </section>
            </section>

            <form name="confermaOrdineForm" action="Dispatcher" method="post">
                <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>">
                <input type="hidden" name="tipoSped" value="<%=tipoSped%>">
                <input type="hidden" name="metodoPagam" value="<%=metodoPagam%>">                                
                <input type="hidden" name="controllerAction" value="Acquisto.confermaOrdine"/>
            </form>

            <form name="vediProdottoForm" action="Dispatcher" method="post">
                <input type="hidden" name="idProdotto"/>                
                <input type="hidden" name="controllerAction" value="HomePage.visualizzaProdotto"/>
            </form>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>