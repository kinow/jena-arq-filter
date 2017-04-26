package br.eti.kinoshita.jena;

import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class Main {

    public static void main(String[] args) throws Throwable {
        // Query String
        final String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
                "SELECT ?label\n" + 
                "WHERE {\n" + 
                "  ?conc a skos:Concept .\n" + 
                "  ?conc skos:prefLabel|skos:altLabel ?label .\n" + 
                "  FILTER(STRSTARTS(LCASE(?label), \"t\"))\n" + 
                "} \n" + 
                "ORDER BY ?label";
        // --- Model
        Model model = ModelFactory.createDefaultModel();
        try (InputStream is = Main.class.getResourceAsStream("/ysa-skos.ttl")) {
            model.read(is, /* base */null, "TURTLE");
        }
        // Query object
        Query query = QueryFactory.create(queryString);
        // Execute query
        try (QueryExecution qExec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qExec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                System.out.println(solution);
            }
        }
    }

}
