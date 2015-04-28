# Introduktion #

RSS-portleten används för att visa RSS-flöden i en portal. Vilka flöden som ska visas konfigureras i portletens inställningar. Det finns även möjlighet att konfigurera hur många nyheter som ska visas (dock max 20 st oavsett inställning).

Nyheterna visas i en elegant design och det finns möjlighet att expandera/minimera diverse texter samt sortera på datum och titel.

# Teknik #

Portleten är utformad enligt [JSR 286](http://jcp.org/en/jsr/detail?id=286) med hjälp av Spring Portlet MVC. Integrationen mot notes sker via en RESTfull webservice som anropas mha Springs [RestTemplate](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/web/client/RestTemplate.html).

För parsning av RSS-flödets XML och konvertering till objektmodell används [Rome](https://rome.dev.java.net/).

I vylagret används javascriptbiblioteket [jQuery](http://jquery.com) för grafiska effekter och underlättande av AJAX-hantering.