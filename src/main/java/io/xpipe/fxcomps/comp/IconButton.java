package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXButton;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.store.DefaultValueStoreComp;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.Size;
import javafx.css.SizeUnits;
import org.kordamp.ikonli.javafx.FontIcon;

public class IconButton extends DefaultValueStoreComp<CompStructure<JFXButton>, String> {

    private final IntegerProperty adjustment;
    private final Runnable listener;

    public IconButton(String defaultVal) {
        super(defaultVal);
        this.listener = null;
        this.adjustment = new SimpleIntegerProperty(0);
    }

    public IconButton(String defaultVal, Runnable listener) {
        super(defaultVal);
        this.listener = listener;
        this.adjustment = new SimpleIntegerProperty(0);
    }

    public IconButton(String defaultVal, Runnable listener, int adjust) {
        super(defaultVal);
        this.listener = listener;
        this.adjustment = new SimpleIntegerProperty(adjust);
    }
    
    @Override
    public CompStructure<JFXButton> createBase() {
        var button = new JFXButton();

        var fi = new FontIcon(getValue());
        valueProperty().addListener((c,o,n) -> {
            fi.setIconLiteral(n);
        });
        fi.setIconSize((int) new Size(fi.getFont().getSize(), SizeUnits.PT).pixels() + adjustment.get());
        button.fontProperty().addListener((c,o,n) -> {
            fi.setIconSize((int) new Size(n.getSize(), SizeUnits.PT).pixels() + adjustment.get());
        });
        fi.iconColorProperty().bind(button.textFillProperty());
        button.setGraphic(fi);
        button.setOnAction(e -> {
            if (listener != null) {
                listener.run();
            }
        });
        button.getStyleClass().add("icon-button-comp");
        return new CompStructure<>(button);
    }
}
