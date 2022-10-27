<%@page session="false"%>
<%@page import="model.mo.Catalogo"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.mo.Vetrina"%>
<%@page import="java.util.List"%>
<%@page import="model.session.mo.UtenteLoggato"%>
<%@page import="model.session.mo.Carrello"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    UtenteLoggato utenteLoggato = (UtenteLoggato) request.getAttribute("utenteLoggato");

    boolean esisteCarrello = (Boolean) request.getAttribute("esisteCarrello");
    List<Carrello> carrello = (List<Carrello>) request.getAttribute("carrello");
    int quantitaCarrello = (int) request.getAttribute("quantitaCarrello");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");
    List<Vetrina> listaVetrine = (List<Vetrina>) request.getAttribute("listaVetrine");

    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>  
        <style>
            .vetrina {  
                padding: 10px 0px 20px 0px;
                margin: 10px 0px;
                background-image: url(immagini/vetrina.jpg);
            }

            .nomevetrina {
                margin: 0px;
            }

            h1.titolo {
                width: min-content;
                background-color: white;
                margin-left: auto;
                margin-right: auto;
                text-transform: uppercase;
            }

            .descrizione > p {
                left: 50%;
                transform: translate(-50%, -50%);
            }
            
            .descrizione {
                height: 90%;
            }

        </style>

        <script language = "javascript">
            function visualizzaProdotto(idProdotto) {
                document.prodottoForm.idProdotto.value = idProdotto;
                document.prodottoForm.submit();
            }
        </script>

    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main>
            <%for (Vetrina vetrina : listaVetrine) {
                    if (!vetrina.getNomeVetrina().equals("-")) {%>
            <section class="vetrina clearfix">

                <div class="nomevetrina">
                    <h1 class="titolo"><%=vetrina.getNomeVetrina()%></h1>
                </div>
                <div class="container">
                    <%for (Prodotto prodotto : vetrina.getProdotti()) {%>
                    <article>
                        <a href="javascript:visualizzaProdotto('<%=prodotto.getIdProdotto()%>')">
                            <div class="imgholder">
                                <img class="centraImmagine" src="immagini/<%=prodotto.getImmAnteprima()%>" width="250" height="250" title="<%=prodotto.getNomeProdotto()%>"/>
                            </div>
                            <div class="infoprod">
                                <p><b><%=prodotto.getNomeProdotto()%></b></p>
                                <p><%=prodotto.getColore()%></p>
                                <h1><%=prodotto.getPrezzo()%>&euro;</h1>
                            </div>
                        </a>

                    </article>
                    <%}%>
                </div>

            </section>
            <%}
                }%>

            <section class="cataloghi clearfix">
                <div class="container clearfix">
                    <%for (Catalogo catalogo : listaCataloghi) {%>
                    <article>
                        <a href="javascript:visualizzaCatalogo('<%=catalogo.getNomeCatalogo()%>')">
                            <div class="catalogo clearfix">
                                <div class="descrizione">
                                    <p><%=catalogo.getDescrCatalogo()%></p>
                                </div>
                                <img class="centraImmagine" src="immagini/<%=catalogo.getImmCatalogo()%>" width="160" height="160"/>
                                <input type="button" name="<%=catalogo.getNomeCatalogo()%>" class="button" value="<%=catalogo.getNomeCatalogo()%>"/>
                            </div>         
                        </a>
                    </article>
                    <%}%>
                </div>               
            </section>

            <form name="prodottoForm" action="Dispatcher" method="post">
                <input type="hidden" name="idProdotto"/>                
                <input type="hidden" name="controllerAction" value="HomePage.visualizzaProdotto"/>
            </form>


        </main>

        <%@include file="/include/footer.inc"%>
    </body>
</html>
