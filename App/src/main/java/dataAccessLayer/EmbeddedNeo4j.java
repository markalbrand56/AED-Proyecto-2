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

        HashMap<String, String> hashmapDeQuimica = new HashMap<String, String>();

   	 try ( Session session = driver.session() )
        {
   		 
   		 
   		 LinkedList<String> actors = session.readTransaction( new TransactionWork<LinkedList<String>>()
            {
                @Override
                public LinkedList<String> execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH(p:Persona {nombre:\"Mark Albrand\"})-[:LE_GUSTA]->(gustos) RETURN gustos.titulo");
                    LinkedList<String> myactors = new LinkedList<String>(); // Lo que le gusta a la persona, si es Mark, la paloma.
                    List<Record> registros = result.list();
                    for (int i = 0; i < registros.size(); i++) {
                   	 //myactors.add(registros.get(i).toString());
                   	 myactors.add(registros.get(i).get("gustos.titulo").asString());
                    }




                    return myactors;
                }
            } );

            List<String> ids = getRegistrados();

            for (int i = 0; i < actors.size(); i++) {
                hashmapDeQuimica.put(ids.get(i), actors.get(i));
            }
            
            return actors;
        }
   }

}
