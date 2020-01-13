/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex02;

import javax.swing.JLabel;

/**
 *
 * @author Lorenzo Volpi
 */
public class TempLabel extends JLabel {

    @Override
    public void setText(String text) {
        if(text.length() == 0) super.setText(text);
        else {
            String s = "";
            if(text.endsWith("C")){
                s = text.substring(0, text.length()-1).trim();
            } else s = text.trim();

            Double c = Double.parseDouble(s);
            c *= 9.0/5.0;
            c+= 32.0;

            s = c + " F";
            super.setText(s);
            
        }
    }
    
}
