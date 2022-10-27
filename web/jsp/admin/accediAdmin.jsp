<%@page session="false"%>
<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String menuActiveLink = "Accesso Admin";
    
    String menuAdmin = "null";
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

        </style>
        
        <script language="javascript">
            
            
        </script>
        
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main>
                <section class=" login clearfix">

                    <div>
                        <h1>ACCEDI ALL'ACCOUNT AMMINISTRATORE</h1>
                        <form name="accessoAdmin" action="Dispatcher" method="post">
                            <input type="email" name="emailAdmin" maxlength="50" required placeholder='Inserisci la tua mail'><br>
                            <input type="password" name="passwordAdmin" maxlength="40" required placeholder='Inserisci la password'><br>
                            <input type="submit" class="button normal-button" value="ACCEDI">
                            <input type="hidden" name="controllerAction" value="ProfiloAdmin.accediAdmin">
                        </form>
                    </div>

                </section>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>