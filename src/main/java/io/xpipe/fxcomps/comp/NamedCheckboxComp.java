package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXCheckBox;
import io.xpipe.fxcomps.store.DefaultValueStoreComp;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class NamedCheckboxComp extends DefaultValueStoreComp<Boolean> {

    private final Supplier<String> label;

    public NamedCheckboxComp(boolean defaultVal, Supplier<String> label) {
        super(defaultVal);
        this.label = label;
    }

    @Override
    public Region createBase() {
        var cb = new JFXCheckBox();
        cb.selectedProperty().bindBidirectional(valueProperty());

        var text = new Label(label.get());
        cb.prefHeightProperty().bind(text.heightProperty());

        var box = new HBox(cb, text);
        box.setSpacing(5);
        return box;
    }
}
