/*!
* Start Bootstrap - One Page Wonder v6.0.5 (https://startbootstrap.com/theme/one-page-wonder)
* Copyright 2013-2022 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-one-page-wonder/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project
 //Evento del botón que me devuelve el listado de actores
 (function mostrar(){
        alert("The button was clicked 1");
                
        $.ajax( {
            
            type: "GET",
            url: '/HelloWorld/HelloServlet',
            success: function(data) {
                //alert("Result" + data.resultado);
                var htmlActorsList = '<ul>';
                $.each(data.actores, function(i,item){
                    htmlActorsList += '<li>' + item + '</li>';
                });
                htmlActorsList += '</ul>';
                $('#div-listado-actores').html("");
                $('#div-listado-actores').append(htmlActorsList);
            }
        } );
        
        
    });
  (jQuery); 