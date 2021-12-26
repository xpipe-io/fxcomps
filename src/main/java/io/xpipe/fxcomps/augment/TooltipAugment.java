package io.xpipe.fxcomps.augment;

import com.jfoenix.controls.JFXTooltip;
import io.xpipe.fxcomps.CompStructure;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

import java.util.function.Supplier;

public class TooltipAugment<S extends CompStructure<?>> implements Augment<S> {

    static {
        JFXTooltip.setHoverDelay(Duration.millis(400));
        JFXTooltip.setVisibleDuration(Duration.INDEFINITE);
    }

    private final Property<String> text;

    public TooltipAugment(Supplier<String> text) {
        this.text = new SimpleObjectProperty<>(text.get());
    }

    public TooltipAugment(Property<String> text) {
        this.text = text;
    }

    @Override
    public void augment(S struc) {
        var tt = new JFXTooltip();
        tt.textProperty().bind(text);
        tt.setStyle("-fx-font-size: 11pt;");
        JFXTooltip.install(struc.get(), tt);
        tt.getStyleClass().add("tooltip-augment");
    }
}
