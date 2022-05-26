

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


import dataAccessLayer.EmbeddedNeo4j;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
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
		 	
		 	JSONArray nombreUsuarios = new JSONArray();
		 	
		 	 try ( EmbeddedNeo4j greeter = new EmbeddedNeo4j( "bolt://localhost:7687", "neo4j", "221756" ) )
		        {
				 	LinkedList<String> usuarios = greeter.getRegistrados();
				 	
				 	for (int i = 0; i < usuarios.size(); i++) {
				 		 //out.println( "<p>" + myactors.get(i) + "</p>" );
				 		nombreUsuarios.add(usuarios.get(i));
				 	}
		        	
		        } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	
		 	myResponse.put("usuarios", nombreUsuarios);
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
