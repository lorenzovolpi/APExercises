/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce.volpi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Lorenzo Volpi
 */
public abstract class MapReduce<K, V1, V2> {
   
    private final Path path;
    
    public MapReduce(Path path) {
        this.path = path;
    }
    
    protected abstract Stream<Pair<String, List<String>>> read(Path path);
    
    protected abstract Stream<Pair<K, V1>> map(Stream<Pair<String, List<String>>> is);
    
    protected abstract int compare(K v1, K v2);
    
    protected abstract Stream<Pair<K, V2>> reduce(Stream<Pair<K, List<V1>>> is);
    
    protected abstract void write(Stream <Pair<K, V2>> is, Path path);
    
    protected Stream<Pair<K, List<V1>>> combine(Stream<Pair<K, V1>> is) {
        return is.
                sorted((p1, p2) -> compare(p1.key, p2.key)).
                collect(Collectors.groupingBy((p -> p.key), Collectors.mapping((p -> p.value), Collectors.toList()))).
                entrySet().
                stream().
                map(e -> new Pair(e.getKey(), e.getValue()));
    }
    
    public void compute() {
        write( reduce( combine( map( read(this.path) ) ) ), this.path );
    }
    
}
