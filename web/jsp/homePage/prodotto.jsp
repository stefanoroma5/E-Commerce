<%@page import="model.mo.Catalogo"%>
<%@page session="false"%>
<%@page import="model.mo.Taglie"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="model.mo.ImmGalleria"%>
<%@page import="java.util.List"%>
<%@page import="model.session.mo.UtenteLoggato"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    UtenteLoggato utenteLoggato = (UtenteLoggato) request.getAttribute("utenteLoggato");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    boolean esisteCarrello = (Boolean) request.getAttribute("esisteCarrello");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    int quantitaCarrello = (int) request.getAttribute("quantitaCarrello");

    String nomeCatalogo = (String) request.getAttribute("nomeCatalogo");
    if (nomeCatalogo == null) {
        nomeCatalogo = "Prodotto";
    }

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");

    boolean disponibile = false;
    if (prodotto != null) {
        for (Taglie taglia : prodotto.getTaglie()) {
            if (taglia.getDisponibilita() > 0) {
                disponibile = true;
            }
        }
    }

    String menuActiveLink = nomeCatalogo;
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .immagini {
                float: left;
                margin: 0px 10px 10px 0px;
            }

            .galleria {
                float: left;
                width: auto; 
                margin-right: 5px;
            }

            .imgholder {
                width: auto;
                height: auto;
                margin-bottom: 5px;
                border: solid 1px #e7e7e7;
                border-radius: 0px;
            }

            .immGrande {
                border: solid 1px #e7e7e7;
                border-radius: 0px;
                width: 450px;
                height: 450px;
                float: right;
                margin: 0px 5px;
                position: relative;
            }

            .cartaProd {
                float: right;
                margin: 0px 10px 10px 10px;
                width: 42%;
            }

            .nomeProdotto {
                font-size: 2em;
            }

            .taglia-quantita {
                margin: 20px 0;
                font-size: larger;
            }

            .taglia {
                float: left;
            }

            .quantita {
                float: right;
                width: 215px;
            }

            label {
                width: auto;
            }

            select, input[type="number"] {
                font-size: medium;
                border-radius: 6px;
            }

            input[type="number"] {
                width: 50px;
                background-color: white;
            }
            select {
                padding: 5px;
                margin: 5px 0;
                border: solid 1px #e7e7e7;
            }

            select:focus, input[type="number"]:focus {
                outline:none;
            }

            .prezzo {
                color: #ff2121;
                font-size: 1.6em;
            }

            .compra {
                background-color: #ff2121;
                font-size: 1.1em;
                font-weight: bold;
                border-radius: 6px;
                padding: 20px 10px;
                margin: 20px 0;   
                position: static;
                transform: none;
            }

            .descrDettagliata {
                margin: 10px 0;
                float: left;
                width: 100%;
            }

            .immGalleria {
                cursor: pointer;
            }

        </style>

        <script language = "javascript">
            function cambiaImmagineGrandeCon(immagine) {
                var imG = document.querySelector("#immGrande");
                imG.src = 'immagini/' + immagine;
            }


            function impostaMaxDisponibilita() {
                var t = document.querySelector('#taglia');
                var q = document.querySelector('#quantitaDisp');
                q.max = t.options[t.selectedIndex].value;
            }

            function aggiungiAlCarrello(idProd) {
                var t = document.querySelector('#taglia');
                var q = document.querySelector('#quantitaDisp');
                var taglia = t.options[t.selectedIndex].text;
                var quantita = q.value;

                var f = document.aggCarrelloForm;
                f.idProdotto.value = idProd;
                f.tagliaScelta.value = taglia;
                f.quantitaScelta.value = quantita;
                f.submit();
            }

        </script>
    </head>

    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main class="clearfix">
                <section class="immagini">
                    <%if (prodotto != null) {%>
                    <div class="galleria">
                        <div class="imgholder immGalleria">
                            <img src="immagini/<%=prodotto.getImmAnteprima()%>" width="80" height="80" 
                                 onclick="javascript:cambiaImmagineGrandeCon('<%=prodotto.getImmAnteprima()%>')"/>
                        </div>
                        <% for (ImmGalleria imm : prodotto.getImmGalleria()) {%>
                        <%if (!imm.getImmagine().equals("null")) {%>
                        <div class="imgholder immGalleria">
                            <img src="immagini/<%=imm.getImmagine()%>" width="80" height="80"
                                 onclick="javascript:cambiaImmagineGrandeCon('<%=imm.getImmagine()%>')"/>
                        </div>
                        <%}%>
                        <%}%>
                    </div>

                    <div class="immGrande">
                        <img class="centraImmagine" id="immGrande" src="immagini/<%=prodotto.getImmAnteprima()%>" width="450" height="450"/>
                    </div>
                </section> 

                <section class="cartaProd">
                    <h1 class="nomeProdotto"><%=prodotto.getNomeProdotto()%></h1><br>
                    <h2>Colore: <%=prodotto.getColore()%></h2><br>
                    <div class="taglia-quantita clearfix">
                        <% if (disponibile) {%>
                        <div class="taglia">
                            <label>TAGLIA:</label>
                            <select id="taglia" name="taglia" onchange="javascript:impostaMaxDisponibilita()" required>
                                <option disabled selected>Seleziona una taglia</option>
                                <%for (Taglie taglia : prodotto.getTaglie()) {
                                        if (taglia.getDisponibilita() > 0) {%>
                                <option value="<%=taglia.getDisponibilita()%>"> <%=taglia.getTaglia()%></option>
                                <%}
                                    }%>
                            </select>
                        </div>

                        <div class="quantita">
                            <label>QUANTIT&Agrave;:</label>
                            <input type="number" id="quantitaDisp" name="quantitaDisp" value="1"
                                   min="1"/>
                        </div>
                        <%} else {%>
                        <h2>Prodotto non disponibile</h2>
                        <%}%>
                    </div>
                    <div class="prezzo">
                        <h1><%=prodotto.getPrezzo()%>&euro;</h1>
                    </div>

                    <%if (disponibile) {%>
                    <input type="button" name="compra" class="button compra" value="AGGIUNGI AL CARRELLO"
                           onclick="javascript:aggiungiAlCarrello('<%=prodotto.getIdProdotto()%>')"/>
                    <%}%>
                </section>

                <section class="descrDettagliata">
                    <h1>Descrizione del prodotto</h1><br>
                    <p><%=prodotto.getDescrProdotto()%></p>
                </section>

                <%} else {%>
                <h1>Prodotto non disponibile</h1>
                <%}%>

                <form name="aggCarrelloForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idProdotto"/>                
                    <input type="hidden" name="tagliaScelta"/>                
                    <input type="hidden" name="quantitaScelta"/>    
                    <input type="hidden" name="nomeCatalogo" value="<%=nomeCatalogo%>"/>
                    <input type="hidden" name="controllerAction" value="GestioneCarrello.aggiungiAlCarrello"/>
                </form>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
