<%@page import="model.session.mo.AdminLoggato"%>
<%@page session="false"%>
<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    AdminLoggato adminLoggato = (AdminLoggato) request.getAttribute("adminLoggato");

    String menuActiveLink = "Accesso Admin";
    
    String menuAdmin = "home";
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
            
            h1 {
                text-transform: uppercase;
            }

        </style>
        
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main>
                <section class=" login clearfix">
                    <h1>Benvenuto <%=adminLoggato.getNomeAdmin()%> </h1>
                </section>

            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>