package outersight;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author foxjo
 */
public class FXMLDocumentController implements Initializable {

    private int dim, points;
    private DualType arr;
    private Parent originalCS;
    private double vR, rR;

    @FXML
    private Pane space;

    @FXML
    private VBox cordinates, rotation;

    @FXML
    private Spinner<Integer> dimChoose, pointsChoose;

    @FXML
    private ScrollPane sp;
    
    @FXML
    private TextField vr, rr, size;
    
    @FXML
    private ChoiceBox shapeSelect;
    
    @FXML
    private ColorPicker colorSelect;

    @FXML
    private void settingsChanged() {

        dim = dimChoose.getValue();
        points = pointsChoose.getValue();
        
        try {
            vR = Double.parseDouble(vr.getText());
            rR = Double.parseDouble(rr.getText());
        } catch (Exception exception) {}
        
        space.getChildren().clear();
        boxIt(dim - 2, space, new int[dim - 2]);
        correctCords();
        correctRotation();
        
        //Pane p = (Pane) space.getChildren().get(0);
        //p.autosize();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        SpinnerValueFactory<Integer> vF1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 8);
        vF1.setValue(4);
        dimChoose.setValueFactory(vF1);

        SpinnerValueFactory<Integer> vF2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3);
        vF2.setValue(2);
        pointsChoose.setValueFactory(vF2);
        
        shapeSelect.getItems().addAll("krychle", "koule");
        shapeSelect.setValue("koule");
        
        colorSelect.setValue(Color.YELLOW);
        
