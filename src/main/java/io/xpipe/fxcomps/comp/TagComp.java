package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public abstract class TagComp extends Comp {

    protected final BooleanProperty shown = new SimpleBooleanProperty(true);
    protected TagContainerComp container;

    public void setContainer(TagContainerComp container) {
        this.container = container;
    }

    @Override
    protected final Region createBase() {
        var pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        var r = createTag();
        pane.getChildren().add(r);
        r.getStyleClass().add("tag-comp");
        pane.minWidthProperty().bind(r.prefWidthProperty());
        pane.minWidthProperty().bind(r.prefHeightProperty());
        return pane;
    }

    protected abstract Region createTag();

    public boolean isShown() {
        return shown.get();
    }

    public BooleanProperty shownProperty() {
        return shown;
    }
}
