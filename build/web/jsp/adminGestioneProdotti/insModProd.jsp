<%@page import="model.mo.Taglie"%>
<%@page import="model.mo.Categoria"%>
<%@page import="model.mo.ImmGalleria"%>
<%@page session="false"%>
<%@page import="model.mo.Vetrina"%>
<%@page import="model.mo.Catalogo"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="java.util.List"%>
<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    List<Vetrina> listaVetrine = (List<Vetrina>) request.getAttribute("listaVetrine");

    String menuAdmin = "gest-prodotti";
    String menuActiveLink = "Profilo Admin";

    String azione = (String) request.getAttribute("azione");
    int i = 0;
    int j = 0;
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

            .prodotto .imgholder {
                width: 100px;
                height: 100px;
                float: left;
            }

            .prodotto table {
                text-align: left;
                border-spacing: 0;
            }

            .prodotto th, td {
                padding: 5px 40px 5px 0;
                vertical-align: top;
            }

            select {
                padding: 4px;
            }

            .taglia-quantita {
                float: right;
            }

            .agg-taglie {
                width: 115px;
            }

            .imm-galleria {
                border-left: solid 1px #e7e7e7;
            }

            input {
                width: auto;
            }

            #prezzo {
                width: 170px;
            }

            #nuovaTaglia {
                width: 50px;
            }

            .annulla {
                background-color: #ff2121;
                float: right;
            }

            .centrale {
                text-align: center;
            }

            .wrapper {
                margin: 0 auto -30px;
            }


        </style>

        <script language = "javascript">
            function eliminaTaglia(t) {
                document.eliminaTagliaForm.tagliaE.value = t;
                document.eliminaTagliaForm.submit();
            }
            function eliminaImm(imm) {
                document.eliminaImmForm.immagine.value = imm;
                document.eliminaImmForm.submit();
            }
        </script>
    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/headerAdmin.inc"%>
            <main class="clearfix">
                <%if ((azione.equals("mod") && prodotto != null) || azione.equals("ins")) {%>
                <h1 class="centrale"><%=(azione.equals("mod")) ? "Modifica Prodotto" : "Inserimento prodotto"%></h1>
                <div class="prodotto clearfix">
                    <form name="insModProdForm" action="Dispatcher" method="post">
                        <div class="taglia-quantita">
                            <h3>Taglia - Quantità</h3>
                            <%if (azione.equals("mod")) {
                                    for (Taglie t : prodotto.getTaglie()) {%>                        
                            <%=t.getTaglia()%> <input type="number" class="agg-taglie" name="disp<%=i%>" required min="0" value="<%=t.getDisponibilita()%>">
                            <a href="javascript:eliminaTaglia('<%=t.getTaglia()%>')">
                                <img alt="Rimuovi" src="immagini/rimuovi.png" width="10" height="10" title="Rimuovi"/>
                            </a><br>
                            <input type="hidden" name="taglia<%=i%>" value="<%=t.getTaglia()%>">
                            <%i++;
                                    }
                                }%>
                            <h4>Aggiungi una nuova taglia</h4>
                            <input type="text" id="nuovaTaglia" name="nuovaTaglia" maxlength="3" placeholder="Taglia" <%=(azione.equals("ins")) ? "required" : ""%>>
                            <input type="number" class="agg-taglie" name="nuovaDisp" placeholder="Disponibilita" min="0" <%=(azione.equals("ins")) ? "required" : ""%>>

                        </div>

                        <table>
                            <tr>
                                <th>Nome prodotto</th>
                                <th>Colore</th>
                                <th>Marca</th>
                            </tr>

                            <tr>
                                <td>
                                    <input type="text" id="nome-prod" name="nomeProd"
                                           value="<%=(azione.equals("mod")) ? prodotto.getNomeProdotto() : ""%>"
                                           required size="20" maxlength="50"/>
                                </td>
                                <td>
                                    <input type="text" id="colore" name="colore" size="20" maxlength="50" required
                                           value="<%=(azione.equals("mod")) ? prodotto.getColore() : ""%>">
                                </td>
                                <td>
                                    <input type="text" id="marca" name="marca" size="20" maxlength="40" required
                                           value="<%=(azione.equals("mod")) ? prodotto.getMarca() : ""%>">
                                </td>
                            </tr>
                        </table>



                        <table>
                            <tr>
                                <th>Categoria</th>
                                <th>Prezzo</th>
                                <th>Aggiungi a vetrina</th>
                            </tr>
                            <tr>
                                <td>
                                    <select name="categoria" required>
                                        <%for (Catalogo catalogo : listaCataloghi) {%>
                                        <optgroup label="<%=catalogo.getNomeCatalogo()%>">
                                            <%for (Categoria c : catalogo.getCategorie()) {%>
                                            <option value="<%=c.getIdCategoria()%>"
                                                    <%=(azione.equals("mod") && (c.getIdCategoria() == prodotto.getCategorie().getIdCategoria())) ? "selected" : ""%> >
                                                <%=c.getNomeCategoria()%>
                                            </option>
                                            <%}%> 
                                        </optgroup>
                                        <%}%>
                                    </select>
                                </td>
                                <td>
                                    <input type="text" id="prezzo" name="prezzo" required
                                           value="<%=(azione.equals("mod")) ? prodotto.getPrezzo() : ""%>"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           >&euro;
                                </td>
                                <td>
                                    <select name="vetrina" required>
                                        <%for (Vetrina v : listaVetrine) {%>
                                        <option value="<%=v.getIdVetrina()%>"
                                                <%=(azione.equals("mod") && (v.getIdVetrina() == prodotto.getVetrine().getIdVetrina())) ? "selected" : ""%>>
                                            <%=v.getNomeVetrina()%>
                                        </option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                        <table>
                            <tr>
                                <th>Immagine anteprima</th>
                                <th>Immagini prodotto</th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" id="imm-anteprima" name="immAnteprima" required
                                           value="<%=(azione.equals("mod")) ? prodotto.getImmAnteprima() : ""%>">
                                </td>
                                <td>
                                    <%if (azione.equals("mod")) {
                                            for (ImmGalleria imm : prodotto.getImmGalleria()) {%>
                                    <input type="text" id="imm-galleria" name="imm<%=j%>" maxlength="250" required
                                           value="<%=imm.getImmagine()%>">
                                    <a href="javascript:eliminaImm('<%=imm.getImmagine()%>')">
                                        <img alt="Rimuovi" src="immagini/rimuovi.png" width="10" height="10" title="Rimuovi"/>
                                    </a>
                                    <%j++;
                                            }
                                        }%>
                                    <h5>Aggiungi una nuova immagine</h5>
                                    <input type="text" name="nuovaImm" maxlength="250" <%=(azione.equals("ins")) ? "required" : ""%> >

                                </td>
                            </tr>
                        </table>

                        <table>
                            <tr>
                                <th colspan="2">Descrizione prodotto</th>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <textarea name="descrizione" cols="120" rows="10" wrap="hard" required="" maxlength="10000"><%=(azione.equals("mod")) ? prodotto.getDescrProdotto() : ""%></textarea>
                                </td>

                            </tr>

                            <tr>
                                <td>
                                    <input type="submit" value="SALVA" class="button normal-button">
                                </td>
                                <td>
                                    <input type="button" value="ANNULLA" class="button normal-button annulla"
                                           onclick="javascript:annullaForm.submit()">
                                </td>
                            </tr>
                        </table>
                        <%if (azione.equals("mod")) {%>
                        <input type="hidden" name="idProdotto" value="<%=prodotto.getIdProdotto()%>">
                        <%}%>
                        <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.<%=(azione.equals("mod")) ? "modificaProdotto" : "inserisciProdotto"%>">
                    </form>
                </div>
                <%if (azione.equals("mod")) {%>
                <form name="eliminaImmForm" action="Dispatcher" method="post">
                    <input type="hidden" name="immagine"/>   
                    <input type="hidden" name="idProdotto" value="<%=prodotto.getIdProdotto()%>">
                    <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.eliminaImmagine"/>
                </form>

                <form name="eliminaTagliaForm" action="Dispatcher" method="post">
                    <input type="hidden" name="tagliaE"/>   
                    <input type="hidden" name="idProdotto" value="<%=prodotto.getIdProdotto()%>">
                    <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.eliminaTaglia"/>
                </form>
                <%}%>

                <form name="annullaForm" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="AdminGestioneProdotti.vediProdotti"/>
                </form>
                <%} else {%>
                <h1>Prodotto non disponibile</h1>
                <%}%>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>


