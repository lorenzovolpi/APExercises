/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce.volpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Lorenzo Volpi
 */
public class CountingWords extends MapReduce<String, Integer, Integer>{

    public CountingWords(Path path) {
        super(path);
    }
    
    @Override
    protected Stream<Pair<String, List<String>>> read(Path path) {
        Reader r = new Reader(path);
        try{
            return r.read();
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    @Override
    protected Stream<Pair<String, Integer>> map(Stream<Pair<String, List<String>>> is) {
        return is.
                flatMap(p -> p.value.stream()).
                flatMap(s -> 
                    Arrays.asList(s.
                        trim().
                        replaceAll("[.,:;!?\\-\"'()_\\[\\]—’‘“”]+", " ").
                        split(" ")
                    ).
                    stream().
                    filter(s1 -> s1.length() > 3).
                    map(s1 -> s1.toLowerCase())
                ).
                collect(Collectors.groupingBy(s -> s, Collectors.reducing(0, e -> 1, Integer::sum))).
                entrySet().
                stream().
                map(e -> new Pair(e.getKey(), e.getValue()));
    }

    @Override
    protected int compare(String v1, String v2) {
        return v1.compareTo(v2);
    }

    @Override
    protected Stream<Pair<String, Integer>> reduce(Stream<Pair<String, List<Integer>>> is) {
        return is.
                map(p -> new Pair<String, Integer>(p.key, p.value.stream().reduce(0, (a, b) -> a+b)));
    }

    @Override
    protected void write(Stream<Pair<String, Integer>> is, Path path) {
        Writer w = new Writer();
        
        File f = Paths.get(path.toAbsolutePath().toString(), "cw_result.csv").toFile();
        try {
            w.write(f, is);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }
    
}
