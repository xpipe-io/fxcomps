package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.util.PlatformUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public final class SectionComp extends Comp<CompStructure<GridPane>> {

    private final ObservableValue<String> name;
    private final List<Entry> entries;

    public SectionComp() {
        this((ObservableValue<String>) null);
    }

    public SectionComp(ObservableValue<String> name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public SectionComp(List<Entry> entries) {
        this.name = null;
        this.entries = entries;
    }

    public SectionComp(ObservableValue<String> name, List<Entry> entries) {
        this.name = PlatformUtil.wrap(name);
        this.entries = entries;
    }

    @Override
    public CompStructure<GridPane> createBase() {
        GridPane grid = new GridPane();
        var struc = Structure.builder().value(grid);

        if (name != null) {
            var t = new Label();
            t.textProperty().bind(name);
            t.getStyleClass().add("header");
            grid.add(t, 0, 0, 2, 1);
            struc.sectionName(t);
        }

        int row = 1;
        for (var entry : entries) {
            var l = new Label();
            l.textProperty().bind(entry.name);
            struc.entryName(l);
            grid.add(l, 0, row);
            Region val = entry.comp().createRegion();
            struc.entryValue(val);
            grid.add(val, 1, row);
            GridPane.setHgrow(val, Priority.ALWAYS);
            row++;
        }

        grid.getStyleClass().add("section-comp");
        return new CompStructure<>(grid);
    }

    @Value
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Structure extends CompStructure<GridPane> {
        Label sectionName;
        @Singular
        List<Label> entryNames;
        @Singular
        List<Region> entryValues;
    }

    public static record Entry(ObservableValue<String> name, Comp<?> comp) {

        public Entry(String name, Comp<?> comp) {
            this(new SimpleObjectProperty<>(name), comp);
        }

        public Entry(ObservableValue<String> name, Comp<?> comp) {
            this.name = PlatformUtil.wrap(name);
            this.comp = comp;
        }
    }
}
