package io.xpipe.fxcomps.comp;

import com.jfoenix.controls.JFXButton;
import io.xpipe.fxcomps.Comp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class ButtonComp extends Comp {

    private final Supplier<String> name;
    private final ObjectProperty<Node> graphic;
    private final Runnable listener;

    public ButtonComp(Supplier<String> name, Node graphic, Runnable listener) {
        this.name = name;
        this.graphic = new SimpleObjectProperty<>(graphic);
        this.listener = listener;
    }

    public Supplier<String> getName() {
        return name;
    }

    public Node getGraphic() {
        return graphic.get();
    }

    public ObjectProperty<Node> graphicProperty() {
        return graphic;
    }

    public Runnable getListener() {
        return listener;
    }

    @Override
    public Region createBase() {
        var button = new JFXButton(getName().get());
        button.setGraphic(getGraphic());
        button.setOnAction(e -> getListener().run());
        button.getStyleClass().add("button-comp");
        return button;
    }
}
