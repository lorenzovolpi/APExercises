/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

import java.util.EventListener;

/**
 *
 * @author Lorenzo Volpi
 */
public interface OutOfRangeListener extends EventListener {
    
    public void outOfRange(OutOfRangeEvent evt);
    
}