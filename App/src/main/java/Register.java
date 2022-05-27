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

@WebServlet("/Register")
public class Register extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Register() {
		super();
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
	 	
	 	JSONArray resultados = new JSONArray();
	 	
	 	String carnet = request.getParameter("carnet");
	 	String carrera = request.getParameter("carrera");
	 	String edad = request.getParameter("edad");
	 	String email = request.getParameter("email");
	 	String instagram = request.getParameter("instagram");
	 	String nombre = request.getParameter("nombre");
	 	String sexo = request.getParameter("sexo");
	 	String gusto1 = request.getParameter("gusto1");
	 	String gusto2 = request.getParameter("gusto2");
	 	String gusto3 = request.getParameter("gusto3");
	 	String gusto4 = request.getParameter("gusto4");
	 	String gusto5 = request.getParameter("gusto5");
	 	
	 	 try ( EmbeddedNeo4j greeter = new EmbeddedNeo4j( "bolt://localhost:7687", "neo4j", "221756" ) )
	        {
			 	boolean resultado = greeter.registrarNuevo(carnet, carrera, edad, email, instagram, nombre, sexo, gusto1, gusto2, gusto3, gusto4, gusto5);
			 	String registro = "";
			 	
			 	if(resultado) {
			 		registro = "Registro correcto";
			 	}else {
			 		registro = "Registro incorrecto";
			 	}
			 	resultados.add(registro);
	        	
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	 myResponse.put("resultado", resultados);
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
