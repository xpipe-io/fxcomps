package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class BigIconButton extends ButtonComp {

    public BigIconButton(Supplier<String> name, Node graphic, Runnable listener) {
        super(name, graphic, listener);
    }

    @Override
    public Region createBase() {
        var vbox = new VBox();
        vbox.getStyleClass().add("vbox");
        vbox.setAlignment(Pos.CENTER);

        var icon = new StackPane(getGraphic());
        icon.setAlignment(Pos.CENTER);
        icon.getStyleClass().add("icon");
        vbox.getChildren().add(icon);

        var label = new Label(getName().get());
        label.getStyleClass().add("name");
        vbox.getChildren().add(label);

        var b = new JFXButton(null);
        b.setGraphic(vbox);
        b.setOnAction(e -> getListener().run());
        b.getStyleClass().add("big-icon-button-comp");
        return b;
    }
}
