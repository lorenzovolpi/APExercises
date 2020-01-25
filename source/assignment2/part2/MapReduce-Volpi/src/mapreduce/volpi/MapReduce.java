/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce.volpi;

import java.nio.file.Path;
import java.util.List;
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
    
    /**
     * Funzione di lettura dell'input da memoria secondaria. 
     * 
     * @param path Path della cartella dalla quale leggere i file.
     * @return Uno stream di coppie <String, List<String>>
     * @Note Si è scelto di usare come return un tipo predefinito in
     *      quanto si assume che lo stream generato sia sempre di coppie
     *      in cui al nome del file è associata la lista delle linee del
     *      file lette.
     */
    protected abstract Stream<Pair<String, List<String>>> read(Path path);
    
    /**
     * Funzione che a partire da uno stream (ottenuto dalla lettura dei file) si applica
     * una funzione di mapping generando uno stream di coppie di diverso tipo.
     * 
     * @param is Stream di ingresso.
     * @return Stream risultante dal mapping
     */
    protected abstract Stream<Pair<K, V1>> map(Stream<Pair<String, List<String>>> is);
    
    /**
     * Funzione di comparazione tra due chiavi di coppie diverse dello stream.
     * 
     * @param v1 Prima chiave
     * @param v2 Seconda chiave
     * @return -1 se v1 < v2, 1 se v1 > v2, 0 altrimenti
     */
    protected abstract int compare(K v1, K v2);
    
    /**
     * Funzione che prende in ingresso lo stream prodotto dal mapping e combina in un unica lista,
     * per ogni chiave, i vari valori ad essa associati. 
     * 
     * @param is Stream di ingresso
     * @return uno stream di coppie dove ad ogni chiave è associata la lista dei valori associati.
     */
    protected Stream<Pair<K, List<V1>>> combine(Stream<Pair<K, V1>> is) {
        return is.
                sorted((p1, p2) -> compare(p1.key, p2.key)).
                collect(Collectors.groupingBy((p -> p.key), Collectors.mapping((p -> p.value), Collectors.toList()))).
                entrySet().
                stream().
                map(e -> new Pair(e.getKey(), e.getValue()));
    }
    
    /**
     * Funzione che prende in ingresso lo stream prodotto da combine e restituisce uno
     * stream risultato della riduzione dei valori delle lsite per ogni chiave.
     * 
     * @param is Stream di ingresso.
     * @return Uno stream di coppie in cui ad ogni chiave è associata la riduzione dei
     *      valori combinati con la combine.
     */
    protected abstract Stream<Pair<K, V2>> reduce(Stream<Pair<K, List<V1>>> is);
    
    /**
     * Funzione che scrive su un file .csv il risultato della pipeline.
     * 
     * @param is Stream risultante.
     * @param path Path su cui deve essere scritto il risultato.
     */
    protected abstract void write(Stream <Pair<K, V2>> is, Path path);
    
    
    /**
     * Funzione che esegue, effettivamente, la pipeline, combinando le altre funzioni.
     * Frozen spot.
     */
    public void compute() {
        write( reduce( combine( map( read(this.path) ) ) ), this.path );
    }
    
}
