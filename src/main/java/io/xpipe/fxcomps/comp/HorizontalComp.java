package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.SimpleCompStructure;
import javafx.scene.layout.HBox;

import java.util.List;

public class HorizontalComp extends Comp<CompStructure<HBox>> {

    private final List<Comp<?>> entries;

    public HorizontalComp(List<Comp<?>> comps) {
        entries = List.copyOf(comps);
    }

    @Override
    public CompStructure<HBox> createBase() {
        HBox b = new HBox();
        b.getStyleClass().add("horizontal-comp");
        for (var entry : entries) {
            b.getChildren().add(entry.createRegion());
        }
        return new SimpleCompStructure<>(b);
    }
}
