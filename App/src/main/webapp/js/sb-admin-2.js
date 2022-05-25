(function($) {
  "use strict"; // Start of use strict

	
	//Evento del botón que me devuelve el listado de películas de un determinado actor
	$("#btn-search-movies-by-actor").click(function(){
				
		$.ajax( {
			
			type: "GET",
			url: '/HelloWorld/MoviesByActor?actor_name=' + $('#txt-actor').val(),
			success: function(data) {
				//alert("Result" + data.resultado);
			    var htmlMovieList = '<ul>';
				$.each(data.peliculas, function(i,item){
					  htmlMovieList += '<li>' + item + '</li>';
				});
				htmlMovieList += '</ul>';
				$('#div-listado-actores').html("");
				$('#div-listado-actores').append(htmlMovieList);
			}
		} );
		
		
	});
	
	
	//Evento del botón que creara una nueva pelicula
	$("#btn_mostraractores").click(

    function (){
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
      }

  );



})(jQuery); // End of use strict
