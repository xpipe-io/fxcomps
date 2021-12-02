package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.store.ValueStoreComp;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.List;

public class LayerComp extends ValueStoreComp<List<Comp>> {

    public LayerComp(List<Comp> comps) {
        this.value.setValue(comps);
    }

    @Override
    public Region createBase() {
        var pane = new StackPane();
        for (var c : value.getValue()) {
            pane.getChildren().add(c.create());
        }
        pane.setAlignment(Pos.CENTER);
        return pane;
    }
}
