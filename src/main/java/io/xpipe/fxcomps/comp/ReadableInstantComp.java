package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.store.WrappedValueStoreComp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.Instant;

public class ReadableInstantComp extends WrappedValueStoreComp<Instant> {

    public ReadableInstantComp(Instant i) {
        super(new SimpleObjectProperty<>(i));
    }

    public ReadableInstantComp(ObservableValue<Instant> observableValue) {
        super(observableValue);
    }

    @Override
    protected Region createBase() {
        var label = new Label(new PrettyTime().format(getValue()));
        label.setAlignment(Pos.CENTER);
        valueProperty().addListener((c,o,n) -> {
            label.setText(new PrettyTime().format(getValue()));
        });
        label.getStyleClass().add("readable-instant-comp");
        label.setMinWidth(Region.USE_PREF_SIZE);
        return label;
    }
}