        settingsChanged();
        
    }

    /*
    Funguje rekurzivně.
    Metodu voláme, aby přidala vše, co má být v předaném Pane.
    Předáváme, kolik vrstev (zevnitř) bude každý přidávaný prvek mít, včetně nejvnější.
    Celkový počet vrstev odpovídá počtu dimenzí - 2.
     */
    private void boxIt(int level, Pane parent, int[] labeling) {
        /*
        Pokud level == 2 nebo points == 1 přidáváme CordedSlice a je jich stejný počet jako points.
        Buďto jsme totiž v nejvnitřnější vrstvě nebo je není třeba vytvářet pro jediný průřez.
        Ukončujem před dalším vytvářením Boxů.
         */
        try {
            if (level == 0|| points == 1) {
                Pane p = FXMLLoader.load(getClass().getResource("CordedSliceXML.fxml"));
                parent.getChildren().add(p);
                
                Label l = (Label) p.getChildren().get(1);
                l.setText("");
                for (int i = 0; i < labeling.length; i++) {
                    l.setText(l.getText().concat(Double.toString(  (labeling[i] * vR)))); //times distance
                    if (i != labeling.length - 1){
                        l.setText(l.getText().concat(" ;  "));
                    }
                }
                //l.setText(Integer.toString(labeling[0]));
                return;
            }
        } catch (IOException e) {
            System.out.println("Can not load \"CordedSliceXML.fxml\"");
        }
        /*
        Je-li ještě třeba vytvářet vrstvy (tzn. neplatí předešlé), přidáme nový Hbox, nebo Vbox
        podle toho, zda, s ním, bude počet vrstev (zevnitř) sudý, nebo lichý, rerpeknive.
        Volá pro něj boxIt za každý prvek jemu náležící (= points).
         */
        Pane m;
        if (level % 2 == 0) {
            parent.getChildren().add(m = new VBox());
            //m.setStyle("-fx-border-width: 2;" + "-fx-border-color: #AAAA00");
            /*
            Existuje-li více VBoxů (které jsou vždy napněny HBoxy) na stejné vrsvě,
            je třeba je orámovat, tvoří tak jednotlivé prvky i vzhledově a dají se skládat za sebe.
             */
            if (level != dim - 2) {
                m.setStyle("-fx-border-width: 2;" + "-fx-border-color: #AAAA00");
            }
        } else {
            parent.getChildren().add(m = new HBox());
        }
        level -= 1;
        for (int i = 1; i <= points; i++) {
            labeling[level] = i - 1;
            boxIt(level, m, labeling);
        }
    }
    /*
    private void arrIt() {
        DualType o = null;
        for (int i = 3; i <= dim; i++) {
            DualType p = new DualType();
            p.ay = new DualType[points];
            for (int j = 0; j < points; j++) {
                p.ay[j] = o;
            }
            o = p;
        }
        arr = o;
    }
*/
    public Object getSlice(int[] indexTable) {
        Pane p = (Pane) space.getChildren().get(0);
        
        for (int i = indexTable.length - 1; i >= 0; i--) {
            p = (Pane) p.getChildren().get(indexTable[i]);
        }
        return p.getChildren().get(0);
        /*
        DualType o = arr;
        for (int i = 0; i < indexTable.length; i++) {
            o = o.ay[indexTable[i]];
        }
        return o.cs;
        */
    }

    private int getDrawBoud(boolean upper, double midpoint, double maxDistance, double spread) {
        if (upper) {
            return (int) Math.floor((midpoint + maxDistance) / spread);
        }
        return (int) Math.floor((midpoint - maxDistance) / spread);
    }

    /*
    opravuje počet řádek pro zadávání souřadnic a to přidáváním a ubíráním HBoxů
    s dětmi Labelem (pozice 0) a samotným TextFieldem (pozice 1)
    */
    private void correctCords() {
        int children = cordinates.getChildren().size();
        /*
        cyklus po větší z počtu rozměrů a řádků pro zadávání, překročí-li počet rozměrů
        a pokračuje ubírá nadbytečné řádky, pokračuje-li nad počet řádků, začne je přidávat
        */
        for (int i = 1; i <= Math.max(dim, children); i++) {
            if (i > dim) {
                cordinates.getChildren().remove(dim);
            }
            if (children < i) {
                try {
                    HBox m = FXMLLoader.load(getClass().getResource("EnterCordsXML.fxml"));
                    //přidání právě načteného HBoxu m
                    cordinates.getChildren().add(m);
                    //vezme Label, dítě od m, a opraví jeho text
                    Label l = (Label) m.getChildrenUnmodifiable().get(0); //cu
                    l.setText("t" + i + ": ");
                } catch (IOException e) {
                    System.out.println("EnterCordsXML.fxml file not found!");
                }
            }
        }
    }
    
    private void correctRotation(){
        int children = rotation.getChildren().size();

        for (int i = 1; i <= Math.max(dim, children); i++) {
            if (i > dim) {
                rotation.getChildren().remove(dim);
            }
            if (children < i) {
                try {
                    HBox m = FXMLLoader.load(getClass().getResource("EnterCordsXML.fxml"));
                    rotation.getChildren().add(m);
                    Label l = (Label) m.getChildrenUnmodifiable().get(0);
                    l.setText("c" + i + ": ");
                } catch (IOException e) {
                    System.out.println("EnterCordsXML.fxml file not found!");
                }
            }
        }
    }
    
    
    private double[] centerCords() {
        double[] cc = new double[dim];
        for (int i = 0; i < cordinates.getChildren().size(); i++) {
            /*
            bereme dítě okénka s řádky na pozici i, tedy řádek, jeho dítě na pozici 1,
            tedy Label, a vypiseme jeho obsah
            */
            HBox h = (HBox) cordinates.getChildren().get(i);
            TextField tf = (TextField) h.getChildren().get(1);
            cc[i] = Double.parseDouble(tf.getText());
        }
        return cc;
    }
    
    private double distance2Dplus(double[] d, double[] e){
        double tot = 0;
        for (int i = 0; i < dim - 2; i++) {
            tot += Math.pow(Math.abs(e[i] - d[i + 2]), 2);
        }
        return Math.sqrt(tot);
    }
    
    @FXML
    private void insert(){
        if (shapeSelect.getValue() == "koule"){
            drawSphere((Pane) space.getChildren().get(0), dim - 2);
        }
        else if(shapeSelect.getValue() == "krychle"){
            
        }
    }
    
    private void drawSphere(Pane p, int level) {
        if(level == 0 || points == 1){
            Label l = (Label) p.getChildren().get(1);
            String s = l.getText();
            double[] e = new double[dim - 2];
            int j = 0;
            int k = 0;
            for (int i = 0; i < s.length(); i++) {
                if(s.charAt(i) == ' ') continue;
                if(s.charAt(i) == ';'){
                    e[k] = Double.parseDouble(s.substring(i - j, i - 1));
                    j = 0;
                    k++;
                }
                else j++;
            }      
            double r;
            double t = distance2Dplus(centerCords(), e);
            r = Math.sqrt(Math.pow(Double.parseDouble(size.getText()) / 2, 2) - t * t); 
            r = r / rR * 150;
            
            Pane q = (Pane) p.getChildren().get(0);
            q.getChildren().add(new Circle(10, 20, 5, Color.CRIMSON));
        }
        else{
            for (int i = 0; i < points; i++) {
                drawSphere((Pane) p.getChildren().get(i), level - 1);
            }
        }
    }
    
    @FXML
    private void tapulele(){
        int[] a = {1,1};
        Pane p = (Pane) getSlice(a);
        p.getChildren().add(new Circle(10, 20, 5, Color.CRIMSON));
        
        System.out.println(rR);
        
        System.out.println(shapeSelect.getValue());
        System.out.println(shapeSelect.getValue().equals("krychle"));
        System.out.println(colorSelect.getValue());
            
    }

    
}
