/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drones.volpi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Lorenzo Volpi
 */
public class DronesPanel extends JPanel implements ComponentListener{
    
    private ArrayList<OutOfRangeListener> outOfRangeListeners = new ArrayList<>();
    
    public DronesPanel(){
        super();
    }

    @Override
    public Component add(Component comp) {
        comp.addComponentListener(this);
        
        return super.add(comp);
    }

    @Override
    public void remove(Component comp) {
        comp.removeComponentListener(this);
        super.remove(comp); 
    }

    @Override
    public void remove(int index) {
        this.getComponent(index).removeComponentListener(this);
        super.remove(index); 
    }
    
    public void addOutOfRangeListener(OutOfRangeListener l) {
        outOfRangeListeners.add(l);
    }
    
    public void removeOutOfRangeListener(OutOfRangeListener l) {
        outOfRangeListeners.remove(l);
    }

    @Override
    public void componentResized(ComponentEvent arg0) {}

    @Override
    public void componentMoved(ComponentEvent arg0) {
        Rectangle r = arg0.getComponent().getBounds(), tr = this.getBounds();
        tr.width -= 80;
        tr.height -= 16;
        //System.out.println("out" + r);
        if(r.x < 0 || r.y < 0 || r.x > tr.width || r.y > tr.height){
            
            OutOfRangeEvent oure = new OutOfRangeEvent(this, tr);
            for(OutOfRangeListener l : outOfRangeListeners) 
                l.outOfRange(oure);
        }
    }

    @Override
    public void componentShown(ComponentEvent arg0) {}

    @Override
    public void componentHidden(ComponentEvent arg0) {}
    
    
    
}
