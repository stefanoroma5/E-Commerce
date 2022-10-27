<%@page import="java.math.BigDecimal"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.mo.Composizione"%>
<%@page import="model.mo.Ordine"%>
<%@page session="false"%>
<%@page import="java.util.List"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    Ordine ordine = (Ordine) request.getAttribute("ordine");
    
    String statoOrdine = (String) request.getAttribute("statoOrdine");
    String data1 = (String) request.getAttribute("data1");
    String data2 = (String) request.getAttribute("data2");

    String menuAdmin = "ordini";
    String menuActiveLink = "Profilo Admin";
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
                vertical-align: top;
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
                margin: 20px 80px 10px 0;
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

        </style>

        <script language = "javascript">
            function modificaProdotto(idProdotto) {
                document.vediProdottoForm.idProdotto.value = idProdotto;
                document.vediProdottoForm.submit();
            }


        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <h1>Ordine di <%=ordine.getUtente().getNome()%> <%=ordine.getUtente().getCognome()%></h1>
                <div class="ordine clearfix">
                    <div class="container">
                        <p>ID: <%=ordine.getIdOrdine()%></p>
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
                            <%if(ordine.getSpedizione().getDataSpedizione() != null) {%>
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
                            totOrdine = totOrdine.add(new BigDecimal(3));
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
                        <form name="cambiaStatoOrdineForm" action="Dispatcher" method="post">
                            <select name="nuovoStatoOrdine">
                                <option value="Confermato" <%=(ordine.getStatoOrdine().equals("Confermato")) ? "selected" : ""%>>Confermato</option>
                                <option value="Spedito" <%=(ordine.getStatoOrdine().equals("Spedito")) ? "selected" : ""%>>Spedito</option>
                                <option value="Consegnato" <%=(ordine.getStatoOrdine().equals("Consegnato")) ? "selected" : ""%>>Consegnato</option>
                                <option value="Annullato" <%=(ordine.getStatoOrdine().equals("Annullato")) ? "selected" : ""%>>Annullato</option>
                            </select>
                            <input type="submit" class="button normal-button" value="aggiorna">
                            <input type="hidden" name="idUtente" value="<%=ordine.getUtente().getIdUtente().toString()%>">                        
                            <input type="hidden" name="idOrdine" value="<%=ordine.getIdOrdine().toString()%>">                     
                            <input type="hidden" name="statoOrdine" value="<%=statoOrdine%>">                     
                            <input type="hidden" name="data1" value="<%=data1%>">                     
                            <input type="hidden" name="data2" value="<%=data2%>">                     
                            <input type="hidden" name="controllerAction" value="AdminGestioneOrdini.aggiornaStatoOrdine"/>
                        </form>
                    </div>

                    <%for (Composizione comp : ordine.getComposizione()) {%>
                    <div class="prodContainer">
                        <div class="imgholder">
                            <a href="javascript:modificaProdotto('<%=comp.getProdotto().getIdProdotto()%>')">
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
                                    <a href="javascript:modificaProdotto('<%=comp.getProdotto().getIdProdotto()%>')">
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
                <form name="vediProdottoForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idProdotto"/>                
                    <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.paginaModifica"/>
                </form>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>

