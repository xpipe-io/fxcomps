package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class LayoutComp extends Comp {

    private ObjectProperty<Comp> top;
    private ObjectProperty<Comp> bottom;
    private ObjectProperty<Comp> left;
    private ObjectProperty<Comp> right;
    private ObjectProperty<Comp> center;

    public LayoutComp() {
        this.top = new SimpleObjectProperty<>();
        this.bottom = new SimpleObjectProperty<>();
        this.left = new SimpleObjectProperty<>();
        this.right = new SimpleObjectProperty<>();
        this.center = new SimpleObjectProperty<>();
    }

    public Comp getTop() {
        return top.get();
    }

    public void setTop(Comp top) {
        this.top.set(top);
    }

    public ObjectProperty<Comp> topProperty() {
        return top;
    }

    public Comp getBottom() {
        return bottom.get();
    }

    public void setBottom(Comp bottom) {
        this.bottom.set(bottom);
    }

    public ObjectProperty<Comp> bottomProperty() {
        return bottom;
    }

    public Comp getLeft() {
        return left.get();
    }

    public void setLeft(Comp left) {
        this.left.set(left);
    }

    public ObjectProperty<Comp> leftProperty() {
        return left;
    }

    public Comp getRight() {
        return right.get();
    }

    public void setRight(Comp right) {
        this.right.set(right);
    }

    public ObjectProperty<Comp> rightProperty() {
        return right;
    }

    public Comp getCenter() {
        return center.get();
    }

    public void setCenter(Comp center) {
        this.center.set(center);
    }

    public ObjectProperty<Comp> centerProperty() {
        return center;
    }

    @Override
    public Region createBase() {
        var pane = new BorderPane();
        pane.getStyleClass().add("layout-comp");
        var comp = this;

        comp.topProperty().addListener((c, o, n) -> {
            pane.setTop(n.create());
        });
        if (comp.getTop() != null) pane.setTop(comp.getTop().create());

        comp.bottomProperty().addListener((c, o, n) -> {
            pane.setBottom(n.create());
        });
        if (comp.getBottom() != null) pane.setBottom(comp.getBottom().create());

        comp.leftProperty().addListener((c, o, n) -> {
            pane.setLeft(n.create());
        });
        if (comp.getLeft() != null) pane.setLeft(comp.getLeft().create());

        comp.rightProperty().addListener((c, o, n) -> {
            pane.setRight(n.create());
        });
        if (comp.getRight() != null) pane.setRight(comp.getRight().create());

        comp.centerProperty().addListener((c, o, n) -> {
            pane.setCenter(n.create());
        });
        if (comp.getCenter() != null) pane.setCenter(comp.getCenter().create());

        return pane;
    }
}
