<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page session="false"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.mo.Categoria"%>
<%@page import="model.mo.Catalogo"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="java.util.List"%>
<%@page import="model.session.mo.UtenteLoggato"%>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    UtenteLoggato utenteLoggato = (UtenteLoggato) request.getAttribute("utenteLoggato");

    boolean esisteCarrello = (Boolean) request.getAttribute("esisteCarrello");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    int quantitaCarrello = (int) request.getAttribute("quantitaCarrello");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String nomeCatalogo = (String) request.getAttribute("nomeCatalogo");

    Categoria[] categorie = (Categoria[]) request.getAttribute("categorie");
    String categoriaSelezionata = (String) request.getAttribute("categoriaSelezionata");
    boolean catIsSelected;
    if ((categoriaSelezionata != null) && (!categoriaSelezionata.equals("null"))) {
        catIsSelected = true;
    } else {
        catIsSelected = false;
    }

    String prezzoScelto = (String) request.getAttribute("prezzoScelto");
    boolean priceIsSelected;
    if ((prezzoScelto != null) && (!prezzoScelto.equals("null"))) {
        priceIsSelected = true;
    } else {
        priceIsSelected = false;
    }

    String listaMarcheSelezionate = (String) request.getAttribute("listaMarcheSelezionate");

    String listaTaglieSelezionate = (String) request.getAttribute("listaTaglieSelezionate");

    List<String> listaTaglie = new ArrayList<>();
    String[] values;
    if (listaTaglieSelezionate != null && !listaTaglieSelezionate.equals("null")) {
        values = listaTaglieSelezionate.split(",");
        listaTaglie = new ArrayList<>(Arrays.asList(values));
    } else {
        listaTaglie.clear();
    }

    List<String> listaTaglieProdotti = (List<String>) request.getAttribute("listaTaglieProdotti");

    List<String> listaMarcheDisponibili = (List<String>) request.getAttribute("listaMarcheDisponibili");

    List<Prodotto> listaProdottiFiltrati = (List<Prodotto>) request.getAttribute("listaProdottiFiltrati");
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = nomeCatalogo;

    int i = 0;
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <style>
            .container {
                margin: 0px -10px;
            }

            .categorie {
                margin: 0px 0px 20px 0px;
            }

            .categoria {
                position: relative;
                width: 275px;
                height: 200px;
            }

            .titoloCategoria{
                margin: 10px 10px 10px 3px;
            }

            #nomeCategoria {
                text-align: left;
            }

            .visualizzaprodotti {
                margin: 10px 0px 10px 0px;
            }

            label {
                float: none;
            }

            .filtri {
                float: left;
                border-style: solid;
                border-width: 1px 1px 0px 1px;
                border-color: #e7e7e7;
                width: 260px;
                margin: 10px 10px 20px 0px;
            }

            .contenitoreFiltri {
                margin-bottom: 0px;
            }

            .nomeFiltro {
                border-bottom: solid 1px #e7e7e7;
                padding-left: 10px;
                background-color: #f8f8f8;
                border-radius: 0px;
            }

            .contenitoreValori {
                padding: 5px 5px 0px 5px;
                border-bottom: solid 1px #e7e7e7;
                border-radius: 0px;
            }

            .valoreFiltro {
                margin-left: 5px;
                margin-bottom: 5px;
            }

            input{
                width: auto;
            }

            .button {
                margin-top: 10px;
            }

            .prodotti {
                margin: 10px 0px 10px 10px;
                float: right;
                width: 75%;
            }

            .prodContainer > article {
                margin: 0px 3px 6px 3px;
            }

            .imgholder {
                width: 100%;
            }

            .prodotto {
                width: 284px;
            }

            .selezionato {
                background-color: #ff2121;
            }
        </style>

        <script language="javascript">

            function scegliCategoria(nomeCategoria) {
                document.impostaFiltriForm.categoriaSelezionata.value = nomeCategoria;
                if (<%=priceIsSelected%>) {
                    document.impostaFiltriForm.prezzoScelto.value = "<%=prezzoScelto%>";
                } else {
                    document.impostaFiltriForm.prezzoScelto.value = "null";
                }
                document.impostaFiltriForm.marcaSelezionata.value = "null";
                document.impostaFiltriForm.tagliaSelezionata.value = "null";

                document.impostaFiltriForm.submit();
            }

            function scegliPrezzo(prezzo) {
                document.impostaFiltriForm.prezzoScelto.value = prezzo;
                if (<%=catIsSelected%>) {
                    document.impostaFiltriForm.categoriaSelezionata.value = "<%=categoriaSelezionata%>";
                } else {
                    document.impostaFiltriForm.categoriaSelezionata.value = "null";
                }
                document.impostaFiltriForm.marcaSelezionata.value = "null";
                document.impostaFiltriForm.tagliaSelezionata.value = "null";

                document.impostaFiltriForm.submit();
            }

            function aggiungiMarca(marca) {
                if (<%=priceIsSelected%>) {
                    document.impostaFiltriForm.prezzoScelto.value = "<%=prezzoScelto%>";
                } else {
                    document.impostaFiltriForm.prezzoScelto.value = "null";
                }

                if (<%=catIsSelected%>) {
                    document.impostaFiltriForm.categoriaSelezionata.value = "<%=categoriaSelezionata%>";
                } else {
                    document.impostaFiltriForm.categoriaSelezionata.value = "null";
                }
                document.impostaFiltriForm.tagliaSelezionata.value = "null";
                document.impostaFiltriForm.marcaSelezionata.value = marca;
                document.impostaFiltriForm.submit();
            }

            function aggiungiTaglia(taglia) {
                if (<%=priceIsSelected%>) {
                    document.impostaFiltriForm.prezzoScelto.value = "<%=prezzoScelto%>";
                } else {
                    document.impostaFiltriForm.prezzoScelto.value = "null";
                }

                if (<%=catIsSelected%>) {
                    document.impostaFiltriForm.categoriaSelezionata.value = "<%=categoriaSelezionata%>";
                } else {
                    document.impostaFiltriForm.categoriaSelezionata.value = "null";
                }
                document.impostaFiltriForm.marcaSelezionata.value = "null";
                document.impostaFiltriForm.tagliaSelezionata.value = taglia.toString();
                document.impostaFiltriForm.submit();
            }


            function visualizzaProdotto(idProdotto) {
                document.prodottoForm.idProdotto.value = idProdotto;
                document.prodottoForm.submit();
            }
        </script>

    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main>
            <section class="categorie clearfix">
                <div class="container clearfix">
                    <%for (Categoria c : categorie) {%>
                    <article>
                        <a href="javascript:scegliCategoria('<%="" + c.getNomeCategoria() + ""%>')">
                            <div class="categoria clearfix">
                                <div class="descrizione">
                                    <p><%=c.getDescrCategoria()%></p>
                                </div>
                                <img class="centraImmagine" src="immagini/<%=c.getImmCategoria()%>" width="160" height="160"/>
                                <input type="button" name="<%=c.getNomeCategoria()%>" value="<%=c.getNomeCategoria()%>"
                                       class="button <%=((categoriaSelezionata != null) && (categoriaSelezionata.equals(c.getNomeCategoria()))) ? "selezionato" : ""%>" >

                            </div>                   
                        </a>
                    </article>
                    <%}%>                    
                </div>
            </section>

            <section class="visualizzaprodotti clearfix">
                <div class="">
                    <div class="filtri">
                        <div class="contenitoreFiltri">
                            <div class="nomeFiltro">
                                <h2>Marca</h2>
                            </div>
                            <div class="contenitoreValori">
                                <%for (String marca : listaMarcheDisponibili) {%>
                                <div class="valoreFiltro">
                                    <input type="checkbox" name="<%=marca%>" value="<%=marca%>" onclick="javascript:aggiungiMarca('<%=marca%>')"
                                           <%=((listaMarcheSelezionate != null && !listaMarcheSelezionate.isEmpty()) && (listaMarcheSelezionate.contains(marca))) ? "checked" : ""%>
                                           >
                                    <label><%=marca%></label>
                                </div>
                                <%}%>
                            </div>

                            <div class="nomeFiltro">
                                <h2>Taglia</h2>
                            </div>
                            <div class="contenitoreValori">
                                <%for (String taglia : listaTaglieProdotti) {%>
                                <div class="valoreFiltro">
                                    <input type="checkbox" name="<%=taglia%>" value="<%=taglia%>" onclick="javascript:aggiungiTaglia('<%=taglia%>')"
                                           <%=((listaTaglie != null && !listaTaglie.isEmpty()) && (listaTaglie.contains(taglia))) ? "checked" : ""%>
                                           >
                                    <label><%=taglia%></label>
                                </div>
                                <%}%>
                            </div>

                            <div class="nomeFiltro">
                                <h2>Prezzo</h2>
                            </div>
                            <div class="contenitoreValori">
                                <div class="valoreFiltro">
                                    <input type="radio" name="prezzo"
                                           <%=((prezzoScelto != null && !prezzoScelto.equals("null")) && (prezzoScelto.equals("meno50")) ? "checked" : "")%>
                                           onclick="javascrispt:scegliPrezzo('meno50')">
                                    <label>Meno di 50&euro;</label>
                                </div>
                                <div class="valoreFiltro">
                                    <input type="radio" name="prezzo" onclick="javascrispt:scegliPrezzo('tra50e100')"
                                           <%=((prezzoScelto != null && !prezzoScelto.equals("null")) && (prezzoScelto.equals("tra50e100")) ? "checked" : "")%>
                                           >
                                    <label>50 - 100 &euro;</label>
                                </div>
                                <div class="valoreFiltro">
                                    <input type="radio" name="prezzo" onclick="javascrispt:scegliPrezzo('tra100e150')"
                                           <%=((prezzoScelto != null && !prezzoScelto.equals("null")) && (prezzoScelto.equals("tra100e150")) ? "checked" : "")%>
                                           >
                                    <label>100 - 150 &euro;</label>
                                </div>
                                <div class="valoreFiltro">
                                    <input type="radio" name="prezzo" onclick="javascrispt:scegliPrezzo('piu150')"
                                           <%=((prezzoScelto != null && !prezzoScelto.equals("null")) && (prezzoScelto.equals("piu150")) ? "checked" : "")%>
                                           >
                                    <label>Maggiore di 150&euro;</label>
                                </div>
                            </div>
                        </div>                                          
                    </div>


                    <section class="prodotti clearfix">
                        <section class="titoloCategoria">
                            <div id="nomeCategoria">
                                <h1><%=(categoriaSelezionata == null || categoriaSelezionata.equals("null")) ? "" : categoriaSelezionata%></h1>
                            </div>
                        </section>

                        <%if (listaProdottiFiltrati != null && !listaProdottiFiltrati.isEmpty()) {%>
                        <div class="prodContainer clearfix">
                            <%for (Prodotto prodotto : listaProdottiFiltrati) {%>
                            <article>
                                <a href="javascript:visualizzaProdotto('<%=prodotto.getIdProdotto()%>')">
                                    <div class="prodotto">
                                        <div class="imgholder clearfix">
                                            <img class="centraImmagine" src="immagini/<%=prodotto.getImmAnteprima()%>" width="250" height="250" title="<%=prodotto.getNomeProdotto()%>"/>
                                        </div>
                                        <div class="infoprod">
                                            <p><b><%=prodotto.getNomeProdotto()%></b></p>
                                            <p><%=prodotto.getColore()%></p>
                                            <h1><%=prodotto.getPrezzo()%>&euro;</h1>
                                        </div>
                                    </div>
                                </a>
                            </article>
                            <%}%>                                             
                        </div>
                        <%} else {%>
                        <h2>Non sono presenti prodotti</h2>
                        <a href="javascript:visualizzaCatalogo('<%=nomeCatalogo%>')">
                            <input type="button" class="button normal-button" value="azzera filtri">
                        </a>
                        <%}%>
                    </section>
            </section>

            <form name="impostaFiltriForm" action="Dispatcher" method="post">
                <input type="hidden" name="nomeCatalogo" value="<%=nomeCatalogo%>"/>
                <input type="hidden" name="categoriaSelezionata"/>
                <input type="hidden" name="prezzoScelto"/>
                <input type="hidden" name="marcaSelezionata"/>
                <input type="hidden" name="listaMarcheSelezionate" value="<%=listaMarcheSelezionate%>"/>                
                <input type="hidden" name="tagliaSelezionata"/>
                <input type="hidden" name="listaTaglieSelezionate" value="<%=listaTaglieSelezionate%>" />                
                <input type="hidden" name="controllerAction" value="HomePage.visualizzaCatalogo"/>
            </form>

            <form name="prodottoForm" action="Dispatcher" method="post">
                <input type="hidden" name="idProdotto"/>   
                <input type="hidden" name="nomeCatalogo" value="<%=nomeCatalogo%>"/>
                <input type="hidden" name="controllerAction" value="HomePage.visualizzaProdotto"/>
            </form>

        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
