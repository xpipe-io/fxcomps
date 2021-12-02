package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class HorizontalComp extends Comp {

    private final ObservableList<Comp> entries;

    public HorizontalComp() {
        entries = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    }

    public HorizontalComp(Comp... comps) {
        entries = FXCollections.synchronizedObservableList(FXCollections.observableArrayList(comps));
    }

    public void add(Comp comp) {
        entries.add(comp);
    }

    public ObservableList<Comp> getEntries() {
        return entries;
    }

    @Override
    public Region createBase() {
        var comp = this;
        HBox b = new HBox();
        b.setFillHeight(true);
        b.setAlignment(Pos.CENTER);
        b.getStyleClass().add("horizontal-comp");
        for (var entry : comp.getEntries()) {
            b.getChildren().add(entry.create());
        }
        comp.getEntries().addListener((ListChangeListener<? super Comp>) c -> {
            b.getChildren().clear();
            for (var entry : comp.getEntries()) {
                b.getChildren().add(entry.create());
            }
        });
        return b;
    }
}
