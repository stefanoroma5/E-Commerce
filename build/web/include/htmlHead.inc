<meta charset="utf-8"/>

<!-- Linking styles -->
<link rel="stylesheet" href="css/soccershop.css" type="text/css" media="screen">
<title>SoccerShop: <%=menuActiveLink%></title>
<script>
  var applicationMessage;
  <%if (applicationMessage != null) {%>
    applicationMessage="<%=applicationMessage%>";
  <%}%>
  function onLoadHandler() {
    try { mainOnLoadHandler(); } catch (e) {}
    if (applicationMessage!==undefined) alert(applicationMessage);
  }
  window.addEventListener("load", onLoadHandler);
</script>
