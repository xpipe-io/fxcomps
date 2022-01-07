package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.scene.layout.VBox;

import java.util.List;

public class VerticalComp extends Comp<CompStructure<VBox>> {

    private final List<Comp<?>> entries;

    public VerticalComp(List<Comp<?>> comps) {
        entries = List.copyOf(comps);
    }

    @Override
    public CompStructure<VBox> createBase() {
        VBox b = new VBox();
        b.getStyleClass().add("vertical-comp");
        for (var entry : entries) {
            b.getChildren().add(entry.createRegion());
        }
        return new CompStructure<>(b);
    }
}
