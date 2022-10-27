<div class="menu-profilo">
    <nav>
        <div class="menu">
            <ul>
                <li <%= (menu . equals("dati-account")) ? "class=\"focus\"" : "" %>>
                    <a href="Dispatcher?controllerAction=ProfiloUtente.datiAccount">Dati account</a>
                </li>
                <li <%= (menu . equals("indirizzi")) ? "class=\"focus\"" : "" %>>
                    <a href="Dispatcher?controllerAction=ProfiloUtente.indirizziAccount">Indirizzi</a>
                </li>
                <li <%= (menu . equals("ordini")) ? "class=\"focus\"" : "" %>>
                    <a href="Dispatcher?controllerAction=ProfiloUtente.ordini">Ordini effettuati</a>
                </li>
            </ul>
        </div>
    </nav>
</div>
