<%@page import="model.mo.Catalogo"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.mo.Composizione"%>
<%@page import="model.mo.Ordine"%>
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

    List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");

    String piuRecente = (String) request.getParameter("piuRecente");
    String statoOrdine = (String) request.getParameter("statoOrdine");
    if (statoOrdine == null) {
        statoOrdine = "tutti";
    }
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = "Profilo Utente";
    String menu = "ordini";
%>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .normal-button {
                margin: 0;
            }

            th, td {
                padding: 5px 10px 15px;
            }

            .stato-ordine p {
                font-size: 1.2em;
            }

            td > a:hover {
                text-decoration: underline;
            }

            label {
                float: none;
            }

            input {
                width: auto;
            }

            .ordine {
                margin: 20px 0;
                border-top: solid 1px #2e2ea0;
            }

            .ordine .container {
                float: left;
                margin: 20px 0 10px 0;
            }

            .ordine .prodContainer {
                float: left;
            }

            .ordine .tab-riepilogo {
                float: right;
            }

            .ordine .stato-ordine {
                float: right;
                border-left: none;
            }

            .ordine .container th, .container td {
                padding: 5px 10px 5px 0;
            }

        </style>

        <script language = "javascript">
            function visualizzaProdotto(idProdotto) {
                document.vediProdottoForm.idProdotto.value = idProdotto;
                document.vediProdottoForm.submit();
            }

            function ordiniPiuRecenti() {
                document.filtraOrdiniForm.piuRecente.value = "piuRecente";
            }
        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main class="clearfix">
                <%@include file="/include/menu-utente.inc"%>
                <div class="info-profilo clearfix">
                    <form name="filtraOrdiniForm" action="Dispatcher" method="post">
                        <p>Filtra ordini:</p>                    
                        <input type="checkbox" id="piuRecente" name="piuRecente" onclick="javascript:ordiniPiuRecenti()"
                               <%=(piuRecente != null && !piuRecente.equals("null")) ? "checked" : ""%> >
                        <label for="piuRecente">Pi? recenti</label>

                        <label for="statoOrdine">Stato ordine:</label>
                        <select id="statoOrdine" name="statoOrdine">
                            <option value="tutti" <%=(statoOrdine.equals("tutti")) ? "selected" : ""%> >Tutti</option>
                            <option value="Confermato" <%=(statoOrdine.equals("Confermato")) ? "selected" : ""%>>Confermato</option>
                            <option value="Spedito" <%=(statoOrdine.equals("Spedito")) ? "selected" : ""%>>Spedito</option>
                            <option value="Consegnato" <%=(statoOrdine.equals("Consegnato")) ? "selected" : ""%>>Consegnato</option>
                            <option value="Annullato" <%=(statoOrdine.equals("Annullato")) ? "selected" : ""%>>Annullato</option>
                        </select>
                        <input type="submit" class="button normal-button" value="filtra">
                        <input type="hidden" name="piuRecente" value="null">
                        <input type="hidden" name="controllerAction" value="ProfiloUtente.filtraOrdini"/>
                    </form>
                    <%if (listaOrdini != null && !listaOrdini.isEmpty()) {%>

                    <%for (Ordine ordine : listaOrdini) {%>
                    <div class="ordine clearfix">
                        <div class="container">
                            <table>
                                <tr>
                                    <td>DATA ORDINE:</td>
                                    <td><%=ordine.getDataInserimento().toString()%></td>
                                </tr>
                                <tr>
                                    <td>INDIRIZZO SPEDIZIONE:</td>
                                    <td>
                                        <%=ordine.getIndirizzo().getCitta()%><br>
                                        <%=ordine.getIndirizzo().getIndirizzo()%><br>
                                        <%=ordine.getIndirizzo().getProvincia()%><br>
                                        <%=ordine.getIndirizzo().getCap().toString()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td>MODALIT&Agrave; SPEDIZIONE:</td>
                                    <td><%=ordine.getSpedizione().getMetodoSpedizione()%></td>
                                </tr>
                                <%if (ordine.getSpedizione().getDataSpedizione() != null) {%>
                                <tr>
                                    <td>DATA SPEDIZIONE:</td>
                                    <td><%=ordine.getSpedizione().getDataSpedizione()%></td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td>MODALIT&Agrave; PAGAMENTO:</td>
                                    <td><%=ordine.getPagamento().getMetodoPagamento()%></td>
                                </tr>
                                <tr>
                                    <td>DATA PAGAMENTO:</td>
                                    <td><%=ordine.getPagamento().getDataPagamento()%></td>
                                </tr>
                            </table>
                        </div>

                        <%
                            BigDecimal totProdotti = new BigDecimal(0);
                            for (Composizione comp : ordine.getComposizione()) {
                                totProdotti = totProdotti.add(comp.getPrezzoPagato().multiply(new BigDecimal(comp.getQuantita())));
                            }

                            BigDecimal totOrdine = totProdotti.add(ordine.getSpedizione().getSpeseSpedizione());
                            if (ordine.getPagamento().getMetodoPagamento().equals("Alla Consegna")) {
                                totOrdine.add(new BigDecimal(3));
                            }

                        %>

                        <div class="tab-riepilogo">
                            <h2>RIEPILOGO ORDINE</h2>
                            <table>
                                <tr>
                                    <th>Totale prodotti</th>
                                    <td><%=totProdotti%>&euro;</td>
                                </tr>
                                <%if (ordine.getPagamento().getMetodoPagamento().equals("Alla Consegna")) {%>
                                <tr>
                                    <th>Pagamento alla consegna</th>
                                    <td>+ 3&euro;</td>
                                </tr>
                                <%}%>
                                <tr>
                                    <th>Spese spedizione</th>
                                    <td>+ <%=ordine.getSpedizione().getSpeseSpedizione().toString()%>&euro;</td>
                                </tr>
                                <tr class="totale">
                                    <th>Totale ordine</th>
                                    <th><%=totOrdine%>&euro;</th>
                                </tr>
                            </table>
                        </div>
                        <div class="stato-ordine">
                            <h2>STATO ORDINE</h2><br>
                            <p><%=ordine.getStatoOrdine()%></p>
                        </div>

                        <%for (Composizione comp : ordine.getComposizione()) {%>
                        <div class="prodContainer">
                            <div class="imgholder">
                                <a href="javascript:visualizzaProdotto('<%=comp.getProdotto().getIdProdotto()%>')">
                                    <img class="centraImmagine" src="immagini/<%=comp.getProdotto().getImmAnteprima()%>" width="100" height="100"/>
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
                                        <a href="javascript:visualizzaProdotto('<%=comp.getProdotto().getIdProdotto()%>')">
                                            <p><%=comp.getProdotto().getNomeProdotto()%></p>
                                        </a>
                                        <p>Colore: <%=comp.getProdotto().getColore()%></p>
                                    </td>
                                    <td><%=comp.getTagliaScelta()%></td>
                                    <td><%=comp.getQuantita()%></td>
                                    <td><%=comp.getPrezzoPagato()%>&euro;</td>
                                    <td><%=comp.getPrezzoPagato().multiply(new BigDecimal(comp.getQuantita()))%>&euro;</td>
                                </tr>
                            </table>
                        </div>


                        <%}%>


                    </div>
                    <%}
                    } else {%>       
                    <h2>Non sono presenti ordini</h2>
                    <%}%>
                </div>
                <form name="vediProdottoForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idProdotto"/>                
                    <input type="hidden" name="controllerAction" value="HomePage.visualizzaProdotto"/>
                </form>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>

