/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 *
 * @author bruno
 */
public class ChartView extends Application{

    private final ObservableNumberValue total;
    private JogosTask jogosTask = new JogosTask();
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Start");
        btn.setOnAction(
            e -> { 
                new Thread(jogosTask).start();
                System.out.println("Indo...");
            }
        
        );
        
        Rectangle recD1 = new Rectangle(0, 100);
        Rectangle recEmpate = new Rectangle(0, 100);
        Rectangle recD2 = new Rectangle(0, 100);
        
        HBox hbox = new HBox(recD1, recEmpate, recD2);
        hbox.setSpacing(0);
        makePercentualBinding(recD1.widthProperty(),this.jogosTask.vitoriasDupla1,total);
        makePercentualBinding(recEmpate.widthProperty(),this.jogosTask.empates,total);
        makePercentualBinding(recD2.widthProperty(),this.jogosTask.vitoriasDupla2,total);
        
        recD1.setFill(Paint.valueOf("red"));
        recEmpate.setFill(Paint.valueOf("black"));
        recD2.setFill(Paint.valueOf("blue"));
        
        Label lv1 = new Label();
        Label lEm = new Label();
        Label lv2 = new Label();
        Label lto = new Label();
        
        makeLabelPercBinding(lv1.textProperty(),"Dupla 1",recD1.widthProperty());
        makeLabelPercBinding(lEm.textProperty(),"Empates",recEmpate.widthProperty());
        makeLabelPercBinding(lv2.textProperty(),"Dupla 2",recD2.widthProperty());
        makeLabelBinding(lto.textProperty(),"Partidas",total);
        
        
        VBox boxLabels = new VBox(lv1,lEm,lv2,lto);
                
        VBox root = new VBox(btn,hbox,boxLabels);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void makeLabelPercBinding(StringProperty prop, String label, ObservableNumberValue val){
        
        
        
        prop.bind(Bindings.format("%s: %.2f%%", label, DoubleExpression.doubleExpression(val).divide(2.5)));
            
    }
    
    private void makeLabelBinding(StringProperty prop, String label, ObservableNumberValue val){
        prop.bind(Bindings.concat(label,": ",val));
    }
    
    
    private void makePercentualBinding(
            DoubleProperty prop, 
            ObservableNumberValue parte, 
            ObservableNumberValue total){
       
        prop.bind(
            Bindings.when(Bindings.equal(total, 0))
                    .then(0)
                    .otherwise(
                        DoubleExpression.doubleExpression(parte)
                        .divide(total)
                        .multiply(250)
                    ));        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public ChartView() {
        
        this.jogosTask = new JogosTask();
        this.total = this.jogosTask.vitoriasDupla1.add(jogosTask.empates).add(jogosTask.vitoriasDupla2);
    }

    
}
