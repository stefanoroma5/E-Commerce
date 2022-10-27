<%@page import="model.mo.Catalogo"%>
<%@page session="false"%>
<%@page import="model.mo.Indirizzi"%>
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

    List<Indirizzi> indirizzi = (List<Indirizzi>) request.getAttribute("indirizzi");
    
    List<Catalogo> listaCataloghi = (List<Catalogo>) request.getAttribute("listaCataloghi");

    String menuActiveLink = "Profilo Utente";

    String menu = "indirizzi";

    int i = 0;
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

            .indirizzi {
                float: left;
                width: 50%;
            }

            h2 {
                text-align: center;
            }

            input[type="radio"] {
                width: auto;
            }

            .elimina {
                background-color: #ff2121;
                float: right;
            } 

        </style>

        <script language="javascript">
            function eliminaIndirizzo(idIndirizzo) {
                document.eliminaIndirizzoForm.idindirizzo.value = idIndirizzo;
                document.eliminaIndirizzoForm.submit();
            }

            function selezionaIndirizzo(idIndirizzo) {
                document.selezionaIndirizzoForm.idindirizzo.value = idIndirizzo;
                document.selezionaIndirizzoForm.submit();
            }
        </script>

    </head>
    <body>
        <div class="wrapper">
            <%@include file="/include/header.inc"%>
            <main class="clearfix">
                <%@include file="/include/menu-utente.inc"%>
                <div class="info-profilo clearfix">    
                    <%if(!indirizzi.isEmpty()) {%>
                    <div class="indirizzi">
                        <h2>I TUOI INDIRIZZI</h2>
                        <%for (Indirizzi ind : indirizzi) {%>
                        <form name="modificaIndirizzoForm<%=i%>" action="Dispatcher" method="post">
                            <div class="dati-account">
                                <div class="field clearfix">
                                    <label for="alias">Alias Indirizzo</label>
                                    <input type="text" id="alias" name="alias"
                                           value="<%=ind.getAlias()%>"
                                           required size="20" maxlength="50"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="indirizzo">Indirizzo</label>
                                    <input type="text" id="indirizzo" name="indirizzo"
                                           value="<%=ind.getIndirizzo()%>"
                                           required size="20" maxlength="100"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="citta">Citt&aacute;</label>
                                    <input type="text" id="citta" name="citta"
                                           value="<%=ind.getCitta()%>"
                                           required size="20" maxlength="60"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="provincia">Provincia</label>
                                    <input type="text" id="provincia" name="provincia"
                                           value="<%=ind.getProvincia()%>"
                                           required size="2" maxlength="2"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="cap">CAP</label>
                                    <input type="text" id="cap" name="cap"
                                           value="<%=ind.getCap().toString()%>"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           required size="5" maxlength="5"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="tel1">Telefono 1</label>
                                    <input type="text" id="tel1" name="tel1"
                                           value="<%=ind.getTelefono1()%>"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           required size="20" maxlength="40"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="tel2">Telefono 2</label>
                                    <input type="text" id="tel2" name="tel2"
                                           value="<%=((ind.getTelefono2() != null) && !(ind.getTelefono2().isEmpty())) ? ind.getTelefono2() : ""%>"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           size="20" maxlength="40"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="selezionato">Selezionato</label>
                                    <input type="radio" id="selezionato" name="selezionato" <%=(ind.isSelezionato()) ? "checked" : ""%>
                                           onclick="javascript:selezionaIndirizzo('<%=ind.getIdIndirizzo()%>')">
                                </div>
                                <div class="field clearfix">
                                    <label>&#160;</label>
                                    <input type="submit" class="button normal-button" value="AGGIORNA"/>
                                    <input type="button" class="button normal-button elimina" value="ELIMINA"
                                           onclick="javascript:eliminaIndirizzo('<%=ind.getIdIndirizzo()%>')">
                                </div>
                            </div>
                            <input type="hidden" name="idindirizzo" value="<%=ind.getIdIndirizzo().toString()%>">
                            <input type="hidden" name="controllerAction" value="ProfiloUtente.modificaIndirizzo"/>
                        </form>
                        <%i++;
                            }%>                        
                    </div>
                    <%}%>

                    <form name="eliminaIndirizzoForm" action="Dispatcher" method="post">
                        <input type="hidden" name="idindirizzo">
                        <input type="hidden" name="controllerAction" value="ProfiloUtente.eliminaIndirizzo"/>
                    </form>

                    <form name="selezionaIndirizzoForm" action="Dispatcher" method="post">
                        <input type="hidden" name="idindirizzo">
                        <input type="hidden" name="controllerAction" value="ProfiloUtente.selezionaIndirizzo"/>
                    </form>

                    <div class="indirizzi">
                        <h2>INSERISCI UN NUOVO INDIRIZZO</h2>
                        <div class="dati-account">
                            <form name="inserisciIndirizzoForm" action="Dispatcher" method="post">
                                <div class="field clearfix">
                                    <label for="newalias">Alias Indirizzo</label>
                                    <input type="text" id="newalias" name="alias"
                                           required size="20" maxlength="50"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="newindirizzo">Indirizzo</label>
                                    <input type="text" id="newindirizzo" name="indirizzo"
                                           required size="20" maxlength="100"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="newcitta">Citt&aacute;</label>
                                    <input type="text" id="newcitta" name="citta"
                                           required size="20" maxlength="60"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="newprovincia">Provincia</label>
                                    <input type="text" id="newprovincia" name="provincia"
                                           required size="2" maxlength="2"/>
                                </div>
                                <div class="field clearfix">    
                                    <label for="newcap">CAP</label>
                                    <input type="text" id="newcap" name="cap"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           required size="5" maxlength="5"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="newtel1">Telefono 1</label>
                                    <input type="text" id="newtel1" name="tel1"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           required size="20" maxlength="40"/>
                                </div>
                                <div class="field clearfix">
                                    <label for="newtel2">Telefono 2</label>
                                    <input type="text" id="newtel2" name="tel2"
                                           oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyDown="limitText(this,10);" onKeyUp="limitText(this,10);"
                                           size="20" maxlength="40" placeholder="Opzionale"/>
                                </div>
                                <div class="field clearfix">
                                    <label>&#160;</label>
                                    <input type="submit" class="button normal-button" value="AGGIUNGI"/>
                                </div>
                                
                                <input type="hidden" name="controllerAction" value="ProfiloUtente.aggiungiIndirizzo"/>
                            </form>
                        </div>
                    </div>
                </div>
            </main>
            <div class="push"></div>
        </div>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
