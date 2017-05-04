package br.eti.kinoshita.jena;

import org.apache.jena.sparql.expr.nodevalue.NodeValueLang;
import org.apache.jena.sparql.expr.nodevalue.NodeValueSortKey;

public class Sorting {

    public static void main(String[] args) {
        NodeValueSortKey nvsk1 = new NodeValueSortKey("Casa", "es");
        NodeValueSortKey nvsk2 = new NodeValueSortKey("Casa", "pt");
        System.out.println(nvsk1.equals(nvsk2));
        
        NodeValueLang nvl1 = new NodeValueLang("Casa", "es");
        NodeValueLang nvl2 = new NodeValueLang("Casa", "pt");
        System.out.println(nvl1.equals(nvl2));
    }

}
