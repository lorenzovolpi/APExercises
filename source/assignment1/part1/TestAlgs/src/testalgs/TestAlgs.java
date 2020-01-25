/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testalgs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lorenzo Volpi
 */
public class TestAlgs {
    
    /**
     * Funzione che carica da file le classi crittografiche da analizzare e testare.
     * @param dir Directory padre della cartella crypto
     * @return Un Map che ad ogni nome di classe associa l'oggetto classe caricato.
     */
    private static Map<String, Class> loadClasses(Path dir) {
        Map<String, Class> res = new HashMap<>();
        try {
            File[] files = Path.of(dir.toString(), "crypto\\algos\\").toFile().listFiles();
            URL[] urls = new URL[]{dir.toUri().toURL()};
            ClassLoader cl = new URLClassLoader(urls);
            
            for(File f : files) {
                if(f.getName().endsWith(".class")) {
                    String cn = f.getName().replace(".class", "");
                    try {
                        Class c = Class.forName(("crypto.algos." + cn), true, cl);
                        res.put(c.getName(), c);
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        continue;
                    }
                }
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        
        return res;
    }
    
    /**
     * Carica da file il KeyRegistry delle classi e chiavi crittografiche.
     * @param dir Directory padre di crypto.
     * @param classes Map delle classi caricate.
     * @return Un KeyRegistry contenente le varie chiavi caricate.
     */
    private static KeyRegistry loadRegistry(Path dir, Map<String, Class> classes) {
        KeyRegistry r = new KeyRegistry();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dir.toString(), "crypto\\keys.list"), Charset.forName("UTF-8"));
            for(String l : lines) {
                String[] ls = l.trim().split(" ");
                Class c = classes.get(ls[0]);
                if(c != null) {
                    r.add(c, ls[1]);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        return r;
    }
    
    private static  void checkAlgos(Path dir, KeyRegistry registry, Map<String, Class> classes) {
        try {
            File[] files = Path.of(dir.toString(), "crypto\\algos\\").toFile().listFiles();
            List<String> secrets = Files.readAllLines(Path.of(dir.toString(), "\\crypto\\secret.list"));
            
            for(File f : files) {
                if(f.getName().endsWith(".class")) {
                    String cn = f.getName().replace(".class", "");
                    try {
                        Class c = classes.get("crypto.algos." + cn);
                        if(c == null) throw new ClassNotFoundException("Class not found.");
                        
                        System.out.println(c.getName() + ":");
                        Constructor constr;
                        Method enc = null, dec = null;
                        try {
                            constr = c.getConstructor(String.class);
                            Method[] ms = c.getMethods();
                            for (Method m : ms) {
                                if(m.getName().startsWith("enc") && m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(String.class)) enc = m;
                                if(m.getName().startsWith("dec") && m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(String.class)) dec = m;
                            }
                            if(enc == null) {
                                System.out.println("\tThe class has no public method which name starts with \"enc\" and with a single String parameter.");
                                continue;
                            }
                            if(dec == null) {
                                System.out.println("\tThe class has no public method which name starts with \"dec\" and with a single String parameter.");
                                continue;
                            }
                            
                        } catch (NoSuchMethodException ex) {
                            System.out.println("\tThe class has no public constructor with a signle String parameter.");
                            continue;
                        } catch (SecurityException ex) {
                            System.out.println(ex);
                            continue;
                        }
                        
                        for(String secret : secrets) {
                            String w = secret.trim();
                            String arg = registry.get(c);
                            if(arg == null) throw new ClassNotFoundException("Class not found.");
                            Object algo = constr.newInstance(registry.get(c));
                            String e = (String)enc.invoke(algo, w);
                            String d = (String)dec.invoke(algo, e);
                            
                            if(!d.equals(w) && !d.equals(w + "#".repeat(d.length() - w.length()))) {
                                System.out.println("\tKO: " + w + " -> " + e + " -> " + d);
                            } else {
                                //System.out.println("\tOK");
                            }
                        }
                        
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        continue;
                    } catch (InstantiationException ex) {
                        System.out.println(ex);
                    } catch (IllegalAccessException ex) {
                        System.out.println(ex);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex);
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TestAlgs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        Path dir;
        if(args.length > 0) {
            dir = Paths.get(args[0]);
        } else return;
        
        System.out.println(dir.toString());
        
        Map<String, Class> classes = loadClasses(dir);
        
        KeyRegistry registry = loadRegistry(dir, classes);
        
        checkAlgos(dir, registry, classes);
    }
    
}
