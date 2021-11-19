package com.mycompany.ourferret;

import com.mycompany.ourferret.FoundGeneAndRegion;
import com.mycompany.ourferret.NCBIData;
public class Test {
    public static void main(String[] args) {
        String[] listgene = new String[1];
        listgene[0] = ("KCNT2");
        FoundGeneAndRegion res;
        NCBIData ncbi = new NCBIData();
        res = NCBIData.getQueryFromGeneName(listgene,true);
        res.affiche();

    }
}
