/**
 * 
 */
package dataAccessLayer;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

import java.util.*;



/**
 * @author Mark Albrand
 * @author Alejandro Azurdia
 * Hecho tomando como base el código suministrado por Moises Alonso.
 */
public class EmbeddedNeo4j implements AutoCloseable{

    private final Driver driver;


    public EmbeddedNeo4j( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                                     "SET a.message = $message " +
                                                     "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    
    public LinkedList<String> getRegistrados()
    {
    	 try ( Session session = driver.session() )
         {
    		 LinkedList<String> registrados = session.readTransaction( new TransactionWork<LinkedList<String>>()
             {
                 @Override
                 public LinkedList<String> execute( Transaction tx )
                 {
                     Result result = tx.run( "MATCH (n:Persona) RETURN n.nombre");
                     LinkedList<String> nombres = new LinkedList<String>();
                     List<Record> registros = result.list();
                     for (int i = 0; i < registros.size(); i++) {
                    	 nombres.add(registros.get(i).get("n.nombre").asString());
                     }
                     
                     return nombres;
                 }
             } );
             
             return registrados;
         }
    }
    
    public LinkedList<String> getRecomendacion(String usuario)
    {

    	try ( Session session = driver.session() )
		    {

			HashMap<String, Integer> hashmapDeQuimica = new HashMap<String, Integer>();
			LinkedList<String> ids = getRegistrados();

			// Obtener el nombre del usuario de la base de datos.
			String nombreUsuario = session.readTransaction( new TransactionWork<String>()
	        	{
		            @Override
		            public String execute( Transaction tx )
		            {
		                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"}) RETURN p.nombre");		         
		                List<Record> registros = result.list();
		                String nombre = registros.get(0).get("p.nombre").asString();
		                return nombre; //devuelve los gustos de un usuario.
		            }
		    } );
			
			// Obtener la carrera del usuario de la base de datos.
			String carreraUsuario = session.readTransaction( new TransactionWork<String>()
        	{
	            @Override
	            public String execute( Transaction tx )
	            {
	                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"}) RETURN p.carrera");		         
	                List<Record> registros = result.list();
	                String nombre = registros.get(0).get("p.carrera").asString();
	                return nombre; //devuelve los gustos de un usuario.
	            }
        	} );
			
			// Obtener el sexo del usuario de la base datos.
			String sexoUsuario = session.readTransaction( new TransactionWork<String>()
        	{
	            @Override
	            public String execute( Transaction tx )
	            {
	                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"}) RETURN p.sexo");		         
	                List<Record> registros = result.list();
	                String nombre = registros.get(0).get("p.sexo").asString();
	                return nombre; //devuelve los gustos de un usuario.
	            }
        	} );
			
