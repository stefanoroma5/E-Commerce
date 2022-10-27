<%@page import="model.mo.Catalogo"%>
<%@page import="java.math.BigDecimal"%>
<%@page session="false"%>
<%@page import="model.mo.Prodotto"%>
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

    List<Prodotto> listaProdotti = (List<Prodotto>) request.getAttribute("listaProdotti");

    String menuActiveLink = "Carrello";
    int i = 0;
    BigDecimal parziale;
    BigDecimal totale = new BigDecimal(0);
    if (esisteCarrello) {
        for (Carrello c : carrello) {
            parziale = listaProdotti.get(i).getPrezzo().multiply(new BigDecimal(c.getQuantita()));
            totale = totale.add(parziale);
            i++;
        }
    }

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    i = 0;
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .elementi {
                margin: 15px 0; 
                float: left;
            }

            .elementi th, .elementi td {
                padding: 5px 30px 15px;
            }

            .prodContainer {
                border: solid 1px #e7e7e7;
                margin: 10px 0;
                padding: 5px;
            }

            .imgholder {
                width: 150px;
                height: 150px;
                float: left;
            }

            .quantita {
                height: 20px;
            }

            #rimuovi {
                float: right;
                position: relative;
                top: 10px;
                right: 10px;
                cursor: pointer;
            }


            .procedi-acquisto {
                font-size: 1.1em;
                margin: 15px 0;
            }

            input[type="text"] {
                border-radius: 4px;
                margin: 10px 0;
            }

            .modButton {
                width: auto;
            }
            
            td > a:hover {
                text-decoration: underline;
            }

        </style>

        <script language = "javascript">
            function rimuovi(prod) {
                document.rimuoviForm.idProdotto.value = prod;
                document.rimuoviForm.submit();
            }

            function aggiungi(prod, i) {
                document.querySelector('#quantita' + i).value++;
                var v = document.querySelector('#quantita' + i).value;
                document.modificaForm.idProdotto.value = prod;
                document.modificaForm.nuovaQuantita.value = v;
                document.modificaForm.submit();
            }

            function togli(prod, i) {
                document.querySelector('#quantita' + i).value--;
                var v = document.querySelector('#quantita' + i).value;
                document.modificaForm.idProdotto.value = prod;
                document.modificaForm.nuovaQuantita.value = v;
                document.modificaForm.submit();
            }
            
            function visualizzaProdotto(idProdotto) {
                document.vediProdottoForm.idProdotto.value = idProdotto;
                document.vediProdottoForm.submit();
            }


        </script>
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main class="clearfix">
                <%if (carrello == null) { %>
                <h2>Il tuo carrello &egrave; vuoto</h2>                
                <%} else {%>
                <h1>IL TUO CARRELLO</h1>
                <div class="elementi clearfix">
                    <%for (Carrello elemento : carrello) {%>
                    <div class="prodContainer clearfix">
                        <div class="imgholder">
                            <a href="javascript:visualizzaProdotto('<%=elemento.getIdProdotto()%>')">
                                <img class="centraImmagine" src="immagini/<%=listaProdotti.get(i).getImmAnteprima()%>" width="150" height="150"/>
                            </a>
                        </div>
                        <img id="rimuovi" alt="Rimuovi" src="immagini/rimuovi.png" width="15" height="15" 
                             title="Rimuovi" onclick="javascript:rimuovi('<%=elemento.getIdProdotto()%>')"/>
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
                                <td>
                                    <input type="button" class=" button normal-button" value="-" onclick="javascript:togli('<%=elemento.getIdProdotto()%>', '<%=i%>')">
                                    <input type="text" id="quantita<%=i%>" class="quantita" value="<%=elemento.getQuantita()%>"
                                           readonly>
                                    <input type="button" class=" button normal-button" value="+" onclick="aggiungi('<%=elemento.getIdProdotto()%>', '<%=i%>')">

                                </td>
                                <td><%=listaProdotti.get(i).getPrezzo()%>&euro;</td>
                                <td><%= listaProdotti.get(i).getPrezzo().multiply(new BigDecimal(elemento.getQuantita()))%>&euro;</td>
                            </tr>
                        </table>
                    </div>
                    <% i++;
                        }%>

                </div>                        

                <div class="tab-riepilogo">
                    <h2>RIEPILOGO ORDINE</h2>
                    <table>
                        <tr class="totale">
                            <th>Totale ordine</th>
                            <th><%=totale%>&euro;</th>
                        </tr>
                    </table>
                </div>

                <section>
                    <input type="button" class="button normal-button procedi-acquisto" name="procedi-acquisto" value="PROCEDI ALL'ACQUISTO"
                           onclick="document.indirizziForm.submit()"/>
                </section>
                <%}%>
                <form name="rimuoviForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idProdotto"/> 
                    <input type="hidden" name="controllerAction" value="GestioneCarrello.rimuoviDalCarrello"/>
                </form>

                <form name="modificaForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idProdotto"/> 
                    <input type="hidden" name="nuovaQuantita"/>                     
                    <input type="hidden" name="controllerAction" value="GestioneCarrello.modificaCarrello"/>
                </form>

                <form name="indirizziForm" action="Dispatcher" method="post">
                    <input type="hidden" name="totaleProdotti" value="<%=totale%>"/>
                    <input type="hidden" name="tipoSped" value="no"/>
                    <input type="hidden" name="controllerAction" value="Acquisto.indSped"/>
                </form>

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
