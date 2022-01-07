package io.xpipe.fxcomps.comp;

import io.xpipe.fxcomps.Comp;
import io.xpipe.fxcomps.CompStructure;
import io.xpipe.fxcomps.util.PlatformUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class AspectComp extends Comp<CompStructure<StackPane>> {

    private final Comp<?> comp;
    private final ObservableValue<Number> aspectRatio;

    public AspectComp(Comp<?> comp, ObservableValue<Number> aspectRatio) {
        this.comp = comp;
        this.aspectRatio = PlatformUtil.wrap(aspectRatio);
    }

    private double getRatio() {
        return aspectRatio.getValue().doubleValue();
    }

    @Override
    public CompStructure<StackPane> createBase() {
        var r = comp.createRegion();
        var sp = new StackPane(r);
        sp.setAlignment(Pos.CENTER);

        ChangeListener<? super Number> l = (c, o, n) -> {
            double w = Math.max(sp.getWidth() - sp.getPadding().getLeft() - sp.getPadding().getRight(), 0);
            double h = Math.max(sp.getHeight() - sp.getPadding().getTop() - sp.getPadding().getBottom(), 0);
            if (w == 0 || h == 0) {
                return;
            }

            boolean widthLimited = w / h < getRatio();
            if (widthLimited) {
                r.setPrefWidth(w);
                r.setPrefHeight(w / getRatio());
            } else {
                r.setPrefWidth(h * getRatio());
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
                if (h < 0) {
                    return Region.USE_COMPUTED_SIZE;
                }

                return h * getRatio();
            } else {
                return Region.USE_COMPUTED_SIZE;
            }
        }, sp.prefWidthProperty(), sp.prefHeightProperty(), sp.paddingProperty()));
        sp.maxWidthProperty().bind(sp.prefWidthProperty());

        return new CompStructure<>(sp);
    }
}
