package br.eti.kinoshita.jena;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class ArqOrderByTest {

//    @Benchmark
//    public String[] testOrderByString() throws IOException {
//        final String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
//                "SELECT ?label\n" + 
//                "WHERE {\n" + 
//                "  ?conc a skos:Concept .\n" + 
//                "  ?conc skos:prefLabel|skos:altLabel ?label .\n" + 
//                "  FILTER(STRSTARTS(LCASE(?label), \"t\"))\n" + 
//                "} \n" + 
//                "ORDER BY ?label";
//        return executeQueryWithOrderBy(queryString);
//    }

    @Benchmark
    public String[] testOrderByLang() throws IOException {
        final String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "SELECT ?label\n" +
                "WHERE {\n" +
                "  ?conc a skos:Concept .\n" +
                "  ?conc skos:prefLabel|skos:altLabel ?label .\n" +
                "  FILTER(STRSTARTS(LCASE(?label), \"t\"))\n" +
                "} \n" +
                "ORDER BY ?label";
        return executeQueryWithOrderBy(queryString);
    }

    @Benchmark
    public String[] testOrderByCollation() throws IOException {
        final String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX fn: <http://www.w3.org/2005/xpath-functions#>\n"  +
                "SELECT ?label\n" +
                "WHERE {\n" +
                "  ?conc a skos:Concept .\n" +
                "  ?conc skos:prefLabel|skos:altLabel ?label .\n" +
                "  FILTER(STRSTARTS(LCASE(?label), \"t\"))\n" +
                "} \n" +
                "ORDER BY fn:collation(\"fi\", ?label)";
        return executeQueryWithOrderBy(queryString);
    }

    private String[] executeQueryWithOrderBy(final String queryString) throws IOException {
        // Query String
        // --- Model
        Model model = ModelFactory.createDefaultModel();
        try (InputStream is = Main.class.getResourceAsStream("/ysa-skos.ttl")) {
            model.read(is, /* base */null, "TURTLE");
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw e;
        }
        // Query object
        Query query = QueryFactory.create(queryString);
        // Execute query
        try (QueryExecution qExec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qExec.execSelect();
            return convertResultSetToArray(results);
        }
    }

    private String[] convertResultSetToArray(ResultSet rs) {
        List<String> list = new LinkedList<>();
        while (rs.hasNext()) {
            final QuerySolution qs = rs.next();
            final String text = qs.toString();
            list.add(text);
        }
        return list.toArray(new String[0]);
    }

}
