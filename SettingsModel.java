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
public class SettingsModel {
    private int version1KG; 
    private String output;
    private boolean versionHG;
    private boolean espMAF;

    public SettingsModel(int version1KG, String output, boolean versionHG, boolean espMAF) {
        this.version1KG = version1KG;
        this.output = output;
        this.versionHG = versionHG;
        this.espMAF = espMAF;
    }

    public int getVersion1KG() {
        return version1KG;
    }

    public void setVersion1KG(int version1KG) {
        this.version1KG = version1KG;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isVersionHG() {
        return versionHG;
    }

    public void setVersionHG(boolean versionHG) {
        this.versionHG = versionHG;
    }

    public boolean isEspMAF() {
        return espMAF;
    }

    public void setEspMAF(boolean espMAF) {
        this.espMAF = espMAF;
    }
    
    
}
