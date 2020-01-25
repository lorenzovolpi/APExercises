/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

import java.beans.*;
import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * JavaBean che mantiene due proprietà di sola lettura che indicano se il drone sta volando
 * e la sua posizione attuale, con rispettivi getter. Fornisce anche metodi pubblici per
 * far partire il volo del drone e per terminarlo e prevede dei PropertyChangeEvent per 
 * entrambe le proprietà. Prevede inoltre un VetoableChangeEvent per la proprietà Loc.
 * 
 * @author Lorenzo Volpi
 */
public class Drone implements Serializable {
    
    public static final String PROP_LOC = "loc";
    public static final String PROP_FLYING = "flying";
    
    private Timer timer = null;
    private Location loc = new Location();
    private Boolean flying = false;
    
    private PropertyChangeSupport pcs;
    private VetoableChangeSupport vcs;
    
    public Drone() {
        pcs = new PropertyChangeSupport(this);
        vcs = new VetoableChangeSupport(this);
    }
    
    public Location getLoc() {
        return new Location(this.loc.X, this.loc.Y);
    }

    private void setLoc(Location newLoc) {
        Location oldLoc = this.loc;
        try{
            vcs.fireVetoableChange(PROP_LOC, oldLoc, newLoc);
        } catch (PropertyVetoException ex) {
            newLoc.X = 0;
            newLoc.Y = 0;
        }
        
        this.loc = new Location(newLoc.X, newLoc.Y);
        pcs.firePropertyChange(PROP_LOC, oldLoc, newLoc);
    }
    
    public Boolean isFlying() {
        return this.flying;
    }
    
    private void setFlying(Boolean newFlying) {
        Boolean oldFlying = this.flying;
        this.flying = newFlying;
        pcs.firePropertyChange(PROP_FLYING, oldFlying, newFlying);
    }
    
    public void takeOff(Location initLoc) {
        this.setFlying(true);
        this.setLoc(initLoc);
        
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random rnd = new Random();
                int rndx = rnd.nextInt(21) - 10;
                int rndy = rnd.nextInt(21) - 10;
                Location loc = Drone.this.getLoc();
                loc.X += rndx;
                loc.Y += rndy;
                Drone.this.setLoc(loc);
            }
        }, 1000, 1000);
    }
    
    public void land() {
        if(this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        this.setFlying(false);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    /**
     * Aggiunge un VetoableChangeListener per la proprietà loc.
     * @param listener 
     */
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vcs.addVetoableChangeListener(listener);
    }
    
    /**
     * Aggiunge un VetoableChangeListener per la proprietà loc.
     * @param listener 
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vcs.removeVetoableChangeListener(listener);
    }
}
