package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXButton;
import io.xpipe.fxcomps.Comp;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class SectionComp extends Comp {

    private final Supplier<String> name;
    private final List<Entry> entries;

    public SectionComp() {
        this(() -> null);
    }

    public SectionComp(Supplier<String> name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    @Override
    public Region createBase() {
        var comp = this;
        GridPane grid = new GridPane();

        var t = new Text(comp.getName());
        t.setStyle("-fx-font-weight: bold");
        TextFlow name = new TextFlow(t);
        grid.add(name, 0, 0, 3, 1);

        int row = 1;
        for (var entry : comp.getEntries()) {
            grid.add(new Label(entry.name().get()), 0, row);
            Region val = entry.comp().create();
            grid.add(val, 1, row);
            GridPane.setHgrow(val, Priority.ALWAYS);

            if (entry.help() != null) {
                var help = new JFXButton("?");
                help.setOnAction(e -> entry.help().run());
                help.getStyleClass().add("help");
                help.prefHeightProperty().bind(val.heightProperty());
                grid.add(help, 2, row);
            }

            row++;
        }

        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    public void add(Supplier<String> name, Comp comp) {
        entries.add(new Entry(name, comp, null));
    }

    public void add(Supplier<String> name, Comp comp, Runnable help) {
        entries.add(new Entry(name, comp, help));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public String getName() {
        return name.get();
    }

    public static record Entry(Supplier<String> name, Comp comp, Runnable help) {

        public Entry(Supplier<String> name, Comp comp) {
            this(name, comp, null);
        }
    }
}
