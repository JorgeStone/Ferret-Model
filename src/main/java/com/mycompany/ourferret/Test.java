package com.mycompany.ourferret;

public class Test {

    public static void main(String[] args) {
        String[] listgene = new String[1];
        listgene[0] = ("BDNF");
        FoundGeneAndRegion res;
        res = NCBIData.getQueryFromGeneName(listgene, true);
        String[] test = new String[1];
        test[0] = "CCR5";
        FoundGeneAndRegion inputRegionList = NCBIData.getQueryFromGeneName(test, true);
        inputRegionList.affiche();
        

    }
}
