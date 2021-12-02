package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.store.DefaultValueStoreComp;
import io.xpipe.fxcomps.util.StrongBindings;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class FilterComp extends DefaultValueStoreComp<String> {

    public FilterComp() {
        super("");
    }

    @Override
    public Region createBase() {
        var bgLabel = new Label("Search ...", new FontIcon("mdi2m-magnify"));
        bgLabel.getStyleClass().add("background");
        var filter = new TextField();
        StrongBindings.bind(filter.textProperty(), valueProperty());

        bgLabel.visibleProperty().bind(Bindings.createBooleanBinding(() -> filter.getText().isEmpty() && !filter.isFocused(),
                filter.textProperty(), filter.focusedProperty()));

        var r = new LayerComp(List.of(new WrapperComp(bgLabel), new WrapperComp(filter))).createBase();
        r.getStyleClass().add("filter-comp");
        return r;
    }
}
