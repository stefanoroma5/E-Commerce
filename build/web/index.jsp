<%@page session="false"%>
<!DOCTYPE HTML>
<html lang="it-IT">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="0; url=/E-Commerce/Dispatcher">
    <script type="text/javascript">
      function onLoadHandler() {
        window.location.href = "/E-Commerce/Dispatcher";
      }
      window.addEventListener("load", onLoadHandler);
    </script>
    <title>Page Redirection</title>
  </head>
  <body>
    If you are not redirected automatically, follow the <a href='/E-Commerce/Dispatcher'>link</a>
  </body>
</html>