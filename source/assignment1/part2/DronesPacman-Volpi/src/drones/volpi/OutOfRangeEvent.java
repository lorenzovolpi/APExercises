/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

import java.awt.Rectangle;
import java.util.EventObject;

/**
 *
 * @author Lorenzo Volpi
 */
public class OutOfRangeEvent extends EventObject {
    
    private Rectangle r;
    
    public OutOfRangeEvent(Object source, Rectangle r) {
        super(source);
        this.r = r;
    } 
    
    public double getXRange() {
        return this.r.getWidth();
    }
    
    public double getYRange() {
        return this.r.getHeight();
    }
    
}
