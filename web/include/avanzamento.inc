         
<section class="avanzamento clearfix">
    <ul>

        <li <%= (action . equals("spedizione")) ? "class=\"focus\"" : "" %>>SPEDIZIONE</li>
        <li <%= (action . equals("pagamento")) ? "class=\"focus\"" : "" %>>PAGAMENTO</li>
        <li <%= (action . equals("conferma")) ? "class=\"focus\"" : "" %>>CONFERMA ORDINE</li>
    </ul>
</section>
