<%@page import="model.mo.Vetrina"%>
<%@page session="false"%>
<%@page import="java.util.List"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    List<Vetrina> listaVetrine = (List<Vetrina>) request.getAttribute("listaVetrine");

    String menuAdmin = "vetrine";
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

            .buoni-vetrine {
                margin-bottom: 20px;
            }

            label, input {
                width: auto;
            }

            label {
                font-size: 1.1em;
            }

            .elimina {
                background-color: #ff2121;
            }


        </style>

        <script language="javascript">
            function bloccaVetrina(id) {
                document.bloccaVetrinaForm.idVetrina.value = id;
                document.bloccaVetrinaForm.submit();
            }
        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <%--
                    <div class="buoni-vetrine">
                        <h1>Crea un buono sconto</h1>
                        <div class="field clearfix">
                            <label for="buono-sconto">Inserisci un codice di massimo 5 caratteri:</label>
                            <input type="text" id="buono-sconto" name="buono-sconto" maxlength="5"> (value="...")
                        </div>
                        <div class="field clearfix">
                            <label for="sconto">Inserisci la percentuale di sconto:</label>
                            <input type="number" id="sconto" name="sconto" min="1" max="99">%
                        </div>
                        <input type="submit" class="button normal-button" value="CREA">
                    </div> --%>

                <div class="buoni-vetrine">
                    <h1>Vetrine</h1>
                    <%for (Vetrina v : listaVetrine) {%>
                    <form name="aggiornaVetrinaForm" action="Dispatcher" method="post">
                        <div class="field clearfix">

                            <label>Nome vetrina:</label>
                            <input type="text" maxlength="40" name="nomeVetrina" value="<%=v.getNomeVetrina()%>" required>

                            <input type="submit" class="button normal-button" value="SALVA">
                            <input type="button" class="button normal-button elimina" 
                                   onclick="javascript:bloccaVetrina('<%=v.getIdVetrina().toString()%>')" 
                                   value="<%=(v.isEliminato()) ? "sblocca" : "blocca"%>">
                        </div>
                        <input type="hidden" name="idVetrina" value="<%=v.getIdVetrina().toString()%>">
                        <input type="hidden" name="controllerAction" value="AdminGestioneVetrine.aggiornaVetrina">
                    </form>

                    <%}%>
                    <h2>Crea una nuova vetrina</h2>
                    <form name="creaVetrinaForm" action="Dispatcher" method="post">
                    <div class="field clearfix">
                        <label for="vetrina">Nome vetrina:</label>
                        <input type="text" id="vetrina" name="nuovaVetrina" maxlength="40" required>
                        <input type="submit" class="button normal-button" value="CREA">
                    </div>
                        <input type="hidden" name="controllerAction" value="AdminGestioneVetrine.creaVetrina">
                    </form>
                </div>

                <form name="bloccaVetrinaForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idVetrina">
                    <input type="hidden" name="controllerAction" value="AdminGestioneVetrine.bloccaVetrina">
                </form>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>



