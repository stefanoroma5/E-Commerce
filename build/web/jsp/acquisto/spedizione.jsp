<%@page import="model.mo.Catalogo"%>
<%@page import="java.math.BigDecimal"%>
<%@page session="false"%>
<%@page import="model.mo.Indirizzi"%>
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

    Indirizzi indirizzo = (Indirizzi) request.getAttribute("indirizzo");

    BigDecimal totaleProdotti = (BigDecimal) (request.getAttribute("totaleProdotti"));
    String tipoSped = (String) (request.getAttribute("tipoSped"));
    BigDecimal speseSped = new BigDecimal(0);
    if (tipoSped.equals("Standard")) {
        speseSped = new BigDecimal(3.00);
    } else if (tipoSped.equals("Veloce")) {
        speseSped = new BigDecimal(5.00);
    }
    BigDecimal TotaleOrdine = totaleProdotti.add(speseSped);
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = "Registrazione";
    String action = "spedizione";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .inserimento {
                float:left;
                margin: 20px 0;
            }

            .inserimento > p{
                font-size: large;
            }

            .title {
                margin-bottom:10px;
            }

            select {
                width: 310px;
            }

            .button {
                position: static;
                width: auto;
                transform: none;
                border-radius: 4px;
                margin: 0;
            }

            .spedizione {
                float:right;
                margin: 20px 120px;
            }

            .clearfix > a {
                text-decoration: underline;
            }

        </style>

        <script>
            function tipoSpediz(val) {
                document.speseSpedForm.tipoSped.value = val;
                document.speseSpedForm.submit();
            }

        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main>
                <%@include file="/include/avanzamento.inc" %>
                <section class="clearfix">
                    <%if (indirizzo.getIdIndirizzo() != null) {%>
                    <div class="inserimento">
                        <div class="title">
                            <h1>Indirizzo di spedizione</h1> 
                        </div>

                        <p><b>Nome:</b> <%=utenteLoggato.getNomeUtente()%></p><br>
                        <p><b>Cognome:</b> <%=utenteLoggato.getCognomeUtente()%></p><br>
                        <p><b>Indirizzo:</b> <%=indirizzo.getIndirizzo()%></p><br>
                        <p><b>Citt&aacute;:</b> <%=indirizzo.getCitta()%></p><br>
                        <p><b>Provincia:</b> <%=indirizzo.getProvincia()%></p><br>
                        <p><b>CAP:</b> <%=indirizzo.getCap()%></p><br>
                        <p><b>Telefono 1:</b> <%=indirizzo.getTelefono1()%></p><br>
                        <%if (indirizzo.getTelefono2() != null && !indirizzo.getTelefono2().isEmpty()) {%>
                        <p><b>Telefono 2:</b> <%=indirizzo.getTelefono2()%></p><br>
                        <%}%>
                        <input type="button" class="button normal-button" value="modifica"
                               onclick="javascript:modificaIndirizzoForm.submit()"/>
                    </div>

                    <div class="spedizione clearfix">
                        <div class="title">
                            <h1>Tipo di spedizione</h1>
                        </div>

                        <div>
                            <form name="confermaSpedForm" action="Dispatcher" method="post">
                                <div class="field clearfix">
                                    <input type="radio" name="tipoSped" value="Standard" required
                                           <%=(tipoSped.equals("Standard")) ? "checked" : ""%>
                                           onclick="javascript:tipoSpediz('Standard')"/>Standard 3&euro;<br>
                                </div>
                                <div class="field clearfix">
                                    <input type="radio" name="tipoSped" value="Veloce" required 
                                           <%=(tipoSped.equals("Veloce")) ? "checked" : ""%>
                                           onclick="javascript:tipoSpediz('Veloce')" />Veloce 5&euro;<br><br>
                                </div>

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
                                        <tr class="totale">
                                            <th>Totale ordine</th>
                                            <th><%=TotaleOrdine%>&euro;</th>
                                        </tr>
                                    </table>
                                </div>
                                <div class="field clearfix">
                                    <input type="submit" class="button normal-button" value="CONFERMA"/>
                                </div>
                                <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>"/>
                                <input type="hidden" name="metodoPagam" value="no">
                                <input type="hidden" name="controllerAction" value="Acquisto.modPagamento"/>
                            </form>
                        </div>
                    </div>


                    <%} else { %>
                    <h1>Non sono presenti indirizzi di spedizione o non è stato selezionato nessun indirizzo</h1> 
                    <input type="button" class="button normal-button" value="Vai agli indirizzi"
                               onclick="javascript:modificaIndirizzoForm.submit()"/>
                    <%}%>
                </section>

                <form name="speseSpedForm" action="Dispatcher" method="post">
                    <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>"/>
                    <input type="hidden" name="tipoSped">
                    <input type="hidden" name="controllerAction" value="Acquisto.indSped"/>
                </form>

                <form name="modificaIndirizzoForm" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="ProfiloUtente.indirizziAccount"/>
                </form>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
