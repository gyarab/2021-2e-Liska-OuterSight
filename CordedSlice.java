/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outersight;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

/**
 *
 * @author foxjo
 */
public class CordedSlice extends Pane{
    double[] cords = new double[16];
    Pane slice;
    Label brackets;
    Label[] corners;

    public CordedSlice(double size, double cornerSize, double[] cordinates) {
        
        setPrefSize(size, size);
        this.cords = cordinates;
        
        slice = new Pane();
        
        slice.setPrefSize(size - 2 * cornerSize , size - 2 * cornerSize);
        slice.setLayoutX(size - cornerSize);
        slice.setLayoutX(size - cornerSize);
        
        slice.setStyle("-fx-background-color: #" + "3CDA25");
        setStyle("-fx-background-color: #" + "E7ED9A");
    }
}
