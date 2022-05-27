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
 * @author Administrator
 *
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
                    	 //myactors.add(registros.get(i).toString());
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
            
			// Obtener los gustos de la persona para compararlos con los demás.
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
			
			

            for (int i = 0; i < ids.size(); i++) { // quitar al usuario de la lista.
                if(ids.get(i).equals(nombreUsuario)){
                    ids.remove(i);
                }
            }

            for (int i = 0; i < ids.size(); i++) { // Hacer el hashmap
                if (!ids.get(i).equals(nombreUsuario)){
                    hashmapDeQuimica.put(ids.get(i), 0);
                }
            }
			 
			 // Obtener los gustos de la persona para compararlos con los demás.
			 LinkedList<String> gustosUsuario = session.readTransaction( new TransactionWork<LinkedList<String>>()
		        {
		            @Override
		            public LinkedList<String> execute( Transaction tx )
		            {
		                Result result = tx.run( "MATCH(p:Persona {carnet:\"" + usuario + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
		            LinkedList<String> gustos = new LinkedList<String>(); // Lo que le gusta a la persona, si es Mark, la paloma.
		            List<Record> registros = result.list();
		            for (int i = 0; i < registros.size(); i++) {
		           	 //myactors.add(registros.get(i).toString());
                        gustos.add(registros.get(i).get("gustos.titulo").asString());
		            }
		
		            return gustos; //devuelve los gustos de un usuario.
		        }
		    } );

            for (int usuarioActual = 0; usuarioActual < ids.size(); usuarioActual++) {

                String nombreRegistrado = ids.get(usuarioActual);

                LinkedList<String> gustosDeRegistrado = session.readTransaction( new TransactionWork<LinkedList<String>>()
                {
                    @Override
                    public LinkedList<String> execute( Transaction tx )
                    {
                        Result result = tx.run( "MATCH(p:Persona {nombre:\"" + nombreRegistrado + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
                        LinkedList<String> gustos = new LinkedList<String>(); // Lo que le gusta a la persona, si es Mark, la paloma.
                        List<Record> registros = result.list();
                        for (int i = 0; i < registros.size(); i++) {
                            //myactors.add(registros.get(i).toString());
                            gustos.add(registros.get(i).get("gustos.titulo").asString());
                        }

                        return gustos; //devuelve los gustos de un usuario.
                    }
                } );

                for (int j = 0; j < gustosUsuario.size(); j++) {
                	for(int k = 0; k < gustosDeRegistrado.size(); k++) {
                		if(gustosUsuario.get(j).equals(gustosDeRegistrado.get(k))){
                			//hashmapDeQuimica.get(ids.get(i))
                			int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
                			puntuacion += 1;
                			hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
                		}
                	}
                }
                
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
                
                if(carreraRegistrado.equals(carreraUsuario)) {
                	int puntuacion = hashmapDeQuimica.get(ids.get(usuarioActual));
        			puntuacion += 1;
        			hashmapDeQuimica.put(ids.get(usuarioActual), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
                }
                
                
                

            }

            LinkedList<String> recomendaciones = new LinkedList<>();

            // obtener el valor mas alto de las values del hashmap.
            int max = Collections.max(hashmapDeQuimica.values());
            for (Map.Entry<String, Integer> entry : hashmapDeQuimica.entrySet()) {
                if (entry.getValue() == max && entry.getValue() !=0) {
                    recomendaciones.add(entry.getKey());
                }
            }
            
            if(recomendaciones.size() == 0) {
            	recomendaciones.add("No se encontr� un Match");
            }
            
            return recomendaciones;
        }
   }

}
