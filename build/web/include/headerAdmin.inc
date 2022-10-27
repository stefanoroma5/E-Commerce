<header class="clearfix"><!-- Defining the header section of the page -->
    <div class="header">

        <div class="logo clearfix">
            <img id="logo" src="immagini/logo.png" height="40" width="200" />
        </div>   

        <% if (loggedOn) { %>
        <div id="loginbox" class="clearfix">
            <ul>
                <li class="logintext loggedtext">
                    <a href="Dispatcher?controllerAction=ProfiloAdmin.logoutAdmin">esci</a>
                </li>
            </ul>
        </div>
        <% } %>
    </div>

    <% if (loggedOn) { %>
    <nav><!-- Defining the navigation menu -->
            <div class="menu clearfix">
                <ul>
                    <li <%=menuAdmin.equals("home")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=ProfiloAdmin.homeAdmin">Home</a>
                    </li>
                    <li <%=menuAdmin.equals("gest-utenti")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=ProfiloAdmin.vediUtenti">Utenti</a>
                    </li>
                    <li <%=menuAdmin.equals("gest-admin")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=ProfiloAdmin.vediAdmin">Amministratori</a>
                    </li>
                    <li <%=menuAdmin.equals("gest-prodotti")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=AdminGestioneProdotti.vediProdotti">Prodotti</a>
                    </li>
                    <li <%=menuAdmin.equals("ordini")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=AdminGestioneOrdini.tuttiGliOrdini">Ordini</a>
                    </li>
                    <li <%=menuAdmin.equals("cataloghi")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=AdminGestioneCataloghi.vediCataloghi">Cataloghi</a>
                    </li>
                    <li <%=menuAdmin.equals("vetrine")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=AdminGestioneVetrine.vediVetrine">Vetrine</a>
                    </li>
                </ul>
            </div>
        </nav>
    <% } %>
</header>

