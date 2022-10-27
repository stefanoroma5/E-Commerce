<%@page import="model.mo.Catalogo"%>
<%@page import="model.mo.Categoria"%>
<%@page session="false"%>
<%@page import="java.util.List"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");
    List<Catalogo> nomiCataloghi = (List<Catalogo>) request.getAttribute("nomiCataloghi");

    String menuAdmin = "cataloghi";
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
            
            .categoria {
                border-bottom: solid 1px #e7e7e7;
            }


        </style>

        <script language="javascript">
            function bloccaCatalogo(id) {
                document.bloccaCatalogoForm.idCatalogo.value = id;
                document.bloccaCatalogoForm.submit();
            }
            function bloccaCategoria(id) {
                document.bloccaCategoriaForm.idCategoria.value = id;
                document.bloccaCategoriaForm.submit();
            }
        </script>

    </head>
    <body>
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

                <%for (Catalogo cat : nomiCataloghi) {%>
                <h1>Catalogo <%=cat.getNomeCatalogo()%></h1>
                <form name="aggiornaCatalogoForm" action="Dispatcher" method="post">
                    <div class="field clearfix">

                        <label>Nome catalogo:</label>
                        <input type="text" maxlength="40" name="nomeCatalogo" value="<%=cat.getNomeCatalogo()%>" required>
                    </div>
                    <div class="field clearfix">
                        <label>Descrizione catalogo:</label>
                        <textarea name="descrCatalogo" cols="140" rows="5" wrap="hard" required maxlength="100"><%=cat.getDescrCatalogo()%></textarea>
                    </div>
                    <div class="field clearfix">
                        <label>Immagine catalogo:</label>
                        <input type="text" maxlength="500" name="immCatalogo" value="<%=cat.getImmCatalogo()%>" required>

                        <input type="submit" class="button normal-button" value="SALVA">
                        <input type="button" class="button normal-button elimina" 
                               onclick="javascript:bloccaCatalogo('<%=cat.getIdCatalogo().toString()%>')" 
                               value="<%=(cat.isEliminato()) ? "sblocca" : "blocca"%>">
                    </div>
                    <input type="hidden" name="idCatalogo" value="<%=cat.getIdCatalogo().toString()%>">
                    <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.aggiornaCatalogo">
                </form>
                <h2>Categorie di <%=cat.getNomeCatalogo()%></h2>

                <%for (Catalogo catalogo : listaCataloghi) {
                        if (catalogo.getNomeCatalogo().equals(cat.getNomeCatalogo())) {
                            for (Categoria c : catalogo.getCategorie()) {
                %>
                <div class="categoria">
                <form name="aggiornaCategoriaForm" action="Dispatcher" method="post">
                    <div class="field clearfix">

                        <label>Nome categoria:</label>
                        <input type="text" maxlength="40" name="nomeCategoria" value="<%=c.getNomeCategoria()%>" required>
                    </div>
                    <div class="field clearfix">
                        <label>Descrizione categoria:</label>
                        <textarea name="descrCategoria" cols="140" rows="5" wrap="hard" required maxlength="100"><%=c.getDescrCategoria()%></textarea>
                    </div>
                    <div class="field clearfix">
                        <label>Immagine categoria:</label>
                        <input type="text" maxlength="500" name="immCategoria" value="<%=c.getImmCategoria()%>" required>

                        <input type="submit" class="button normal-button" value="SALVA">
                        <input type="button" class="button normal-button elimina" 
                               onclick="javascript:bloccaCategoria('<%=c.getIdCategoria().toString()%>')" 
                               value="<%=(c.isEliminato()) ? "sblocca" : "blocca"%>">
                    </div>
                    <input type="hidden" name="idCategoria" value="<%=c.getIdCategoria().toString()%>">
                    <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.aggiornaCategoria">
                </form>
                </div>
                <%}}}%>

                <%}%>
                <h2>Crea un nuovo catalogo</h2>
                <form name="creaCatalogoForm" action="Dispatcher" method="post">
                    <div class="field clearfix">
                        <label for="nuovoNomeCatalogo">Nome catalogo:</label>
                        <input type="text" id="nuovoNomeCatalogo" name="nuovoNomeCatalogo" maxlength="40">
                    </div>
                    <div class="field clearfix">
                        <label for="nuovaDescrCatalogo">Descrizione catalogo:</label>
                        <textarea id="nuovaDescrCatalogo" name="nuovaDescrCatalogo" cols="140" rows="5" wrap="hard" required maxlength="100"></textarea>
                    </div>
                    <div class="field clearfix">
                        <label for="nuovaImmCatalogo">Immagine catalogo:</label>
                        <input id="nuovaImmCatalogo" type="text" maxlength="500" name="nuovaImmCatalogo" required>

                        <input type="submit" class="button normal-button" value="CREA">
                    </div>
                    <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.creaCatalogo">
                </form>

                <h2>Crea una nuova categoria</h2>
                <form name="creaCategoriaForm" action="Dispatcher" method="post">
                    <div class="field clearfix">

                        <label>Catalogo di appartenenza:</label>
                        <select name="catalogoApp" required>
                            <%for (Catalogo catalogo : nomiCataloghi) {%>
                            <option value="<%=catalogo.getIdCatalogo()%>"><%=catalogo.getNomeCatalogo()%></option>
                            <%}%>
                        </select>
                    </div>
                    <div class="field clearfix">
                        <label for="nuovoNomeCategoria">Nome categoria:</label>
                        <input type="text" id="nuovoNomeCategoria" name="nuovoNomeCategoria" maxlength="40">
                    </div>
                    <div class="field clearfix">
                        <label for="nuovaDescrCategoria">Descrizione categoria:</label>
                        <textarea id="nuovaDescrCategoria" name="nuovaDescrCategoria" cols="140" rows="5" wrap="hard" required maxlength="100"></textarea>
                    </div>
                    <div class="field clearfix">
                        <label for="nuovaImmCategoria">Immagine categoria:</label>
                        <input id="nuovaImmCategoria" type="text" maxlength="500" name="nuovaImmCategoria" required>

                        <input type="submit" class="button normal-button" value="CREA">
                    </div>
                    <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.creaCategoria">
                </form>
            </div>

            <form name="bloccaCatalogoForm" action="Dispatcher" method="post">
                <input type="hidden" name="idCatalogo">
                <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.bloccaCatalogo">
            </form>

            <form name="bloccaCategoriaForm" action="Dispatcher" method="post">
                <input type="hidden" name="idCategoria">
                <input type="hidden" name="controllerAction" value="AdminGestioneCataloghi.bloccaCategoria">
            </form>

        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>



