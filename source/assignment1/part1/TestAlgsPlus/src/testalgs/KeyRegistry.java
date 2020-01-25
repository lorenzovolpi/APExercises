/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testalgs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
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
