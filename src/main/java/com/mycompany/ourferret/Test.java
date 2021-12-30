
package com.mycompany.ourferret;
import java.io.IOException;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {

    public static void testSortByWindow() {
        LocusModel[] testInput = new LocusModel[5];
        testInput[1] = new LocusModel("1", 50, 100);
        testInput[2] = new LocusModel("2", 500, 600);
        testInput[4] = new LocusModel("1", 75, 150);
        testInput[3] = new LocusModel("1", 200, 300);
        testInput[0] = new LocusModel("1", 300, 350);
        ArrayList<LocusModel> sortedInput = Data1KgModel.sortLocus(testInput);
        assertTrue(sortedInput.get(0).getChromosome().equals("1"));
        assertTrue(sortedInput.get(0).getStart() == 50);
        assertTrue(sortedInput.get(0).getEnd() == 150);
        assertTrue(sortedInput.get(1).getChromosome().equals("1"));
        assertTrue(sortedInput.get(1).getStart() == 200);
        assertTrue(sortedInput.get(1).getEnd() == 350);
        assertTrue(sortedInput.get(2).getChromosome().equals("2"));
        assertTrue(sortedInput.get(2).getStart() == 500);
        assertTrue(sortedInput.get(2).getEnd() == 600);
    }

    public static void main(String[] args) throws IOException {
        String[] test = new String[2];
        test[1] = "MICB";
        test[0] = "CCR5";
        FoundGeneAndRegion result;
        result = NCBIData.getQueryFromGeneName(test,true);
        result.affiche();

        
    }
}
