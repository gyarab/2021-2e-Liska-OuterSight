/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outersight;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author foxjo
 */
public class FXMLDocumentController implements Initializable {

    private int dim, points;
    Object/*[]*/ arr;

    @FXML
    private Pane space;

    @FXML
    private TextField c1, c2, c3, c4, c5, c6, c7, c8;
    
    @FXML
    private Spinner<Integer> dimChoose, pointsChoose;

    @FXML
    private ScrollPane sp;

    @FXML
    private TextField cordinates;

    @FXML
    private void settingsChanged(ActionEvent event) throws IOException {

        dim = dimChoose.getValue();
        System.out.println(dim);

        Parent root = FXMLLoader.load(getClass().getResource("CordedSliceXML.fxml")); //není nutno loadovat pokaždé

        root.setLayoutX(30); //3000
        root.setLayoutY(sp.getHvalue() * 2600); //2600

        space.getChildren().add(root);

        
        
        c1.setVisible(false);
        c2.setVisible(false);
        c3.setVisible(false);
        c4.setVisible(false);
        c5.setVisible(false);
        c6.setVisible(false);
        c7.setVisible(false);
        c8.setVisible(false);

        for (int i = 0; i <= dim; i++) {
            if (i == 1) {
                c1.setVisible(true);
            }
            if (i == 2) {
                c1.setVisible(true);
            }
            if (i == 3) {
                c1.setVisible(true);
            }
            if (i == 4) {
                c1.setVisible(true);
            }
            if (i == 5) {
                c1.setVisible(true);
            }
            if (i == 6) {
                c1.setVisible(true);
            }
            if (i == 7) {
                c1.setVisible(true);
            }

            if (i == 8) {
                c1.setVisible(true);
            }
            
            //createSpace
        }
    }
        @Override
        public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> vF1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 8);
            vF1.setValue(4);

            dimChoose.setValueFactory(vF1);

            SpinnerValueFactory<Integer> vF2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 8);
            vF2.setValue(5);

            pointsChoose.setValueFactory(vF2);
            //createSpace();
        }
    
    

    private void createSpace() {
        Parent n;
        for (int i = 3; i <= dim; i++) {
            if(i % 2 == 0)
                for (int j = 0; j < points; j++) {
                    
                }
            else{
                n = new HBox();
            }
        }
    }

    public void arrIt() {
        Object o = null;
        //VBox n;
        for (int i = 3; i <= dim; i++) {
            Object[] p = new Object[points];
            for (int j = 0; j < points; j++) {
                p[j] = o;
            }
            o = p;
            //n.getChildren() = p;
        }
        arr = o;
    }

    public Object getCS(int[] indexes) {
        if(dim == 2) return arr;
        Object/*[]*/ o = arr;
        for (int i = 0; i < indexes.length; i++) {
            o = o[i];    
        }
        return o;
    }
    
    public int gDB(boolean upper, double midpoint, double maxDistance, double spread){
        if (upper) return (int) Math.floor((midpoint + maxDistance) / spread);
        return (int) Math.floor((midpoint - maxDistance) / spread); 
    }
 /*
    public void draw(){
        String[] preCords = cordinates.getText().split(";");
        double[] cords;
        for (int i = 0; i < preCords.length; i++) {
            cords[preCords.length - i] = Double.valueOf(preCords[i]);
        }
    }
     */
}
