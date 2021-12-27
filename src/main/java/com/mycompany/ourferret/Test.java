package com.mycompany.ourferret;

public class Test {

    public static void main(String[] args) {
        
        String[] test = new String[2];
        test[1] = "MICB";
        test[0] = "CCR5";
        FoundGeneAndRegion inputRegionList = NCBIData.getQueryFromGeneName(test, true);
        inputRegionList.affiche();
        

    }
}
