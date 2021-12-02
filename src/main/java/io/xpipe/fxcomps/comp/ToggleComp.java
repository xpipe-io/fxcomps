package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXToggleButton;
import io.xpipe.fxcomps.store.DefaultValueStoreComp;
import javafx.scene.layout.Region;

public class ToggleComp extends DefaultValueStoreComp<Boolean> {

    public ToggleComp(boolean defaultVal) {
        super(defaultVal);
    }

    @Override
    public Region createBase() {
        var button = new JFXToggleButton();
        button.selectedProperty().bindBidirectional(valueProperty());
        return button;
    }
}
