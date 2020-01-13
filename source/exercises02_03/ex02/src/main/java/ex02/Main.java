/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex02;

/**
 *
 * @author Lorenzo Volpi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //ex02
        TempLabel tl = new TempLabel();
        tl.setText("0 C");
        System.out.println("0 C -> " + tl.getText());
        tl.setText("1 C");
        System.out.println("1 C -> " + tl.getText());
        tl.setText("13 C");
        System.out.println("13 C -> " + tl.getText());
        tl.setText("-7 C");
        System.out.println("-7 C -> " + tl.getText());
        
        //ex02
    }
    
}
