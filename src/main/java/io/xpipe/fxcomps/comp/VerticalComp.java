package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

public class VerticalComp extends Comp<CompStructure<VBox>> {

    private final ObservableList<Comp> entries;

    public VerticalComp() {
        entries = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    }

    public VerticalComp(Comp... comps) {
        entries = FXCollections.synchronizedObservableList(FXCollections.observableArrayList(comps));
    }

    public VerticalComp(List<Comp> entries) {
        this.entries = FXCollections.synchronizedObservableList(FXCollections.observableList(entries));
    }

    public void add(Comp comp) {
        entries.add(comp);
    }

    public ObservableList<Comp> getEntries() {
        return entries;
    }

    @Override
    public CompStructure<VBox> createBase() {
        VBox b = new VBox();
        b.setFillWidth(true);
        //b.setAlignment(Pos.CENTER);
        b.getStyleClass().add("vertical-comp");
        var map = new HashMap<Comp, Region>();
        for (var entry : getEntries()) {
            var r = entry.createRegion();
            b.getChildren().add(r);
            map.put(entry, r);
        }
        getEntries().addListener((ListChangeListener<? super Comp>) c -> {
            b.getChildren().clear();
            for (var entry : getEntries()) {
                b.getChildren().add(map.computeIfAbsent(entry, Comp::createRegion));
            }
            map.keySet().retainAll(getEntries());
        });
        return new CompStructure<>(b);
    }
}
