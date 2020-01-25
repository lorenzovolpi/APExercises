/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;

/**
 *
 * @author Lorenzo Volpi
 */
public class DroneLabel extends JLabel implements PropertyChangeListener {

    private Boolean flying = false;
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(pce.getPropertyName().equals(Drone.PROP_LOC)) {
            String sloc = pce.getNewValue().toString();
            if(this.flying) this.setText(">" + sloc + "<");
            else this.setText("<" + sloc + ">");
            
            Location loc = (Location)pce.getNewValue();
            /*
            Rectangle pb = this.getParent().getBounds();
            if(loc.X < 0) loc.X = 0;
            if(loc.Y < 0) loc.Y = 0;
            if(loc.X + this.getWidth() > pb.width) loc.X = pb.width - this.getWidth();
            if(loc.Y + this.getHeight() > pb.height) loc.Y = pb.height - this.getHeight();
            */
            this.setBounds(loc.X, loc.Y, this.getWidth(), this.getHeight());
        }

        if(pce.getPropertyName().equals(Drone.PROP_FLYING)) {
            this.flying = (Boolean)pce.getNewValue();
            String loc = this.getText();
            if( (loc.startsWith("<") && loc.endsWith(">")) || (loc.startsWith(">") && loc.endsWith("<"))) loc = loc.substring(1, loc.length() - 1);
            if(this.flying) this.setText(">" + loc + "<");
            else this.setText("<" + loc + ">");
        }
    }
    
    
    
}
