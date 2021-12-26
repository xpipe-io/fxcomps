package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class SectionComp extends Comp<CompStructure<GridPane>> {

    private final Supplier<String> name;
    private final List<Entry> entries;

    public SectionComp() {
        this(() -> null);
    }

    public SectionComp(Supplier<String> name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public SectionComp(List<Entry> entries) {
        this.name = () -> null;
        this.entries = entries;
    }

    public SectionComp(Supplier<String> name, List<Entry> entries) {
        this.name = name;
        this.entries = entries;
    }

    @Override
    public CompStructure<GridPane> createBase() {
        var comp = this;
        GridPane grid = new GridPane();

        if (comp.getName() != null) {
            var t = new Label(comp.getName());
            t.getStyleClass().add("header");
            grid.add(t, 0, 0, 2, 1);
        }

        int row = 1;
        for (var entry : comp.getEntries()) {
            grid.add(new Label(entry.name().get()), 0, row);
            Region val = entry.comp().createRegion();
            grid.add(val, 1, row);
            GridPane.setHgrow(val, Priority.ALWAYS);
            row++;
        }

        grid.getStyleClass().add("section-comp");
        return new CompStructure<>(grid);
    }

    public void add(Supplier<String> name, Comp comp) {
        entries.add(new Entry(name, comp));
    }

    public void add(Supplier<String> name, Comp comp, Runnable help) {
        entries.add(new Entry(name, comp));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public String getName() {
        return name.get();
    }

    public static record Entry(Supplier<String> name, Comp comp) {

        public Entry(String name, Comp comp) {
            this(() -> name, comp);
        }
    }
}
