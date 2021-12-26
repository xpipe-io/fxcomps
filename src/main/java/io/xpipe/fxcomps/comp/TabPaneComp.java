package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXTabPane;
import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.function.Supplier;

public class TabPaneComp extends Comp<CompStructure<JFXTabPane>> {

    @Override
    public CompStructure<JFXTabPane> createBase() {
        JFXTabPane tabPane = new JFXTabPane();
        tabPane.getStyleClass().add("tab-pane-comp");

        for (var e : entries) {
            Tab tab = new Tab();
            var ll = new Label(e.name().get(), new FontIcon(e.graphic()));
            ll.getStyleClass().add("name");
            ll.setAlignment(Pos.CENTER);
            tab.setGraphic(ll);
            var content = e.comp().createRegion();
            tab.setContent(content);
            tabPane.getTabs().add(tab);
            content.prefWidthProperty().bind(tabPane.widthProperty());
        }

        return new CompStructure<>(tabPane);
    }

    private final List<Entry> entries;

    public TabPaneComp(List<Entry> entries) {
        this.entries = entries;
    }

    public static record Entry(Supplier<String> name, String graphic, Comp<?> comp) {

    }
}
