<%@page import="model.mo.Catalogo"%>
<%@page import="java.math.BigDecimal"%>
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
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = "Pagamento";
    String action = "pagamento";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .wrapper {
                margin: 0 auto -45px;
            }

            .container {
                margin: 20px auto;
                width:fit-content;
            }

            .mod-pagam {
                margin: 10px 0;
            }

            .button {
                position: static;
                width: auto;
                transform: none;
                border-radius: 4px;
                margin: 0;
            }

            .indietro {
                background-color: #ff2121;
                float: right;
            } 

            .tab-riepilogo {
                float: none;
                width: fit-content;
            }

            input {
                width: 240px;
            }

            .carte {
                position: absolute;
                top: 170px;
                right: 120px;
            }

        </style>

        <script language="javascript">
            function selezionaModPagamento(mod) {
                document.metodoPagamForm.metodoPagam.value = mod;
                document.metodoPagamForm.submit();
            }
        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main>
                <%@include file="/include/avanzamento.inc" %>

                <section class="clearfix">
                    <form name="confermaModPagamForm" action="Dispatcher" method="post">
                        <div class="container">
                            <h1>Modalit&aacute; di pagamento</h1>
                            <div class="mod-pagam">
                                <input type="radio" name="metodoPagam" value="Alla Consegna" required
                                       onclick="javascript:selezionaModPagamento('Alla Consegna')"
                                       <%=(metodoPagam.equals("Alla Consegna")) ? "checked" : ""%>/>Alla consegna (+3&euro;, solo contanti)<br>
                                <input type="radio" name="metodoPagam" value="Carta di credito" required
                                       onclick="javascript:selezionaModPagamento('Carta di credito')"
                                       <%=(metodoPagam.equals("Carta di credito")) ? "checked" : ""%>/>Carta di credito<br>
                                <input type="radio" name="metodoPagam" value="PayPal" required
                                       onclick="javascript:selezionaModPagamento('PayPal')"
                                       <%=(metodoPagam.equals("PayPal")) ? "checked" : ""%>/>PayPal<br>
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

                            <input type="hidden" name="metodoPagam" value="<%=metodoPagam%>"/>
                            <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>"/>
                            <input type="hidden" name="tipoSped" value="<%=tipoSped%>">
                            <input type="hidden" name="controllerAction" value="Acquisto.confermaModPagam"/>
                            
                            <input type="submit" class="button" value="CONFERMA"/>
                            <input type="button" name="indietro" class="button indietro" value="INDIETRO" onclick="document.indietroForm.submit()"/>
                        </div>

                        <div class="carte">
                            <%if (metodoPagam.equals("Carta di credito")) {%>
                            <img src="immagini/creditcards.png" width="320">
                            <div>
                                <label for="numeroCarta">Numero carta</label>
                                <input type="text" id="numeroCarta" name="numeroCarta" required minlength="16" maxlength="16"/>
                            </div>
                            <div>
                                <label for="cvc">CVC</label>
                                <input type="text" id="cvc" name="cvc" required minlength="3" maxlength="3"/>
                            </div>
                            <div>
                                <label for="dataScadenza">Data di scadenza</label>
                                <input type="month" id="dataScadenza" name="dataScadenza" required/>
                            </div>
                            <% } else if (metodoPagam.equals("PayPal")) { %>
                            <img src="immagini/paypal.jpg" width="250">
                            <div>
                                <input type="email" name="email" placeholder="Indirizzo Email" required/>
                            </div>
                            <div>
                                <input type="password" name="password" placeholder="Password" required/>
                            </div>
                            <% }%>
                        </div>
                    </form>
                </section>

                <form name="metodoPagamForm" action="Dispatcher" method="post">
                    <input type="hidden" name="metodoPagam"/>
                    <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>"/>
                    <input type="hidden" name="tipoSped" value="<%=tipoSped%>">
                    <input type="hidden" name="controllerAction" value="Acquisto.modPagamento"/>
                </form>

                <form name="indietroForm" action="Dispatcher" method="post">
                    <input type="hidden" name="totaleProdotti" value="<%=totaleProdotti%>"/>
                    <input type="hidden" name="tipoSped" value="<%=tipoSped%>">
                    <input type="hidden" name="controllerAction" value="Acquisto.indSped"/>
                </form>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
