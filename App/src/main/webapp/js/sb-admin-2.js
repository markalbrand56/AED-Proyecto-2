(function($) {
  "use strict"; // Start of use strict

		
	//Evento del botón que creara una nueva pelicula
	$("#btn_mostrarusuarios").click(

    function (){
  
          $.ajax( {
              
              type: "GET",
              url: '/HelloWorld/HelloServlet',
              success: function(data) {
                  //alert("Result" + data.resultado);
                  var htmlUsersList = '<ol id="lista4">';
                  $.each(data.usuarios, function(i,item){
                      htmlUsersList += '<li>' + item + '</li>';
                  });
                  htmlUsersList += '</ol>';
                  $('#titulo').html("");
                  $('#titulo').append("<h3>¡Estos son nuestros matchers!</h3>");
                  $('#div-listado-usuarios').html("");
                  $('#div-listado-usiarios').append(htmlUsersList);
                 
              }
          } );
      }

  );

  	//Evento del botón que me devuelve el listado de películas de un determinado actor
	$("#btn_match").click(
    function(){		
		$.ajax( {
			
			type: "GET",
			url: '/HelloWorld/Recomendacion?carnet=' + $('#carnet').val(),
			success: function(data) {
				//alert("Result" + data.resultado);
			    var htmlMatchList = '<ol id="lista4">';
				$.each(data.recomendaciones, function(i,item){
					  htmlMatchList += '<li>' + item + '</li>';
				});
				htmlMatchList += '</ol>';
                $('#titulo').html("");
				$('#titulo').append("<h3>¡Estos son tus matchs!</h3>");
				$('#div-listado-match').html("");
				$('#div-listado-match').append(htmlMatchList);
			}
		} );
		
		
	});



})(jQuery); // End of use strict
