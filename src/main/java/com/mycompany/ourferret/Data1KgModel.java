/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ourferret;

import java.util.LinkedList;
import javax.swing.SwingWorker;

/**
 *
 * @author youne
 */
public class Data1KgModel extends SwingWorker<Integer,String>{
    String ftpAdress;
    LocusModel[] queries;
    boolean allSNPsFound;

    public Data1KgModel(String ftpAdress, LocusModel[] queries, boolean allSNPsFound) {
        this.ftpAdress = ftpAdress;
        this.queries = queries;
        this.allSNPsFound = allSNPsFound;
    }

    public String getFtpAdress() {
        return ftpAdress;
    }

    public void setFtpAdress(String ftpAdress) {
        this.ftpAdress = ftpAdress;
    }

    public LocusModel[] getQueries() {
        return queries;
    }

    public void setQueries(LocusModel[] queries) {
        this.queries = queries;
    }

    public boolean isAllSNPsFound() {
        return allSNPsFound;
    }

    public void setAllSNPsFound(boolean allSNPsFound) {
        this.allSNPsFound = allSNPsFound;
    }
    
    public void GeneProcess(LinkedList<ElementSaisiModel> geneQueries, SettingsModel settings){
        publish("Looking up gene locations...");
        FoundGeneAndRegion[] geneLocationFromGeneName = {null};
        if(geneQueries.get(0).getClass().getSimpleName().equals("GeneByNameModel")){
            String[] geneList;
            geneList = new String[geneQueries.size()];
            for (int e=0; e<geneQueries.size(); e++){
                geneList[e] = ((GenebyNameModel)(geneQueries.get(e))).getName();
            }
            geneLocationFromGeneName[0] = NCBIData.getQueryFromGeneName(geneList,settings.isVersionHG());
        }else{
            String[] geneList;
            geneList = new String[geneQueries.size()];
            for (int e=0; e<geneQueries.size(); e++){
                geneList[e] = Integer.toString(((GenebyIDModel)geneQueries.get(e)).getId());
            }
            geneLocationFromGeneName[0] = NCBIData.getQueryFromGeneID(geneList,settings.isVersionHG());
            }        
    }

    @Override
    protected Integer doInBackground() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
