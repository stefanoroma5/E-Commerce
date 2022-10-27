<%@page import="java.math.BigDecimal"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.mo.Ordine"%>
<%@page session="false"%>
<%@page import="java.util.List"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");

    String data1 = (String) request.getParameter("data1");
    if (data1 == null) {
        data1 = "";
    }
    String data2 = (String) request.getParameter("data2");
    if (data2 == null) {
        data2 = "";
    }
    String statoOrdine = (String) request.getParameter("statoOrdine");
    if (statoOrdine == null) {
        statoOrdine = "tutti";
    }

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

            table {
                text-align: left;
                border-spacing: 0;
            }

            th, td {
                padding: 5px 40px 5px 5px;
                vertical-align: top;
            }

            label {
                float: none;
            }

            input {
                width: auto;
            }

            .ordine {
                margin: 10px 25px 10px 0;
                border: solid 1px #e7e7e7;
                border-radius: 10px;
                float: left;
                width: 47%;
            }

            .centrale {
                text-align: center;
                border-bottom: solid 1px #e7e7e7;
                background-color: #f8f8f8;
                font-size: larger;
                border-radius: 10px 10px 0 0;
            }

            .titolo {
                text-align: center;
            }

            .id {
                position: absolute;
                right: 10px;
            }


        </style>

        <script language = "javascript">

        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <h1 class="titolo">LISTA ORDINI</h1>
                <form name="filtraOrdiniForm" action="Dispatcher" method="post">
                    <label for="statoOrdine">Stato ordine:</label>
                    <select id="statoOrdine" name="statoOrdine">
                        <option value="tutti" <%=(statoOrdine.equals("tutti")) ? "selected" : ""%> >Tutti</option>
                        <option value="Confermato" <%=(statoOrdine.equals("Confermato")) ? "selected" : ""%>>Confermato</option>
                        <option value="Spedito" <%=(statoOrdine.equals("Spedito")) ? "selected" : ""%>>Spedito</option>
                        <option value="Consegnato" <%=(statoOrdine.equals("Consegnato")) ? "selected" : ""%>>Consegnato</option>
                        <option value="Annullato" <%=(statoOrdine.equals("Annullato")) ? "selected" : ""%>>Annullato</option>
                    </select>
                    <label for="data">Data ordine:</label>
                    <input type="date" id="data" name="data1" value="<%=(data1.isEmpty()) ? "" : data1%>">
                    <input type="date" name="data2" value="<%=(data2.isEmpty()) ? "" : data2%>">

                    <input type="submit" class="button normal-button" value="filtra">
                    <input type="hidden" name="controllerAction" value="AdminGestioneOrdini.filtraTuttiGliOrdini">
                </form>
                <%if (listaOrdini != null && !listaOrdini.isEmpty()) {%>

                <%for (Ordine ordine : listaOrdini) {%>
                <div class="ordine">
                    <div style="position: relative;">
                        <form name="vediOrdineForm" action="Dispatcher" method="post">
                            <p class="centrale"><b>DATA ORDINE:</b> <%=ordine.getDataInserimento().toString()%> <span class="id">ID:<%=ordine.getIdOrdine()%></span></p>
                            <table>
                                <tr>
                                    <th>UTENTE:</th>
                                    <td><%=ordine.getUtente().getNome()%> <%=ordine.getUtente().getCognome()%></td>
                                </tr>
                                <tr>
                                    <th>INDIRIZZO SPEDIZIONE:</th>
                                    <td>
                                        <%=ordine.getIndirizzo().getCitta()%><br>
                                        <%=ordine.getIndirizzo().getIndirizzo()%><br>
                                        <%=ordine.getIndirizzo().getProvincia()%><br>
                                        <%=ordine.getIndirizzo().getCap().toString()%>
                                    </td>
                                </tr>
                                <tr>
                                    <th>STATO ORDINE:</th>
                                    <td><%=ordine.getStatoOrdine()%></td>
                                </tr>
                                <tr>
                                    <th>TOTALE ORDINE:</th>
                                    <td><%=ordine.getPagamento().getImporto().toString()%>&euro;</td>
                                </tr>
                            </table>
                            <div style="position: absolute; top: 50%; right: 0%;">
                                <input type="submit" class="button normal-button" value="modifica">
                            </div>
                            <input type="hidden" name="idOrdine" value="<%=ordine.getIdOrdine().toString()%>">
                            <input type="hidden" name="idUtente" value="<%=ordine.getUtente().getIdUtente().toString()%>">
                            <input type="hidden" name="statoOrdine" value="<%=statoOrdine%>">                     
                            <input type="hidden" name="data1" value="<%=data1%>">                     
                            <input type="hidden" name="data2" value="<%=data2%>">
                            <input type="hidden" name="controllerAction" value="AdminGestioneOrdini.vediOrdine">
                        </form>
                    </div>
                </div>
                <%}
                } else {%>       
                <h2>Non sono presenti ordini</h2>
                <%}%>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>

