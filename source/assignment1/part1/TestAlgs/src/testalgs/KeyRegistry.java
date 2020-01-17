/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testalgs;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe che mantiene un registro in cui ad ogni oggetto Class è associata
 * una chiave crittografica che da essa deve essere utilizzata.
 * 
 * @author Lorenzo Volpi
 */
public class KeyRegistry {
    
    private Map<Class, String> registry = new HashMap<>();
    
    public void add(Class c, String key) {
        registry.put(c, key);
    }
    
    public String get(Class c) {
        return registry.get(c);
    }
}
