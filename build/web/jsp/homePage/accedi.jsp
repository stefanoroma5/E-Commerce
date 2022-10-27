<%@page import="model.mo.Catalogo"%>
<%@page session="false"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="java.util.List"%>
<%@page import="model.session.mo.UtenteLoggato"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    UtenteLoggato utenteLoggato = (UtenteLoggato) request.getAttribute("utenteLoggato");

    boolean esisteCarrello = (Boolean) request.getAttribute("esisteCarrello");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    int quantitaCarrello = (int) request.getAttribute("quantitaCarrello");
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Accedi";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .login {
                text-align: center;
                margin: 20px;
            }

            .accedi-account {
                float: left;
                width: 50%;
                margin-bottom: 40px;
            }

            .crea-account {
                float: right;
                width:50%;
            }

        </style>
        
        <script language="javascript">
            
            
        </script>
        
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main>
                <section class=" login clearfix">
                    <div class="accedi-account">
                        <h1>ACCEDI ALL'ACCOUNT UTENTE</h1>
                        <form name="accessoUtente" action="Dispatcher" method="post">
                            <input type="email" name="emailUtente" maxlength="50" required placeholder='Inserisci la tua mail'><br>
                            <input type="password" name="passwordUtente" maxlength="40" required placeholder='Inserisci la password'><br>
                            <input type="submit" class="button normal-button" value="ACCEDI">
                            <input type="hidden" name="controllerAction" value="HomePage.accediUtente">
                        </form>
                    </div>

                    <div class="crea-account">
                        <h1>CREA UN ACCOUNT</h1>
                        <form name="creazioneUtente" action="Dispatcher" method="post">
                            <input type="text" name="newemail" maxlength="50" required placeholder='Inserisci una mail valida'><br>
                            <input type="password"  name="newpassword" maxlength="40" required placeholder='Crea la tua password'><br>
                            <input type="text" name="newnome" maxlength="50" required placeholder='Inserisci il tuo nome'><br>
                            <input type="text"  name="newcognome" maxlength="50" required placeholder='Inserisci il tuo cognome'><br>
                            <input type="submit" class="button normal-button" value="CREA">
                            <input type="hidden" name="controllerAction" value="ProfiloUtente.creaAccount">
                        </form>
                    </div>
                </section>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>

