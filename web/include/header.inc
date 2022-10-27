<script language="javascript">
    function visualizzaCatalogo(nCatalogo) {
        document.visualizzaCatalogoForm.nomeCatalogo.value = nCatalogo;
        document.visualizzaCatalogoForm.submit();
    }

</script>

<header class="clearfix"><!-- Defining the header section of the page -->
    <div class="header">

        <div class="logo clearfix">
            <a href="Dispatcher?controllerAction=HomePage.home">
                <img id="logo" src="immagini/logo.png" height="40" width="200" title="Home" />
            </a>  
        </div> 
        <% if (!menuActiveLink . equals("Accedi")) { %>
        <div class="carrello">
                <a href="Dispatcher?controllerAction=GestioneCarrello.vediCarrello">
                    <img src="immagini/carrello.png" height="25" width="25"><%= quantitaCarrello %></img>
                </a>
            </div>  
        <% } %>
        
        <div id="loginbox" class="clearfix">
            <ul>
                <% if (menuActiveLink . equals("Accedi")) { %>
                <li class="logintext">
                        <a href="Dispatcher?controllerAction=ProfiloAdmin.paginaAccesso">Area amministratore</a>
                    </li>
                <% } else { %>

                    <% if (!loggedOn) { %>                    
                <li class="logintext">
                            <a href="Dispatcher?controllerAction=HomePage.paginaRegistrazione">Login    /    Registrati</a>
                        </li>
                    <% } else { %>
                <li class="logintext loggedtext">
                            <a href="Dispatcher?controllerAction=ProfiloUtente.datiAccount">il mio account</a>
                        </li>
                        <li class="logintext loggedtext">
                            <a href="Dispatcher?controllerAction=HomePage.logoutUtente">esci</a>
                        </li>
                    <% }
                } %>
            </ul>
        </div>
        
        <nav><!-- Defining the navigation menu -->
        <div class="menu clearfix">
            <ul>
                <li <%= menuActiveLink . equals("Home") ? "class=\"active\"" : "" %>>
                    <a href="Dispatcher?controllerAction=HomePage.home">Home</a>
                </li>
                <%for (Catalogo catalogo : listaCataloghi) {%>
                <li <%=menuActiveLink.equals(catalogo.getNomeCatalogo())?"class=\"active\"":""%>>
                    <a href="javascript:visualizzaCatalogo('<%=catalogo.getNomeCatalogo()%>')"><%=catalogo.getNomeCatalogo()%></a>
                </li>
                <%}%>
            </ul>
        </div>
    </nav>
    <form name="visualizzaCatalogoForm" method="post" action="Dispatcher">
        <input type="hidden" name="nomeCatalogo"/>    
        <input type="hidden" name="categoriaSelezionata" value="null"/>
        <input type="hidden" name="prezzoScelto" value="null"/>
        <input type="hidden" name="marcaSelezionata" value="null"/>
        <input type="hidden" name="listaMarcheSelezionate" value="null"/>                
        <input type="hidden" name="tagliaSelezionata" value="null"/>
        <input type="hidden" name="listaTaglieSelezionate" value="null"/>
        <input type="hidden" name="controllerAction" value="HomePage.visualizzaCatalogo"/>
    </form>
    </div>
</header>
