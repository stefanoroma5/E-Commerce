<%@page import="model.mo.Utente"%>
<%@page import="java.util.List"%>
<%@page session="false"%>

<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String iniziale = (String) request.getAttribute("iniziale");
    List<String> listaIniziali = (List<String>) request.getAttribute("listaIniziali");
    List<Utente> listaUtenti = (List<Utente>) request.getAttribute("listaUtenti");

    String menuAdmin = "gest-utenti";
    String menuActiveLink = "Profilo Admin";
%>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>

            #initialSelector {
                margin: 12px 0;
            }

            .initial {  
                color: #a3271f;
                letter-spacing: 2px;
            }

            .selectedInitial { 
                color: #c3c3c3;
                letter-spacing: 2px;
            }

            .normal-button {
                margin: 0;
            }

            .utente {
                margin: 0 0 10px 10px;
                padding: 10px 5px;
                border: solid 1px #e7e7e7;
                width: fit-content;
                float: left;
            }

            #rimuovi {
                position: static;
            }

        </style>

        <script language="javascript">
            function changeInitial(inital) {
                document.changeInitialForm.iniziale.value = inital;
                document.changeInitialForm.submit();
            }

            function bloccaUtente(idUtente) {
                document.bloccaUtenteForm.idUtente.value = idUtente;
                document.bloccaUtenteForm.submit();
            }
            
            function mostraOrdini(idUtente) {
                document.mostraOrdiniForm.idUtente.value = idUtente;
                document.mostraOrdiniForm.submit();
            }

        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <nav id="initialSelector">
                    <%if (iniziale.equals("*")) { %>
                    <span class="selectedInitial">*</span>
                    <%} else {%>
                    <a class="initial" href="javascript:changeInitial('*');">*</a>
                    <%}%>      
                    <%for (String i : listaIniziali) {
                            if (i.equals(iniziale)) {%>
                    <span class="selectedInitial"><%=i%></span>
                    <%} else {%>
                    <a class="initial" href="javascript:changeInitial('<%=i%>');"><%=i%></a>
                    <%}%>  
                    <%}%>
                </nav>
                <%for (Utente utente : listaUtenti) {%>
                <div class="utente">
                    <h2><%=utente.getNome()%> <%=utente.getCognome()%></h2>
                    <p><%=utente.getEmail()%></p>                    
                    <input type="button" class="button normal-button" value="Mostra ordini"
                           onclick="javascript:mostraOrdini('<%=utente.getIdUtente().toString()%>')"/>
                    <input type="button" class="button normal-button" value="<%=(utente.isEliminato()) ? "sblocca" : "blocca"%>"
                           onclick="javascript:bloccaUtente('<%=utente.getIdUtente().toString()%>')"/>

                </div>
                <%}%>

                <form name="changeInitialForm" method="post" action="Dispatcher">
                    <input type="hidden" name="iniziale"/>
                    <input type="hidden" name="controllerAction" value="ProfiloAdmin.vediUtenti"/>      
                </form> 

                <form name="bloccaUtenteForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idUtente">
                    <input type="hidden" name="controllerAction" value="ProfiloAdmin.bloccaUtente"/>
                </form>
                
                <form name="mostraOrdiniForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idUtente">
                    <input type="hidden" name="controllerAction" value="AdminGestioneOrdini.ordiniUtente"/>
                </form>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>

