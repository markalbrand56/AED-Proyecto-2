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
                  $('#div-listado-usuarios').append(htmlUsersList);
                 
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

    var carnet = "";
    var sexo = "";
    var nombre = "";
    var carrera = "";
    var edad = "";
    var email = "";
    var instagram = "";
    var hobbie = "";
    var comida = "";
    var lugar = "";
    var musica = "";
    var pelicula = "";
    $("#carnet" ).on('change', function () {
        carnet = $(this).val();
    });
    
    $("#nombre" ).on('change', function () {
        nombre = $(this).val();
    });

    $("#carrera" ).on('change', function () {
        carrera = $(this).val();
    });

    $("#edad" ).on('change', function () {
        edad = $(this).val();
    });

    $("#email" ).on('change', function () {
        email = $(this).val();
    });

    $("#instagram" ).on('change', function () {
        instagram = $(this).val();
    });

    $("#sexo" ).on('change', function () {
        sexo = $(this).attr("id");
    });

    $("input[name='hobbies']" ).on('change', function () {
        hobbie = $('input[name="hobbies"]:checked').attr("id"); 
        alert(hobbie);
    });

    $("input[name='comida']" ).on('change', function () {
        comida = $(this).attr("id");
    });

    $("input[name='lugar']" ).on('change', function () {
        lugar = $(this).attr("id");
    });

    $("input[name='musica']" ).on('change', function () {
        musica = $(this).attr("id"); 
    });

    $("input[name='pelicula']" ).on('change', function () {
        pelicula = $(this).attr("id");
    });

    $("#submit").click(function() {
        alert(carnet);
        alert(sexo); 
        $.ajax({
            type: "GET",
            url: '/HelloWorld/Register?carnet=' + $('#carnet').val() + '&carrera='+ $('#carrera').val() + '&edad=' + $('#edad').val() + '&email=' + $('#email').val() + '&instagram=' + $('#instagram').val() + '&nombre=' + $('#nombre').val() + '&sexo=' + $('input[name="sexo"]:checked').attr("id") + '&gusto1=' + $('input[name="comida"]:checked').attr("id") + '&gusto2=' + $('input[name="lugar"]:checked').attr("id") + '&gusto3=' + $('input[name="musica"]:checked').attr("id") + '&gusto4=' + $('input[name="pelicula"]:checked').attr("id") + '&gusto5=' + $('input[name="hobbies"]:checked').attr("id"),
            success: function(data) {
				//alert("Result" + data.resultado);
			    var htmlMatchList = '<ol id="lista4">';
				$.each(data.resultado, function(i,item){
					  htmlMatchList += '<li>' + item + '</li>';
				});
				htmlMatchList += '</ol>';
                $('#titulo').html("");
				$('#titulo').append("<h3>¡Estos son tus matchs!</h3>");
				$('#div-listado-match').html("");
				$('#div-listado-match').append(htmlMatchList);
			}
        });
    }); 



})(jQuery); // End of use strict
