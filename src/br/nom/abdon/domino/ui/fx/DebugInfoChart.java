
package br.nom.abdon.domino.ui.fx;

import java.util.Collection;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

/**
 *
 * @author Bruno
 */
public class DebugInfoChart extends Region {

    private final TilePane pane;

    public DebugInfoChart() {
        this(10);
    }
    public DebugInfoChart(int  numLinhas) {
        this.pane = new TilePane(Orientation.VERTICAL);
        this.setPrefRows(numLinhas);
        super.getChildren().add(pane);
    }
    
    public void addDebugInfo(String desc, Object val) {
        Text text = new Text();
        text.textProperty().bind(Bindings.concat(desc,": ",val));
        this.pane.getChildren().add(text);
    }

    final void setPrefRows(int rows){
        this.pane.setPrefRows(rows);
    }
    
}
