/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ourferret;

/**
 *
 * @author youne
 */
public class FoundGeneAndRegion {
    private String geneNamesFound;
    private LocusModel[] locationOfFoundGenes;
    private Boolean allFound;
	
    FoundGeneAndRegion(String geneNamesFound, LocusModel[] locationOfFoundGenes, Boolean allFound){
        this.geneNamesFound = geneNamesFound; 
	this.locationOfFoundGenes = locationOfFoundGenes;
	this.allFound = allFound;
    }
	
    public String getFoundGenes(){
	return this.geneNamesFound;
    }
	
    public LocusModel[] getInputRegionArray(){
	return this.locationOfFoundGenes;
    }
	
    public Boolean getFoundAllGenes(){
	return this.allFound;
    }

    public void setGeneNamesFound(String geneNamesFound) {
        this.geneNamesFound = geneNamesFound;
    }

    public void setLocationOfFoundGenes(LocusModel[] locationOfFoundGenes) {
        this.locationOfFoundGenes = locationOfFoundGenes;
    }

    public void setAllFound(Boolean allFound) {
        this.allFound = allFound;
    }
	
}