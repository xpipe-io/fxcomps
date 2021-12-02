package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


public class AspectComp extends Comp {

    private Property<Comp> comp;
    private DoubleProperty aspectRatio;

    public AspectComp(Comp comp, double aspectRatio) {
        this.comp = new SimpleObjectProperty<>(comp);
        this.aspectRatio = new SimpleDoubleProperty(aspectRatio);
    }

    @Override
    public Region createBase() {
        var r = comp.getValue().create();
        var sp = new HBox(r);
        sp.setAlignment(Pos.CENTER);

        ChangeListener<? super Number> l = (c, o, n) -> {
            double w = Math.max(sp.getWidth() - sp.getPadding().getLeft() - sp.getPadding().getRight(), 0);
            double h = Math.max(sp.getHeight() - sp.getPadding().getTop() - sp.getPadding().getBottom(), 0);
            if (w == 0 || h == 0) {
                return;
            }

            boolean widthLimited = w / h < aspectRatio.get();
            if (widthLimited) {
                r.setPrefWidth(w);
                r.setPrefHeight(w / aspectRatio.get());
            } else {
                r.setPrefWidth(h * aspectRatio.get());
                r.setPrefHeight(h);
            }
        };

        r.maxWidthProperty().bind(r.prefWidthProperty());
        r.maxHeightProperty().bind(r.prefHeightProperty());
        r.minWidthProperty().bind(r.prefWidthProperty());
        r.minHeightProperty().bind(r.prefHeightProperty());

        sp.widthProperty().addListener(l);
        sp.heightProperty().addListener(l);

        sp.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            if (sp.prefHeightProperty().isBound()) {
                var h = sp.getPrefHeight() - sp.getPadding().getTop() - sp.getPadding().getBottom();
                return h * aspectRatio.get();
            } else {
                return Region.USE_COMPUTED_SIZE;
            }
        }, sp.prefWidthProperty(), sp.prefHeightProperty(), sp.paddingProperty()));
        sp.maxWidthProperty().bind(sp.prefWidthProperty());

        return sp;
    }
}
