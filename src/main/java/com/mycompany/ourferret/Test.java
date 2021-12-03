package com.mycompany.ourferret;

public class Test {

    public static void main(String[] args) {
        String[] listgene = new String[1];
        listgene[0] = ("BDNF");
        FoundGeneAndRegion res;
        res = NCBIData.get_query_from_gene_name(listgene, true);
        String[] test = new String[1];
        test[0] = "CCR5";
        FoundGeneAndRegion inputRegionList = NCBIData.get_query_from_gene_name(test, true);
        inputRegionList.affiche();
        

    }
}