			// Obtener la edad del usuario de la base de datos.
			String edadTemp = session.readTransaction( new TransactionWork<String>()
        	{
	            @Override
	            public String execute( Transaction tx )
	            {
	                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"}) RETURN p.edad");		         
	                List<Record> registros = result.list();
	                String nombre = registros.get(0).get("p.edad").asString();
	                return nombre; //devuelve los gustos de un usuario.
	            }
        	} );

            int edadUsuario = Integer.parseInt(edadTemp); // Convertir la edad a int.
			
			
			
            for (int i = 0; i < ids.size(); i++) { // Filtrar la lista de ids obtenida de la base de datos para quitar al usuario.
                if(ids.get(i).equals(nombreUsuario)){
                    ids.remove(i);
                }
            }

            for (int i = 0; i < ids.size(); i++) { // ConstrucciÃ³n de un hashmap con la siguiente estructura: key (nombre de usuario), value (puntuaciÃ³n).
                if (!ids.get(i).equals(nombreUsuario)){
                    hashmapDeQuimica.put(ids.get(i), 0);
                }
            }
			 
			 // Obtener los gustos del usuario.
			 LinkedList<String> gustosUsuario = session.readTransaction( new TransactionWork<LinkedList<String>>()
			 	{
		            @Override
		            public LinkedList<String> execute( Transaction tx )
		            {
		                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
		            LinkedList<String> gustos = new LinkedList<String>();
		            List<Record> registros = result.list();
		            for (int i = 0; i < registros.size(); i++) {
                        gustos.add(registros.get(i).get("gustos.titulo").asString());
		            }
		
		            return gustos; //devuelve los gustos de un usuario.
		            }
			 	} );


            // Inicio del ciclo para comparar los gustos de los demás (registrados) con los gustos del usuario.

                for (int usuarioActual = 0; usuarioActual < ids.size(); usuarioActual++) {

            	String nombreRegistrado = ids.get(usuarioActual); // nombre de registrado.

                LinkedList<String> gustosDeRegistrado = session.readTransaction( new TransactionWork<LinkedList<String>>()
                {
                    @Override
                    public LinkedList<String> execute( Transaction tx )
                    {
                        Result result = tx.run( "MATCH(p:Persona {nombre:\"" + nombreRegistrado + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
                        LinkedList<String> gustos = new LinkedList<String>();
                        List<Record> registros = result.list();
                        for (int i = 0; i < registros.size(); i++) {
                            gustos.add(registros.get(i).get("gustos.titulo").asString());
                        }

                        return gustos; //devuelve los gustos de un registrado.
                    }
                } );

                for (int j = 0; j < gustosUsuario.size(); j++) { // por cada gusto del usuario.
                	for(int k = 0; k < gustosDeRegistrado.size(); k++) { // por cada gusto del registrado.
                		if(gustosUsuario.get(j).equals(gustosDeRegistrado.get(k))){ // si el gusto del usuario es igual al gusto del registrado.
                			int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
                			puntuacion += 1;
                			hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion);  // se suma "1" a la puntuación del registrado en el hashmap "quÃ­mica"
                		}
                	}
                }

                // COMPARADORES ESPECIALES

                // Comparar la carrera del registrado actual con la carrera del usuario.
                String carreraRegistrado = session.readTransaction( new TransactionWork<String>()
	        	{
		            @Override
		            public String execute( Transaction tx )
		            {
		                Result result = tx.run( "MATCH(p:Persona {nombre:\"" + nombreRegistrado + "\"}) RETURN p.carrera");		         
		                List<Record> registros = result.list();
		                String nombre = registros.get(0).get("p.carrera").asString();
		                return nombre; //devuelve los gustos de un usuario.
		            }
	        	} );

                // Comparar la edad del registrado actual con la edad del usuario.
                if(carreraRegistrado.equals(carreraUsuario)) {
                	int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
        			puntuacion += 1;
        			hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
                }

                // Comparar el sexo del registrado actual con el sexo del usuario.
                String sexoRegistrado = session.readTransaction( new TransactionWork<String>()
                    {
                    @Override
                    public String execute( Transaction tx )
                        {
                            Result result = tx.run( "MATCH(p:Persona {nombre:\"" + nombreRegistrado + "\"}) RETURN p.sexo");
                            List<Record> registros = result.list();
                            String nombre = registros.get(0).get("p.sexo").asString();
                            return nombre; //devuelve los gustos de un usuario.
                        }
                    } );

                if(!sexoRegistrado.equals(sexoUsuario)) { // si el sexo del registrado es diferente al sexo del usuario.
                    	int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
                        puntuacion += 1;
                        hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
                }

                // Obtener la edad del registrado actual y compararla con la edad del usuario.
                String edadTemp2 = session.readTransaction( new TransactionWork<String>()
            	{
    	            @Override
    	            public String execute( Transaction tx )
    	            {
    	                Result result = tx.run( "MATCH(p:Persona {nombre:\"" + nombreRegistrado + "\"}) RETURN p.edad");		         
    	                List<Record> registros = result.list();
    	                String nombre = registros.get(0).get("p.edad").asString();
    	                return nombre; //devuelve los gustos de un usuario.
    	            }
            	} );
    			int edadRegistrado = Integer.parseInt(edadTemp2);
    			
    			if((edadRegistrado - edadUsuario) <= 2 && (edadRegistrado - edadUsuario) >= -2) {
    				int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
                    puntuacion += 1;
                    hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
    			}
            }


            LinkedList<String> recomendaciones = new LinkedList<>(); // Creación de la lista de NOMBRES DE REGISTRADOS recomendados para el usuario.

            // obtener el valor mas alto de las values del hashmap.
            int max = Collections.max(hashmapDeQuimica.values());
            for (Map.Entry<String, Integer> entry : hashmapDeQuimica.entrySet()) {
                if (entry.getValue() == max && entry.getValue() !=0) {
                    recomendaciones.add(entry.getKey());
                }
            }
            
            if(recomendaciones.size() == 0) {
            	recomendaciones.add("No se encontró un Match");
            }
        	return recomendaciones;
        }
   }




    /**
     * Método para registrar un nuevo usuario.
     * @param carnet
     * @param carrera
     * @param edad
     * @param email
     * @param instagram
     * @param nombre
     * @param sexo
     * @param gusto1
     * @param gusto2
     * @param gusto3
     * @param gusto4
     * @param gusto5
     * @return
     */
    public boolean registrarNuevo(String carnet, String carrera, String edad, String email, String instagram, String nombre, String sexo, String gusto1, String gusto2, String gusto3, String gusto4, String gusto5) {

        try (Session session = driver.session()) {
            boolean registrados = false;
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { // CreaciÃ³n de PERSONA sin tener relaciones.

                    String cadena = stringCreateProfile(carnet, carrera, edad, email, instagram, nombre, sexo);
                    Result result = tx.run(cadena);
                    return true;
                }


            });

        }

        try (Session session = driver.session()) {
            boolean registrados = false;
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { // Link a un gusto.

                    String cadena = createGusto(carnet, gusto1);
                    Result result = tx.run(cadena);
                    return true;
                }
            });
        }

        try (Session session = driver.session()) {
            boolean registrados = false;
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { // Link a un gusto.

                    String cadena = createGusto(carnet, gusto2);
                    Result result = tx.run(cadena);
                    return true;
                }
            });
        }

        try (Session session = driver.session()) {
            boolean registrados = false;
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { // Link a un gusto.

                    String cadena = createGusto(carnet, gusto3);
                    Result result = tx.run(cadena);
                    return true;
                }
            });
        }

        try (Session session = driver.session()) {
            boolean registrados = false; // Link a un gusto.
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {

                    String cadena = createGusto(carnet, gusto4);
                    Result result = tx.run(cadena);
                    return true;
                }
            });
        }

        try (Session session = driver.session()) {
            boolean registrados = false;
            registrados = session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { // Link a un gusto.

                    String cadena = createGusto(carnet, gusto5);
                    Result result = tx.run(cadena);
                    return true;
                }
            });
        }

        return true;

    }



    public String stringCreateProfile(String carnet, String carrera, String edad, String email, String instagram, String nombre, String sexo) {
        // CREATE (C21004:Persona {carnet: '21004', nombre:'Mark Albrand', email:'alb21004@uvg.edu.gt', instagram:'mark.albrand5', carrera:'ComputaciÃ³n', sexo:'masculino', signo:'tauro', zona:'10', edad:'20'})
        String string =  "CREATE (" + "C" + carnet + ":Persona {carnet: '" + carnet +  "', nombre:'" + nombre + "', email:'" + email + "', instagram:'" + instagram + "', carrera:'" + carrera + "', sexo:'" + sexo + "', edad:'" + edad + "'})";
        return string;
    }

    public String createGusto(String carnet, String gusto) {
        String string = "MATCH (p:Persona), (g:Gusto) WHERE p.carnet = '" + carnet + "' AND g.titulo = '" + gusto + "' CREATE (p)-[:LE_GUSTA {gusta:'si'}]->(g) RETURN p,g";
        return string;
    }



}
