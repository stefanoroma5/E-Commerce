<%@page import="model.mo.Amministratore"%>
<%@page import="java.util.List"%>
<%@page session="false"%>

<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String iniziale = (String) request.getAttribute("iniziale");
    List<String> listaIniziali = (List<String>) request.getAttribute("listaIniziali");
    List<Amministratore> listaAdmin = (List<Amministratore>) request.getAttribute("listaAdmin");

    String menuAdmin = "gest-admin";
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

            function bloccaAdmin(idAdmin) {
                document.bloccaAdminForm.idAdmin.value = idAdmin;
                document.bloccaAdminForm.submit();
            }

        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <section>
                    <h3>Inserisci un nuovo amministratore</h3>
                    <form name="aggiungiAdminForm" method="post" action="Dispatcher">
                        <div class="field clearfix">
                            <label for="nome">Nome</label>
                            <input type="text" id="nome" name="nome"
                                   required size="20" maxlength="50"/>
                        </div>
                        <div class="field clearfix">    
                            <label for="cognome">Cognome</label>
                            <input type="text" id="cognome" name="cognome"
                                   required size="20" maxlength="50"/>
                        </div>
                        <div class="field clearfix">    
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email"
                                   required size="20" maxlength="50"/>
                        </div>
                        <div class="field clearfix">    
                            <label for="password">Password</label>
                            <input type="text" id="password" name="password"
                                   required size="20" maxlength="40"/>
                        </div>
                        <div class="field clearfix">
                            <label>&#160;</label>
                            <input type="submit" class="button normal-button" value="INSERISCI"/>
                        </div>
                        <input type="hidden" name="controllerAction" value="ProfiloAdmin.aggiungiAdmin"/>
                    </form>
                </section>

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

                <%for (Amministratore admin : listaAdmin) {%>
                <div class="utente">
                    <h2><%=admin.getNome()%> <%=admin.getCognome()%></h2>
                    <p><%=admin.getEmail()%></p>
                    <p><%=admin.getPassword()%></p>
                    <input type="button" class="button normal-button" value="<%=(admin.isEliminato()) ? "sblocca" : "blocca"%>"
                           onclick="javascript:bloccaAdmin('<%=admin.getIdAdmin().toString()%>')"/>

                </div>
                <%}%>

                <form name="changeInitialForm" method="post" action="Dispatcher">
                    <input type="hidden" name="iniziale"/>
                    <input type="hidden" name="controllerAction" value="ProfiloAdmin.vediAdmin"/>      
                </form> 

                <form name="bloccaAdminForm" action="Dispatcher" method="post">
                    <input type="hidden" name="idAdmin">
                    <input type="hidden" name="controllerAction" value="ProfiloAdmin.bloccaAdmin"/>
                </form>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>