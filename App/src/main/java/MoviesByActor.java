

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dataAccessLayer.EmbeddedNeo4j;

import org.json.simple.JSONArray;

/**
 * Servlet implementation class MoviesByActor
 */
@WebServlet("/Recomendacion")
public class MoviesByActor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoviesByActor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
	 	response.setContentType("application/json");
	 	response.setCharacterEncoding("UTF-8");
	 	JSONObject myResponse = new JSONObject();
	 	
	 	JSONArray recomendaciones = new JSONArray();
	 	
	 	String usuario = request.getParameter("carnet");
	 	 try ( EmbeddedNeo4j greeter = new EmbeddedNeo4j( "bolt://localhost:7687", "neo4j", "221756" ) )
	        {
			 	LinkedList<String> usuariosRecomendados = greeter.getRecomendacion(usuario);
			 	
			 	for (int i = 0; i < usuariosRecomendados.size(); i++) {
			 		 //out.println( "<p>" + myactors.get(i) + "</p>" );
			 		recomendaciones.add(usuariosRecomendados.get(i));
			 	}
	        	
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	
	 	 myResponse.put("recomendaciones", recomendaciones);
	 	out.println(myResponse);
	 	out.flush();  
	 	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
