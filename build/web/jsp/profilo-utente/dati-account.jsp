<%@page import="model.mo.Catalogo"%>
<%@page session="false"%>
<%@page import="model.mo.Utente"%>
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

    Utente utente = (Utente) request.getAttribute("utente");
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = "Profilo Utente";

    String menu = "dati-account";
%>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .dati-account {
                float: left;
                margin: 0 0 20px 20px;
            }

            label {
                width: 110px;
            }

            .focus {
                background-color: #2e2ea0;
            }

            .normal-button {
                margin: 0;

            }

        </style>
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main class="clearfix">
                <%@include file="/include/menu-utente.inc"%>
                <div class="info-profilo clearfix">
                    <form name="datiAccountForm" action="Dispatcher" method="post">
                        <div class="dati-account">
                            <div class="field clearfix">
                                <label for="nome">Nome</label>
                                <input type="text" id="nome" name="nome"
                                       value="<%=utente.getNome()%>"
                                       required size="20" maxlength="50"/>
                            </div>
                            <div class="field clearfix">    
                                <label for="cognome">Cognome</label>
                                <input type="text" id="cognome" name="cognome"
                                       value="<%=utente.getCognome()%>"
                                       required size="20" maxlength="50"/>
                            </div>
                            <div class="field clearfix">    
                                <label for="email">Email</label>
                                <input type="email" id="email" name="email"
                                       value="<%=utente.getEmail()%>"
                                       required size="20" maxlength="50"/>
                            </div>
                            <div class="field clearfix">    
                                <label for="password">Password</label>
                                <input type="text" id="password" name="password"
                                       value="<%=utente.getPassword()%>"
                                       required size="20" maxlength="40"/>
                            </div>
                            <div class="field clearfix">
                                <label>&#160;</label>
                                <input type="submit" class="button normal-button" value="AGGIORNA"/>
                            </div>
                        </div>
                        <input type="hidden" name="controllerAction" value="ProfiloUtente.aggiornaDatiAccount"/>
                    </form>
                </div>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
