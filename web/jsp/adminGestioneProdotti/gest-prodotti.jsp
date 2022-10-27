<%@page import="model.mo.Catalogo"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="java.util.List"%>
<%@page session="false"%>
<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    List<Prodotto> listaProdotti = (List<Prodotto>) request.getAttribute("listaProdotti");

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String catalogoSelezionato = (String) request.getParameter("catalogoSelezionato");

    String menuAdmin = "gest-prodotti";
    String menuActiveLink = "Profilo Admin";
%>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>

            .info-profilo {
                width: 80%;
            }

            .normal-button {
                margin: 0;
            }

            .nuovoProdotto {
                background-color: #00bb17;    
                padding: 15px;
                border-radius: 25px; 
            }

            .prodotto {
                border: solid 1px #e7e7e7;
                width: 32%;
                float: left;
                margin: 0 10px 10px 0;
            }

            .prodotto .imgholder {
                width: 100px;
                height: 100px;
                float: left;
            }

            .prodotto table {
                text-align: left;
                border-spacing: 0;
                width: -webkit-fill-available;
            }

            .prodotto th, td {
                padding: 5px 15px 5px 0;
            }

            #rimuovi {
                position: static;
            }

            .elimina {
                background-color: #ff2121;
            }

            .centrale {
                text-align: center;
                margin: 5px 0;
            }


        </style>

        <script language = "javascript">
            function modificaProdotto(idProdotto) {
                document.modificaProdottoForm.idProdotto.value = idProdotto;
                document.modificaProdottoForm.submit();
            }

            function eliminaProdotto(idProdotto) {
                document.eliminaProdottoForm.idProdotto.value = idProdotto;
                document.eliminaProdottoForm.submit();
            }
        </script>
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix"> 
                <nav>
                    <form name="selezionaCatalogoForm" action="Dispatcher" method="post">
                        <label>Filtra per catalogo:</label>
                        <%for (Catalogo c : listaCataloghi) {%>
                        <input type="radio" name="catalogoSelezionato" value="<%=c.getNomeCatalogo()%>"
                               <%=(catalogoSelezionato != null && catalogoSelezionato.equals(c.getNomeCatalogo())) ? "checked" : ""%>><%=c.getNomeCatalogo()%>
                        <% } %>
                        <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.vediProdotti">
                        <input type="submit" class="button normal-button" value="filtra">
                    </form>
                </nav>
                <form name="inserimentoForm" action="Dispatcher" method="post">
                    <div class="centrale">
                        <input type="submit" class="button normal-button nuovoProdotto" value="INSERISCI UN NUOVO PRODOTTO">
                        <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.paginaInserimento">                        
                    </div>
                </form>
                    <%for (Prodotto p : listaProdotti) {%>
                    <div class="prodotto clearfix">
                        <table>
                            <tr>
                                <td>
                                    <div class="imgholder">
                                        <a href="javascript:modificaProdotto('<%=p.getIdProdotto()%>')">
                                            <img class="centraImmagine" src="immagini/<%=p.getImmAnteprima()%>" width="100" height="100"/>
                                        </a>
                                    </div>
                                </td>
                                <td>
                                    <h4><a href="javascript:modificaProdotto('<%=p.getIdProdotto()%>')">
                                            <%=p.getNomeProdotto()%>
                                        </a>
                                    </h4>
                                    <p>Colore: <%=p.getColore()%></p>
                                </td>
                                <td>
                                    <a href="javascript:eliminaProdotto('<%=p.getIdProdotto()%>')">
                                        <img id="rimuovi" alt="Rimuovi" src="immagini/rimuovi.png" width="15" height="15" title="Rimuovi"/>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <% }%>

                    <form name="modificaProdottoForm" action="Dispatcher" method="post">
                        <input type="hidden" name="idProdotto"/>                
                        <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.paginaModifica"/>
                    </form>

                    <form name="eliminaProdottoForm" action="Dispatcher" method="post">
                        <input type="hidden" name="idProdotto"/>                
                        <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.eliminaProdotto"/>
                    </form>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>


