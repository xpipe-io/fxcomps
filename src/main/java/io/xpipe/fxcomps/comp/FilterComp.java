package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.store.ValueStoreComp;
import io.xpipe.fxcomps.util.StrongBindings;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.kordamp.ikonli.javafx.FontIcon;

public class FilterComp extends ValueStoreComp<FilterComp.Structure, String> {

    @Value
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Structure extends CompStructure<StackPane> {
        FontIcon inactiveIcon;
        Label inactiveText;
        TextField text;
    }

    @Override
    public Structure createBase() {
        var fi = new FontIcon("mdi2m-magnify");
        var bgLabel = new Label("Search ...", fi);
        bgLabel.getStyleClass().add("background");
        var filter = new TextField();
        StrongBindings.bind(filter.textProperty(), valueProperty());
        bgLabel.visibleProperty().bind(Bindings.createBooleanBinding(() -> filter.getText().isEmpty() && !filter.isFocused(),
                filter.textProperty(), filter.focusedProperty()));

        var stack = new StackPane(bgLabel, filter);
        stack.getStyleClass().add("filter-comp");

        return Structure.builder().inactiveIcon(fi).inactiveText(bgLabel).text(filter).value(stack).build();
    }
}
