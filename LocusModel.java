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
public class LocusModel extends ElementSaisiModel{
    protected int chromosome;
    protected int start;
    protected int end;
    protected boolean getEsp;

    public LocusModel(int chromosome, int start, int end, boolean getEsp) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.getEsp = getEsp;
    }
    
}
