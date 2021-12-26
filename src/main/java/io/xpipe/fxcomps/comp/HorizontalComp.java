package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.util.List;

public class HorizontalComp extends Comp<CompStructure<HBox>> {

    private final ObservableList<Comp<?>> entries;

    public HorizontalComp(List<Comp<?>> comps) {
        entries = FXCollections.synchronizedObservableList(FXCollections.observableArrayList(comps));
    }

    @Override
    public CompStructure<HBox> createBase() {
        HBox b = new HBox();
        b.setFillHeight(true);
        b.getStyleClass().add("horizontal-comp");
        for (var entry : entries) {
            b.getChildren().add(entry.createRegion());
        }
        entries.addListener((ListChangeListener<? super Comp<?>>) c -> {
            b.getChildren().clear();
            for (var entry : entries) {
                b.getChildren().add(entry.createRegion());
            }
        });
        return new CompStructure<>(b);
    }
}
