/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce.volpi;

import java.nio.file.Paths;

/**
 *
 * @author Lorenzo Volpi
 */
public class MapReduceVolpi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length > 0) {
            InvertedIndex ii = new InvertedIndex(Paths.get(args[0]));
            ii.compute();
        }
    }
    
}