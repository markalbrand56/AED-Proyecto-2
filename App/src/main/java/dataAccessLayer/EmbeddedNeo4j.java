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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
            List<String> ids = getRegistrados();

            for (int i = 0; i < ids.size(); i++) { // quitar al usuario de la lista.
                if(ids.get(i) == usuario){
                    ids.remove(i);
                }
            }

            for (int i = 0; i < ids.size(); i++) { // Hacer el hashmap
                if (!ids.get(i).equals(usuario)){
                    hashmapDeQuimica.put(ids.get(i), 0);
                }
            }
			 
			 // Obtener los gustos de la persona para compararlos con los demás.
			 LinkedList<String> gustosUsuario = session.readTransaction( new TransactionWork<LinkedList<String>>()
		        {
		            @Override
		            public LinkedList<String> execute( Transaction tx )
		            {
		                Result result = tx.run( "MATCH(p:Persona {nombre:\"" + usuario + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
		            LinkedList<String> gustos = new LinkedList<String>(); // Lo que le gusta a la persona, si es Mark, la paloma.
		            List<Record> registros = result.list();
		            for (int i = 0; i < registros.size(); i++) {
		           	 //myactors.add(registros.get(i).toString());
                        gustos.add(registros.get(i).get("gustos.titulo").asString());
		            }
		
		            return gustos; //devuelve los gustos de un usuario.
		        }
		    } );

            for (int i = 0; i < ids.size(); i++) {

                LinkedList<String> gustosDeRegistrado = session.readTransaction( new TransactionWork<LinkedList<String>>()
                {
                    @Override
                    public LinkedList<String> execute( Transaction tx )
                    {
                        Result result = tx.run( "MATCH(p:Persona {nombre:\"" + usuario + "\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
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
                    if(gustosUsuario.get(i) == gustosDeRegistrado.get(i)){
                        //hashmapDeQuimica.get(ids.get(i))
                        int puntuacion = hashmapDeQuimica.get(ids.get(i));
                        puntuacion = puntuacion ++;
                        hashmapDeQuimica.put(ids.get(i), puntuacion); //asignamos puntaje a cada uno de los elementos del hasmap.
                    }
                }

                LinkedList<String> recomendaciones = new LinkedList<>();
                






            }








            return idRecomendados;
        }
   }

}
