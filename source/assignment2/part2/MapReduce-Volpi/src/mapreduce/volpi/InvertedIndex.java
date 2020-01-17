/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce.volpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Lorenzo Volpi
 */
public class InvertedIndex extends MapReduce<String, Pair<String, Integer>, String>{
    
    public InvertedIndex(Path path){
        super(path);
    }
    
    @Override
    protected Stream<Pair<String, List<String>>> read(Path path) {
        Reader r = new Reader(path);
        try {
            return r.read();
        } catch (IOException ex) {
            System.out.println(ex);
            return Stream.empty();
        }
    }

    @Override
    protected Stream<Pair<String, Pair<String, Integer>>> map(Stream<Pair<String, List<String>>> is) {
        return is.
                <Pair<String, Pair<Integer, String>>>flatMap(p -> IntStream.
                        range(0, p.value.size()).
                        mapToObj(i -> new Pair<String, Pair<Integer, String>>(p.key, new Pair<Integer, String>(i, p.value.get(i))))
                ).
                flatMap((Pair<String, Pair<Integer, String>> p) -> 
                    Arrays.stream((String[])p.value.value.
                        trim().
                        replaceAll("[.,:;!?\\-\"'()_\\[\\]—’‘“”]+", " ").
                        split(" ")
                    ).
                    filter(s1 -> s1.length() > 3).
                    map(s1 -> s1.toLowerCase()).
                    distinct().
                    map((Function<? super String, ? extends Pair<String, Pair<String, Integer>>>) (String s1) -> new Pair(s1, new Pair<String, Integer>(p.key, p.value.key)))
                );
                
    }

    @Override
    protected int compare(String v1, String v2) {
        return v1.compareTo(v2);
    }

    @Override
    protected Stream<Pair<String, String>> reduce(Stream<Pair<String, List<Pair<String, Integer>>>> is) {
        return is.
                peek(p -> Collections.sort(p.value, (p1, p2) -> p1.key.compareTo(p2.key) == 0 ? (p1.value - p2.value) : p1.key.compareTo(p2.key))).
                map(p -> new Pair(p.key, p.value.stream().map(p1 -> p.key + ", " + p1.key + ", " + p1.value).collect(Collectors.joining("\n"))));
    }

    @Override
    protected void write(Stream<Pair<String, String>> is, Path path) {
        File f = Paths.get(path.toAbsolutePath().toString(), "ii_result.csv").toFile();
        try {
            PrintStream ps = new PrintStream(f);
            is.sorted(Comparator.comparing(Pair::getKey)).
                forEach(p -> ps.println(p.getValue()));
        ps.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }
    
}
