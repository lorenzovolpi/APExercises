/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

/**
 *
 * @author Lorenzo Volpi
 */
public class Location {
    int X, Y;
    
    public Location() {
        this.X = 0;
        this.Y = 0;
    }
    
    public Location(int x, int y) {
        this.X = x;
        this.Y = y;
    }
    
    public void set(int x, int y) {
        this.X = x;
        this.Y = y;
    } 

    @Override
    public String toString() {
        return this.X + "," + this.Y;
    }
    
    
}
